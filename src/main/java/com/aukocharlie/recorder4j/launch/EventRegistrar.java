package com.aukocharlie.recorder4j.launch;

import com.aukocharlie.recorder4j.target.TargetManager;
import com.sun.jdi.*;
import com.sun.jdi.request.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author auko
 * @date 2021/3/16 22:35
 */
public class EventRegistrar {

    @Getter
    private Context context;
    @Getter
    private TargetManager targetManager;

    private Set<Field> enabledFields = new HashSet<>();

    private Set<ThreadReference> stepEnabledThread = new HashSet<>();

    public EventRegistrar(Context vm, TargetManager targetManager) {
        this.context = vm;
        this.targetManager = targetManager;
        InitialRegister();
    }

    public void InitialRegister() {
        enableClassPrepareRequest();
        enableExceptionRequest();
        enableMethodEntryRequest();
        enableMethodExitRequest();
        enableThreadStartRequest();
        enableThreadDeathRequest();
    }

    public void enableClassPrepareRequest() {
        ClassPrepareRequest request = context.getVm().eventRequestManager().createClassPrepareRequest();
        for (String exclusion : targetManager.getExcludeClasses()) {
            request.addClassExclusionFilter(exclusion);
        }
        request.enable();
    }

    public void enableExceptionRequest() {
        // want all exceptions
        ExceptionRequest request = context.getVm().eventRequestManager().createExceptionRequest(null, true, true);
        // suspend so we can step
        request.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        request.enable();
    }

    public void enableMethodEntryRequest() {
        MethodEntryRequest request = context.getVm().eventRequestManager().createMethodEntryRequest();
        for (String exclusion : targetManager.getExcludeClasses()) {
            request.addClassExclusionFilter(exclusion);
        }
        request.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        request.enable();
    }

    public void enableMethodExitRequest() {
        MethodExitRequest request = context.getVm().eventRequestManager().createMethodExitRequest();
        for (String exclusion : targetManager.getExcludeClasses()) {
            request.addClassExclusionFilter(exclusion);
        }
        request.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        request.enable();
    }

    public void enableThreadStartRequest() {
        ThreadStartRequest request = context.getVm().eventRequestManager().createThreadStartRequest();
        // Make sure we sync on thread start
        request.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        request.enable();
    }

    public void enableThreadDeathRequest() {
        ThreadDeathRequest request = context.getVm().eventRequestManager().createThreadDeathRequest();
        // Make sure we sync on thread death
        request.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        request.enable();
    }

    public void enableModificationWatchpointEvent(List<Field> fields) {
        EventRequestManager manager = context.getVm().eventRequestManager();
        for (Field field : fields) {
            if (enabledFields.contains(field)) {
                continue;
            }
            ModificationWatchpointRequest request = manager.createModificationWatchpointRequest(field);
            for (String exclusion : targetManager.getExcludeClasses()) {
                request.addClassExclusionFilter(exclusion);
            }
            // Must be SUSPEND_ALL, otherwise the field's value will be messed up
            request.setSuspendPolicy(EventRequest.SUSPEND_ALL);
            request.enable();
            enabledFields.add(field);
        }
    }

    public void enableStepRequestForCatchingException(ThreadReference thread) {
        StepRequest req = context.getVm().eventRequestManager().createStepRequest(thread,
                StepRequest.STEP_MIN,
                StepRequest.STEP_INTO);
        // next step only
        req.addCountFilter(1);
        req.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        req.enable();
    }

    public synchronized void enableStepRequestForWatchpoint(ThreadReference thread) {
        if (!stepEnabledThread.contains(thread)) {
            StepRequest req = context.getVm().eventRequestManager().createStepRequest(thread,
                    StepRequest.STEP_MIN,
                    StepRequest.STEP_INTO);
            // next step only
            req.addCountFilter(1);
            req.setSuspendPolicy(EventRequest.SUSPEND_ALL);
            req.enable();
            stepEnabledThread.add(thread);
        }
    }

    public void enableBreakpointRequest(Location location) {
        BreakpointRequest request = context.getVm().eventRequestManager().createBreakpointRequest(location);
        request.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        request.enable();
    }

    public void enableBreakpointRequest(int[] breakpointLines, ClassType classType) throws AbsentInformationException {
        for (int lineNumber : breakpointLines) {
            Location location = classType.locationsOfLine(lineNumber).get(0);
            BreakpointRequest bpReq = context.getVm().eventRequestManager().createBreakpointRequest(location);
            bpReq.enable();
        }
    }

}
