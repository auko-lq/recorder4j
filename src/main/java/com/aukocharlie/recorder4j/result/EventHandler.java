package com.aukocharlie.recorder4j.result;

import com.aukocharlie.recorder4j.exception.RecorderRuntimeException;
import com.aukocharlie.recorder4j.exception.UnknownEventException;
import com.aukocharlie.recorder4j.launch.EventRegistrar;
import com.aukocharlie.recorder4j.launch.Context;
import com.sun.jdi.*;
import com.sun.jdi.event.*;

import java.util.List;

import static com.aukocharlie.recorder4j.util.LambdaUtils.isLambda;
import static com.aukocharlie.recorder4j.util.LambdaUtils.truncateLambdaClassName;

/**
 * @author auko
 */
public class EventHandler extends Thread {

    private EventRegistrar registrar;
    private Context context;


    public EventHandler(EventRegistrar registrar) {
        super("event handler");
        this.registrar = registrar;
        this.context = registrar.getContext();
    }

    public void run() {
        try {
            EventSet eventSet;
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
            context.setConnected(false);
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
            registrar.enableModificationWatchpointEvent(event.referenceType().allFields());
            try {
                if (isLambda(event)) {
                    String className = truncateLambdaClassName(event.referenceType().name());
                    context.getSourceManager().parseSourceCodeByClassName(className);
                } else {
                    List<String> sourcePaths = event.referenceType().sourcePaths("");
                    if (sourcePaths.size() > 0) {
                        context.getSourceManager().parseSourceCodeByPath(sourcePaths.get(0));
                        for (Location location : event.referenceType().allLineLocations()) {
                            registrar.enableBreakpointRequest(location);
                        }
                    } else {
                        throw new RecorderRuntimeException("Can't get sourcePath of class: " + event.referenceType().toString());
                    }
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
            Value detailMessage = event.exception().getValue(event.exception().referenceType().fieldByName("detailMessage"));
            if (event.catchLocation() == null) {
                System.out.printf("Exception occurred: %s(uncaught), detailMessage: %s %n",
                        event.exception().referenceType().name(), detailMessage);
            } else {
                System.out.printf("Exception occurred: %s(to be caught at: %s), detailMessage: %s %n",
                        event.exception().referenceType().name(), FormatUtils.locationToString(event.catchLocation()), detailMessage);
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


}
