package com.aukocharlie.recorder4j;


import com.aukocharlie.recorder4j.exception.BadLaunchingConnectorException;
import com.aukocharlie.recorder4j.exception.MissingLaunchingConnectorException;
import com.aukocharlie.recorder4j.launch.EventRegistrar;
import com.aukocharlie.recorder4j.launch.Launcher;
import com.aukocharlie.recorder4j.launch.VMWrapper;
import com.aukocharlie.recorder4j.result.EventHandler;
import com.aukocharlie.recorder4j.result.StreamRedirectManager;
import com.aukocharlie.recorder4j.target.TargetManager;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.request.ClassPrepareRequest;

import java.io.IOException;
import java.lang.reflect.*;

/**
 * @author auko
 * @date 2021/3/15 16:06
 */
public class Recorder {

    private TargetManager targetManager;
    private StreamRedirectManager streamRedirectManager;
    private Launcher launcher;

    private Recorder() {
    }

    private Recorder(Builder builder) {
        this.targetManager = builder.targetManager;
        this.streamRedirectManager = builder.streamRedirectManager;
        this.launcher = builder.launcher;
        this.targetManager.checkTarget();
    }

    public static Builder builder() {
        return new Builder();
    }

    public void run() {
        if (targetManager.shouldRecordFields()) {
//            launcher.addVMOption("-classic");
        }

        try {
            VMWrapper vm = launcher.launch();
            EventRegistrar registrar = new EventRegistrar(vm, targetManager);
            registrar.InitialRegister();
            ClassPrepareRequest request = vm.getVm().eventRequestManager().createClassPrepareRequest();
            request.addClassFilter(vm.getMainClass());
            request.enable();
            EventHandler eventHandler = new EventHandler(registrar);
            eventHandler.start();
            eventHandler.join();
        } catch (MissingLaunchingConnectorException e) {
            e.printStackTrace();
        } catch (VMStartException e) {
            e.printStackTrace();
        } catch (IllegalConnectorArgumentsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadLaunchingConnectorException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected static class Builder {
        TargetManager targetManager = new TargetManager();
        StreamRedirectManager streamRedirectManager = new StreamRedirectManager();
        Launcher launcher = new Launcher();

        /**
         * Scan the specified package to obtain annotation information
         *
         * @param target package name
         * @return
         */
        public Builder scanPackage(String target) {
            targetManager.scanPackage(new String[]{target});
            return this;
        }

        public Builder scanPackage(String[] targets) {
            targetManager.scanPackage(targets);
            return this;
        }

        public Builder addRecordClass(Class<?> clazz) {
            targetManager.addRecordClass(clazz);
            return this;
        }

        public Builder setRecordClasses(Class<?>[] classes) {
            targetManager.setRecordClasses(classes);
            return this;
        }

        public Builder addRecordMethod(Method method) {
            targetManager.addRecordMethod(method);
            return this;
        }

        public Builder setRecordMethods(Method[] methods) {
            targetManager.setRecordMethods(methods);
            return this;
        }

        public Builder addRecordField(Field field) {
            targetManager.addRecordField(field);
            return this;
        }

        public Builder setRecordFields(Field[] fields) {
            targetManager.setRecordFields(fields);
            return this;
        }

        public Builder setToConsole(boolean toConsole) {
            streamRedirectManager.setToConsole(toConsole);
            return this;
        }

        public Builder main(Class<?> mainClass) {
            launcher.setMainClassName(mainClass.getName());
            return this;
        }

        public Builder transferArgs(String[] args) {
            launcher.setMainArgs(args);
            return this;
        }

        public Recorder build() {
            return new Recorder(this);
        }
    }
}
