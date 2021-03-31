package com.aukocharlie.recorder4j.result;

import com.aukocharlie.recorder4j.constant.CommonConstants;
import com.aukocharlie.recorder4j.launch.EventRegistrar;
import com.sun.jdi.*;
import com.sun.jdi.event.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author auko
 * @date 2021/3/20 17:37
 */
public class ThreadTrace {

    private static Map<ThreadReference, ThreadTrace> threads = new HashMap<>();

    private final ThreadReference thread;

    private int indent = 0;

    private EventRegistrar registrar;

    private Map<ObjectReference, Map<LocalVariable, Value>> varValuesInFrame = new HashMap<>();

    private Map<Field, Value> staticFieldVlaues = new HashMap<>();

    private Map<Field, Value> attributeValues = new HashMap<>();

    private boolean staticOrAttrModified = false;

    public ThreadTrace(ThreadReference thread, EventRegistrar registrar) {
        this.thread = thread;
        this.registrar = registrar;
    }

    public void handleMethodEntryEvent(MethodEntryEvent event) {
        Location methodLocation = event.location();
        println("\"thread=%s\", METHOD_ENTRY: %s", event.thread().name(), OutputUtils.locationToString(methodLocation));
        indent += 4;
    }

    public void handleMethodExitEvent(MethodExitEvent event) {
        Location location = event.location();
        indent -= 4;
        println("\"thread=%s\", METHOD_EXIT: %s, return=%s", event.thread().name(), OutputUtils.locationToString(location), event.returnValue());
    }

    /**
     * TODO: concurrent and array value displaying
     *
     * @param event
     */
    public void handleBreakpointEvent(BreakpointEvent event) {
        try {
            StackFrame topFrame = event.thread().frame(0);
            if (topFrame.visibleVariables().size() <= 0) {
                return;
            }

            Map<LocalVariable, Value> varValues = Optional.ofNullable(varValuesInFrame.get(topFrame.thisObject()))
                    .orElse(new HashMap<>(topFrame.visibleVariables().size()));

            boolean modified = false;
            for (LocalVariable var : topFrame.visibleVariables()) {
                if (!varValues.containsKey(var) || !topFrame.getValue(var).equals(varValues.get(var))) {
//                    println("old: %s(%s), new = %s", varValues.get(var), varValues.toString(), topFrame.getValue(var));
                    modified = true;
                    break;
                }
            }
            if (modified || staticOrAttrModified) {
                // Display modified-field's value
                List<String> kvString = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                sb.append(indentString())
                        .append("line: ")
                        .append(event.location().lineNumber())
                        .append("   ");

                sb.append("static: ");
                List<String> staticFieldString = staticFieldVlaues.entrySet().stream()
                        .map((entry) -> String.format(CommonConstants.KV_FORMAT, entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList());
                sb.append(String.join(", ", staticFieldString));

                sb.append(" | ");

                sb.append("attr: ");
                List<String> attrFieldString = attributeValues.entrySet().stream()
                        .map((entry) -> String.format(CommonConstants.KV_FORMAT, entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList());
                sb.append(String.join(", ", attrFieldString));

                sb.append(" | ");

                sb.append("local: ");
                for (LocalVariable var : topFrame.visibleVariables()) {
                    kvString.add(String.format("%s = %s", var.name(), topFrame.getValue(var)));
                    varValues.put(var, topFrame.getValue(var));
                }
                varValuesInFrame.put(topFrame.thisObject(), varValues);
                sb.append(String.join(", ", kvString));

                println(sb.toString());
                staticOrAttrModified = false;
            }
        } catch (IncompatibleThreadStateException e) {
//            e.printStackTrace();
        } catch (AbsentInformationException e) {
//            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {

        }
    }

    public void handleModificationWatchpointEvent(ModificationWatchpointEvent event) {
        Field field = event.field();
        Value value = event.valueToBe();
        Location location = event.location();
//        println("\"thread=%s\", MODIFICATION: %s %s = %s , %s", event.thread().name(), field.typeName(), field.name(), value, OutputUtils.locationToSimplifiedString(location));
        if (field.isStatic()) {
            staticFieldVlaues.put(field, value);
        } else {
            attributeValues.put(field, value);
        }
        staticOrAttrModified = true;
    }

    public void handleThreadStartEvent(ThreadStartEvent event) {

    }

    public void handleThreadDeathEvent(ThreadDeathEvent event) {

    }

    public static ThreadTrace currentThread(ThreadReference thread, EventRegistrar registrar) {
        if (!threads.containsKey(thread)) {
            threads.put(thread, new ThreadTrace(thread, registrar));
        }
        return threads.get(thread);
    }

    private void println(String format, Object... args) {
        System.out.print(indentString());
        System.out.printf(format, args);
        System.out.print("\n");
    }

    private void print(String format, Object... args) {
        System.out.printf(format, args);
    }

    private String indentString() {
        char[] spaces = new char[indent];
        for (int i = 0; i < indent; i++) {
            spaces[i] = ' ';
        }
        return new String(spaces);
    }
}
