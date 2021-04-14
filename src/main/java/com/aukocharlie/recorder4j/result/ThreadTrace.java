package com.aukocharlie.recorder4j.result;

import com.aukocharlie.recorder4j.constant.CommonConstants;
import com.aukocharlie.recorder4j.constant.ThreadConstants;
import com.aukocharlie.recorder4j.launch.Context;
import com.aukocharlie.recorder4j.launch.EventRegistrar;
import com.aukocharlie.recorder4j.source.SourcePosition;
import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.tools.jdi.ArrayReferenceImpl;
import com.sun.tools.jdi.ClassTypeImpl;
import com.sun.tools.jdi.EventSetImpl;
import com.sun.tools.jdi.ObjectReferenceImpl;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author auko
 * @date 2021/3/20 17:37
 */
public class ThreadTrace {

    private static Map<ThreadReference, ThreadTrace> threads = new HashMap<>();

    private final ThreadReference thread;
    private int indent = 0;

    private Context context;
    private EventRegistrar registrar;

    private Map<Method, Map<LocalVariable, Value>> varValuesInMethod = new HashMap<>();
    private Map<Field, Value> staticFieldValues = new HashMap<>();
    private Map<ObjectReferenceImpl, FieldValuePairs> objectValues = new HashMap<>();
    private boolean staticOrAttrModified = false;

    public ThreadTrace(ThreadReference thread, EventRegistrar registrar, Context context) {
        this.thread = thread;
        this.registrar = registrar;
        this.context = context;
    }

    public static ThreadTrace currentThread(ThreadReference thread, EventRegistrar registrar, Context context) {
        if (!threads.containsKey(thread)) {
            threads.put(thread, new ThreadTrace(thread, registrar, context));
        }
        return threads.get(thread);
    }


    class FieldValuePairs extends HashMap<Field, Value> {
        @Override
        public String toString() {
            List<String> staticFieldString = entrySet().stream()
                    .map((entry) -> String.format(CommonConstants.KV_FORMAT, entry.getKey().name(), valueToString(entry.getValue())))
                    .collect(Collectors.toList());
            return String.format("{%s}", String.join(", ", staticFieldString));
        }
    }


    public void handleMethodEntryEvent(MethodEntryEvent event) {
        ThreadReference threadReference = event.thread();
        try {
            // Show the location of caller
            StackFrame callerFrame = threadReference.frame(threadReference.frameCount() > 1 ? 1 : 0);
            Location callerLocation = callerFrame.location();
            SourcePosition sourcePosition;
            if ("main".equals(event.method().name())) {
                sourcePosition = SourcePosition.unknownPosition();
            } else {
                sourcePosition = context.getSourceManager().nextPosition(callerFrame.location().declaringType().name());
            }
            println("METHOD_ENTRY: line: %d  \"thread=%s\", %s, %s", callerLocation.lineNumber(), event.thread().name(), sourcePosition.getSource(), sourcePosition.toString());
        } catch (IncompatibleThreadStateException e) {
            e.printStackTrace();
        }
        indent += 4;
    }

    public void handleMethodExitEvent(MethodExitEvent event) {
        indent -= 4;
        println("METHOD_EXIT: \"thread=%s\", return=%s", event.thread().name(), event.returnValue());
    }

    /**
     *
     * @param event
     */
    public void handleBreakpointEvent(BreakpointEvent event) {
//        Thread.yield();
//        println("breakpoint: " + event.location().lineNumber());
        try {
            StackFrame topFrame = event.thread().frame(ThreadConstants.TOP_FRAME);
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
                List<String> staticFieldString = staticFieldValues.entrySet().stream()
                        .map((entry) -> String.format(CommonConstants.KV_FORMAT, entry.getKey(), valueToString(entry.getValue())))
                        .collect(Collectors.toList());
                sb.append(String.join(", ", staticFieldString));

//                sb.append(" | ");
//
//                sb.append("attr: ");
//                List<String> attrFieldString = attributeValues.entrySet().stream()
//                        .map((entry) -> String.format(CommonConstants.KV_FORMAT, entry.getKey().name(), entry.getValue()))
//                        .collect(Collectors.toList());
//                sb.append(String.join(", ", attrFieldString));

                sb.append(" | ");

                sb.append("local: ");
                for (LocalVariable var : topFrame.visibleVariables()) {
                    kvString.add(String.format("%s = %s", var.name(), valueToString(topFrame.getValue(var))));
                    varValues.put(var, topFrame.getValue(var));
                }
                varValuesInMethod.put(topFrame.location().method(), varValues);
                sb.append(String.join(", ", kvString));

                println(sb.toString());
                staticOrAttrModified = false;
            }
        } catch (IncompatibleThreadStateException e) {
//            println("Exception: " + e);
            e.printStackTrace();
        } catch (AbsentInformationException e) {
//            println("Exception: " + e);
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
//            println("Exception: " + e);
            e.printStackTrace();

        }
    }

    public void handleModificationWatchpointEvent(ModificationWatchpointEvent event) {
        Field field = event.field();
        Value value = event.valueToBe();
//        println("FIELD_MODIFIED(static): line: %d, \"thread=%s\", %s %s = %s , %s", event.location().lineNumber(), event.thread().name(), field.typeName(), field.name(), value, OutputManager.locationToSimplifiedString(location));
        if (field.isStatic()) {
            staticFieldValues.put(field, value);
        } else {
            objectValues.computeIfPresent((ObjectReferenceImpl) event.object(), (key, oldValues) -> {
                oldValues.put(field, value);
                return oldValues;
            });
        }
        staticOrAttrModified = true;
    }

    public void handleThreadStartEvent(ThreadStartEvent event) {

    }

    public void handleThreadDeathEvent(ThreadDeathEvent event) {

    }


    private void println(String format, Object... args) {
        context.getOutputManager().print(indentString());
        context.getOutputManager().printf(format, args);
        context.getOutputManager().print("\n");
    }

    private String indentString() {
        char[] spaces = new char[indent];
        for (int i = 0; i < indent; i++) {
            spaces[i] = ' ';
        }
        return new String(spaces);
    }

    private String valueToString(Value value) {
        if (value instanceof ArrayReference) {
            return arrayValueToString((ArrayReferenceImpl) value);
        } else if (value instanceof StringReference) {
            return value.toString();
        } else if (value instanceof ObjectReference) {
            return objValueToString((ObjectReferenceImpl) value);
        }
        return value.toString();
    }

    private String arrayValueToString(ArrayReferenceImpl value) {
        List<String> arrayItemStrings = value.getValues().stream()
                .map(this::valueToString)
                .collect(Collectors.toList());
        return String.format("[%s]", String.join(", ", arrayItemStrings));
    }

    private String objValueToString(ObjectReferenceImpl value) {
        if (!objectValues.containsKey(value)) {
            List<Field> fields = value.referenceType().visibleFields();
            Map<Field, Value> fieldValueMap = value.getValues(fields);
            FieldValuePairs fieldValuePairs = new FieldValuePairs();
            fieldValuePairs.putAll(fieldValueMap);
            objectValues.put(value, fieldValuePairs);
            return fieldValuePairs.toString();
        } else {
            return objectValues.get(value).toString();
        }
    }
}
