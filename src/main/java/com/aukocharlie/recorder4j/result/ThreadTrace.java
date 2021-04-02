package com.aukocharlie.recorder4j.result;

import com.aukocharlie.recorder4j.constant.CommonConstants;
import com.aukocharlie.recorder4j.launch.Context;
import com.aukocharlie.recorder4j.launch.EventRegistrar;
import com.sun.jdi.*;
import com.sun.jdi.event.*;

import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author auko
 * @date 2021/3/20 17:37
 */
public class ThreadTrace {

    private static Map<ThreadReference, ThreadTrace> threads = new HashMap<>();

    private final ThreadReference thread;

    private OutputManager outputManager;

    private int indent = 0;

    private EventRegistrar registrar;

    private Map<Method, Map<LocalVariable, Value>> varValuesInMethod = new HashMap<>();

    private Map<Field, Value> staticFieldVlaues = new HashMap<>();

    private Map<Field, Value> attributeValues = new HashMap<>();

    private boolean staticOrAttrModified = false;

    public ThreadTrace(ThreadReference thread, EventRegistrar registrar, OutputManager outputManager) {
        this.thread = thread;
        this.registrar = registrar;
        this.outputManager = outputManager;
    }

    public void handleMethodEntryEvent(MethodEntryEvent event) {
        Location methodLocation = event.location();
        println("METHOD_ENTRY: line: %d  \"thread=%s\", %s", event.location().lineNumber(), event.thread().name(), OutputManager.locationToString(methodLocation));
        indent += 4;
    }

    public void handleMethodExitEvent(MethodExitEvent event) {
        Location location = event.location();
        indent -= 4;
        println("METHOD_EXIT: \"thread=%s\", %s, return=%s", event.thread().name(), OutputManager.locationToString(location), event.returnValue());
    }

    /**
     * TODO: array value displaying
     *
     * @param event
     */
    public void handleBreakpointEvent(BreakpointEvent event) {
//        Thread.yield();
//        println("breakpoint: " + event.location().lineNumber());
        try {
            StackFrame topFrame = event.thread().frame(0);
//            System.out.println(topFrame);
            if (topFrame.visibleVariables().size() <= 0 && !staticOrAttrModified) {
                // Nothing has modified
                return;
            }

            Map<LocalVariable, Value> varValues = Optional.ofNullable(varValuesInMethod.get(topFrame.location().method()))
                    .orElse(new HashMap<>(topFrame.visibleVariables().size()));

            boolean modified = false;
            for (LocalVariable var : topFrame.visibleVariables()) {
//                println("LOG: line: %d, old: %s(%s), new = %s", event.location().lineNumber(), varValues.get(var), varValues.toString(), topFrame.getValue(var));
                if (!varValues.containsKey(var) || !topFrame.getValue(var).equals(varValues.get(var))) {
                    modified = true;
                    break;
                }
            }
//            System.out.printf("%d, %s, %s\n", event.location().lineNumber(), modified, staticOrAttrModified);
            if (modified || staticOrAttrModified) {
                // Display modified-field's value
                List<String> kvString = new ArrayList<>();
                StringBuilder sb = new StringBuilder();
                sb.append("FIELD_MODIFIED: line: ")
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
                varValuesInMethod.put(topFrame.location().method(), varValues);
                sb.append(String.join(", ", kvString));

                println(sb.toString());
                staticOrAttrModified = false;
            }
        } catch (IncompatibleThreadStateException e) {
//            println("Exception: " + e);
//            e.printStackTrace();
        } catch (AbsentInformationException e) {
//            println("Exception: " + e);
//            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
//            println("Exception: " + e);
//            e.printStackTrace();

        }
    }

    public void handleModificationWatchpointEvent(ModificationWatchpointEvent event) {
        Field field = event.field();
        Value value = event.valueToBe();
        Location location = event.location();
//        println("FIELD_MODIFIED(static): line: %d, \"thread=%s\", %s %s = %s , %s", event.location().lineNumber(), event.thread().name(), field.typeName(), field.name(), value, OutputManager.locationToSimplifiedString(location));
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

    public static ThreadTrace currentThread(ThreadReference thread, EventRegistrar registrar, OutputManager outputManager) {
        if (!threads.containsKey(thread)) {
            threads.put(thread, new ThreadTrace(thread, registrar, outputManager));
        }
        return threads.get(thread);
    }

    private void println(String format, Object... args) {
        outputManager.print(indentString());
        outputManager.printf(format, args);
        outputManager.print("\n");
    }

    private String indentString() {
        char[] spaces = new char[indent];
        for (int i = 0; i < indent; i++) {
            spaces[i] = ' ';
        }
        return new String(spaces);
    }
}
