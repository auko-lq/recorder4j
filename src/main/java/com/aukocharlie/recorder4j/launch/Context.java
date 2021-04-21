package com.aukocharlie.recorder4j.launch;

import com.aukocharlie.recorder4j.result.OutputManager;
import com.aukocharlie.recorder4j.source.SourceManager;
import com.aukocharlie.recorder4j.target.TargetManager;
import com.sun.jdi.VMDisconnectedException;
import com.sun.jdi.VirtualMachine;
import lombok.Getter;
import lombok.Setter;

/**
 * @author auko
 */
@Getter
public class Context {

    private String mainClass;

    private String[] mainArgs;

    private VMOptions options;

    private VirtualMachine vm;

    private Process process;

    private TargetManager targetManager;

    private OutputManager outputManager;

    private SourceManager sourceManager;

    @Setter
    private boolean connected = true;

    @Setter
    private boolean dead = false;

    public Context(String mainClass, String[] mainArgs, VMOptions options, VirtualMachine vm) {
        this.mainClass = mainClass;
        this.mainArgs = mainArgs;
        this.options = options;
        this.vm = vm;
        this.process = vm.process();
    }

    public String getMainClass() {
        return this.mainClass;
    }

    public void shutdown() {
        // Shutdown launched VM When the recorder's VM is terminated.
        try {
            if (this.vm != null) {
                this.vm.dispose();
                this.vm = null;
            }
        } catch (VMDisconnectedException e) {
            // Just ignore it.
        } finally {
            if (this.process != null) {
                this.process.destroy();
                this.process = null;
            }
        }
    }

    public void setOutputManager(OutputManager manager) {
        this.outputManager = manager;
    }

    public void setSourceManager(SourceManager sourceManager) {
        this.sourceManager = sourceManager;
    }

    public void setTargetManager(TargetManager targetmanager) {
        this.targetManager = targetmanager;
    }

}
