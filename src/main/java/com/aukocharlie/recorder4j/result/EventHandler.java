package com.aukocharlie.recorder4j.result;

import com.aukocharlie.recorder4j.launch.EventRegistrar;
import com.aukocharlie.recorder4j.launch.VMWrapper;
import com.aukocharlie.recorder4j.target.TargetManager;
import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.ClassPrepareRequest;

import java.util.Map;

/**
 * @author auko
 * @date 2021/3/16 22:43
 */
public class EventHandler extends Thread {

    private EventRegistrar registrar;
    private VMWrapper vmWrapper;
    private TargetManager targetManager;

    public EventHandler(EventRegistrar registrar) {
        this.registrar = registrar;
        this.vmWrapper = registrar.getVmWrapper();
        this.targetManager = registrar.getTargetManager();
    }

    public void run() {
        try {
            EventSet eventSet = null;
            while ((eventSet = vmWrapper.getVm().eventQueue().remove()) != null) {
//                System.out.println(eventSet);
                for (Event event : eventSet) {
                    if (event instanceof ClassPrepareEvent) {
                        System.out.println(((ClassPrepareEvent) event).referenceType());
//                        registrar.registerBreakpointRequest(new int[]{15}, (ClassType) ((ClassPrepareEvent) event).referenceType());
                    }
                    if (event instanceof BreakpointEvent) {
                        displayVariables((BreakpointEvent) event);
                    }
                }
                // Finally must to resume
                vmWrapper.getVm().resume();
            }
        } catch (InterruptedException | AbsentInformationException | IncompatibleThreadStateException e) {
            e.printStackTrace();
        } catch (VMDisconnectedException e) {
            System.out.println("Disconnected from the target VM.");
        }
    }

    public void displayVariables(LocatableEvent event) throws IncompatibleThreadStateException,
            AbsentInformationException {
        StackFrame stackFrame = event.thread().frame(0);
        if (stackFrame.location().toString().contains(vmWrapper.getMainClass())) {
            Map<LocalVariable, Value> visibleVariables = stackFrame.getValues(stackFrame.visibleVariables());
            System.out.println("=====  Variables at " + stackFrame.location().toString() + "  ===== ");
            for (Map.Entry<LocalVariable, Value> entry : visibleVariables.entrySet()) {
                System.out.printf("%s(%s) = %s%n", entry.getKey().name(), entry.getKey().typeName(), entry.getValue());
            }
            System.out.println("=================================\n");
        }
    }

}
