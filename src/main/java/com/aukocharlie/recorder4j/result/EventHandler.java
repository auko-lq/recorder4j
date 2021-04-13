package com.aukocharlie.recorder4j.result;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.aukocharlie.recorder4j.exception.UnknownEventException;
import com.aukocharlie.recorder4j.launch.EventRegistrar;
import com.aukocharlie.recorder4j.launch.Context;
import com.aukocharlie.recorder4j.target.TargetManager;
import com.sun.jdi.*;
import com.sun.jdi.event.*;

import java.util.List;
import java.util.Map;

/**
 * @author auko
 * @date 2021/3/16 22:43
 */
public class EventHandler extends Thread {

    private EventRegistrar registrar;
    private Context context;
    private TargetManager targetManager;

    public EventHandler(EventRegistrar registrar) {
        super("event handler");
        this.registrar = registrar;
        this.context = registrar.getContext();
        this.targetManager = registrar.getTargetManager();
    }

    public void run() {
        try {
            EventSet eventSet = null;
            EventQueue queue = context.getVm().eventQueue();
            while (context.isConnected() && (eventSet = queue.remove()) != null) {
                for (Event event : eventSet) {
                    handleEvent(event);
                }
                // Finally must to resume
                context.getVm().resume();
            }
        } catch (InterruptedException | AbsentInformationException e) {
            e.printStackTrace();
        } catch (VMDisconnectedException e) {
//            context.setConnected(false);
            System.out.println(" ==== VM disconnected ====");
        }
    }

    private void handleEvent(Event event) throws AbsentInformationException {
        if (event instanceof ClassPrepareEvent) {
            handleClassPrepareEvent((ClassPrepareEvent) event);
        } else if (event instanceof ExceptionEvent) {
            handleExceptionEvent((ExceptionEvent) event);
        } else if (event instanceof MethodEntryEvent) {
            handleMethodEntryEvent((MethodEntryEvent) event);
        } else if (event instanceof MethodExitEvent) {
            handleMethodExitEvent((MethodExitEvent) event);
        } else if (event instanceof BreakpointEvent) {
            handleBreakpointEvent((BreakpointEvent) event);
        } else if (event instanceof ModificationWatchpointEvent) {
            handleModificationWatchpointEvent((ModificationWatchpointEvent) event);
        } else if (event instanceof WatchpointEvent) {
            handleWatchpointEvent((WatchpointEvent) event);
        } else if (event instanceof ThreadStartEvent) {
            handleThreadStartEvent((ThreadStartEvent) event);
        } else if (event instanceof ThreadDeathEvent) {
            handleThreadDeathEvent((ThreadDeathEvent) event);
        } else if (event instanceof StepEvent) {
            handleStepEvent((StepEvent) event);
        } else if (event instanceof VMStartEvent) {
            handleVMStartEvent((VMStartEvent) event);
        } else if (event instanceof VMDeathEvent) {
            handleVMDeathEvent((VMDeathEvent) event);
        } else if (event instanceof VMDisconnectEvent) {
            handleVMDisconnectEvent((VMDisconnectEvent) event);
        } else {
            throw new UnknownEventException(event);
        }
    }

    public void handleClassPrepareEvent(ClassPrepareEvent event) {
        if (event.referenceType().classLoader() != null) {
//            System.out.println(event.referenceType());
            registrar.enableModificationWatchpointEvent(event.referenceType().allFields());
            try {
                List<String> sourcePaths = event.referenceType().sourcePaths("");
                if (sourcePaths.size() > 0) {
                    String srcRelativePath = sourcePaths.get(0);
                    context.getSourceManager().parseSourceCode(srcRelativePath);
                    for (Location location : event.referenceType().allLineLocations()) {
                        registrar.enableBreakpointRequest(location);
                    }
                } else {
                    throw new RecorderRuntimeException("Can't get sourcePath of class: " + event.referenceType().toString());
                }
            } catch (AbsentInformationException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleWatchpointEvent(WatchpointEvent event) {
        System.out.println(event.field());
    }

    public void handleModificationWatchpointEvent(ModificationWatchpointEvent event) {
        ThreadTrace.currentThread(event.thread(), registrar, context).handleModificationWatchpointEvent(event);
    }

    public void handleExceptionEvent(ExceptionEvent event) {
        if (!ExceptionFilter.skip(event)) {
            if (event.catchLocation() == null) {
                System.out.printf("Exception occurred: %s (uncaught)%n", event.exception().referenceType().name());
            } else {
                System.out.printf("Exception occurred: %s (to be caught at: %s)%n", event.exception().referenceType().name(), FormatUtils.locationToString(event.catchLocation()));
            }
        }
    }

    public void handleMethodEntryEvent(MethodEntryEvent event) {
        ThreadTrace.currentThread(event.thread(), registrar, context).handleMethodEntryEvent(event);
    }

    public void handleMethodExitEvent(MethodExitEvent event) {
        ThreadTrace.currentThread(event.thread(), registrar, context).handleMethodExitEvent(event);
    }

    public void handleBreakpointEvent(BreakpointEvent event) {
        ThreadTrace.currentThread(event.thread(), registrar, context).handleBreakpointEvent(event);
    }

    public void handleThreadStartEvent(ThreadStartEvent event) {
        ThreadTrace.currentThread(event.thread(), registrar, context).handleThreadStartEvent(event);
    }

    public void handleThreadDeathEvent(ThreadDeathEvent event) {
        ThreadTrace.currentThread(event.thread(), registrar, context).handleThreadDeathEvent(event);
    }

    public void handleStepEvent(StepEvent event) {
        // Step to exception catch
        System.out.println(event);
//        context.getVm().eventRequestManager().deleteEventRequest(event.request());
    }

    public void handleVMStartEvent(VMStartEvent event) {
        System.out.println(" ==== VM started ==== ");
    }

    public void handleVMDeathEvent(VMDeathEvent event) {
        context.setDead(true);
        System.out.println(" ==== VM terminated ==== ");
    }

    public void handleVMDisconnectEvent(VMDisconnectEvent event) {
        context.setConnected(false);
        System.out.println(" ==== VM disconnected ====");
    }

    public void displayVariables(LocatableEvent event) throws IncompatibleThreadStateException,
            AbsentInformationException {
        StackFrame stackFrame = event.thread().frame(0);
        if (stackFrame.location().toString().contains(context.getMainClass())) {
            Map<LocalVariable, Value> visibleVariables = stackFrame.getValues(stackFrame.visibleVariables());
            System.out.println("=====  Variables at " + stackFrame.location().toString() + "  ===== ");
            for (Map.Entry<LocalVariable, Value> entry : visibleVariables.entrySet()) {
                System.out.printf("%s(%s) = %s%n", entry.getKey().name(), entry.getKey().typeName(), entry.getValue());
            }
            System.out.println("=================================\n");
        }
    }

}
