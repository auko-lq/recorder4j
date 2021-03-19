package com.aukocharlie.recorder4j.launch;

import com.sun.jdi.VirtualMachine;
import lombok.Getter;

/**
 * @author auko
 * @date 2021/3/17 17:06
 */
@Getter
public class VMWrapper {

    private String mainClass;

    private String[] mainArgs;

    private VMOptions options;

    private VirtualMachine vm;

    public VMWrapper(String mainClass, String[] mainArgs, VMOptions options, VirtualMachine vm) {
        this.mainClass = mainClass;
        this.mainArgs = mainArgs;
        this.options = options;
        this.vm = vm;
    }

    public String getMainClass() {
        return this.mainClass;
    }

}
