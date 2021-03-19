package com.aukocharlie.recorder4j.launch;

import com.aukocharlie.recorder4j.target.TargetManager;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.ClassType;
import com.sun.jdi.Location;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import lombok.Getter;

/**
 * @author auko
 * @date 2021/3/16 22:35
 */
public class EventRegistrar {

    @Getter
    private VMWrapper vmWrapper;
    @Getter
    private TargetManager targetManager;

    public EventRegistrar(VMWrapper vm, TargetManager targetManager) {
        this.vmWrapper = vm;
        this.targetManager = targetManager;
    }

    public void InitialRegister() {
        registerClassPrepareRequest(vmWrapper.getMainClass());
    }

    public void registerClassPrepareRequest(String className) {
        ClassPrepareRequest request = vmWrapper.getVm().eventRequestManager().createClassPrepareRequest();
//        request.addClassFilter(className);
        request.enable();
    }

    public void registerBreakpointRequest(int[] breakpointLines, ClassType classType) throws AbsentInformationException {
        for (int lineNumber : breakpointLines) {
            Location location = classType.locationsOfLine(lineNumber).get(0);
            BreakpointRequest bpReq = vmWrapper.getVm().eventRequestManager().createBreakpointRequest(location);
            bpReq.enable();
        }
    }

}
