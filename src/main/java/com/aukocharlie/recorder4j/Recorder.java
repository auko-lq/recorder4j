package com.aukocharlie.recorder4j;


import com.aukocharlie.recorder4j.exception.BadLaunchingConnectorException;
import com.aukocharlie.recorder4j.exception.InvalidRecorderOptionException;
import com.aukocharlie.recorder4j.exception.MissingLaunchingConnectorException;
import com.aukocharlie.recorder4j.launch.EventRegistrar;
import com.aukocharlie.recorder4j.launch.Launcher;
import com.aukocharlie.recorder4j.launch.Context;
import com.aukocharlie.recorder4j.result.EventHandler;
import com.aukocharlie.recorder4j.result.OutputManager;
import com.aukocharlie.recorder4j.target.TargetManager;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.VMStartException;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.regex.Pattern;

/**
 * @author auko
 * @date 2021/3/15 16:06
 */
public class Recorder {

    private TargetManager targetManager;
    private OutputManager outputManager;
    private Launcher launcher;

    private Recorder() {
    }

    private Recorder(Builder builder) {
        this.targetManager = builder.targetManager;
        this.outputManager = builder.outputManager;
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
            Context context = launcher.launch();
            context.addOutputManager(outputManager);
            Runtime.getRuntime().addShutdownHook(new Thread(context::shutdown));

            EventRegistrar registrar = new EventRegistrar(context, targetManager);
            EventHandler eventHandler = new EventHandler(registrar);
            eventHandler.start();
            eventHandler.join();
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
        private final TargetManager targetManager = new TargetManager();
        private final OutputManager outputManager = new OutputManager();
        private final Launcher launcher = new Launcher();

        /**
         * Scan the specified package to obtain annotation information
         *
         * @param targetPackage package name
         * @return
         */
        public Builder scanPackage(String targetPackage) {
            if (targetPackage == null) {
                throw new InvalidRecorderOptionException("`scanPackage` option is invalid: `targetPackage` can't not be null");
            }
            targetManager.scanPackage(new String[]{targetPackage});
            return this;
        }

        public Builder scanPackage(String[] targetPackages) {
            if (targetPackages == null) {
                throw new InvalidRecorderOptionException("`scanPackage` option is invalid: `targetPackages` can't not be null");
            }
            targetManager.scanPackage(targetPackages);
            return this;
        }

        public Builder addRecordClass(Class<?> clazz) {
            if (clazz != null) {
                targetManager.addRecordClass(clazz);
            }
            return this;
        }

        public Builder setRecordClasses(Class<?>[] classes) {
            if (classes != null) {
                targetManager.setRecordClasses(classes);
            }
            return this;
        }

        public Builder addRecordMethod(Method method) {
            if (method != null) {
                targetManager.addRecordMethod(method);
            }
            return this;
        }

        public Builder setRecordMethods(Method[] methods) {
            if (methods != null) {
                targetManager.setRecordMethods(methods);
            }
            return this;
        }

        public Builder addRecordField(Field field) {
            if (field != null) {
                targetManager.addRecordField(field);
            }
            return this;
        }

        public Builder setRecordFields(Field[] fields) {
            if (fields != null) {
                targetManager.setRecordFields(fields);
            }
            return this;
        }

        public Builder main(Class<?> mainClass) {
            if (mainClass == null) {
                throw new InvalidRecorderOptionException("`main` option is invalid: `mainClass` must not be null");
            }
            launcher.setMainClassName(mainClass.getName());
            return this;
        }

        public Builder mainArgs(String[] args) {
            if (args != null) {
                launcher.setMainArgs(args);
            }
            return this;
        }

        public Builder outPutReplace(String reg, String replacement) {
            if (reg == null) {
                throw new InvalidRecorderOptionException("`outPutReplace` option is invalid: `reg` can't be null");
            }
            if (replacement == null) {
                throw new InvalidRecorderOptionException("`outPutReplace` option is invalid: `replacement` can't be null");
            }
            outputManager.addReplacePattern(Pattern.compile(reg), replacement);
            return this;
        }

        public Recorder build() {
            return new Recorder(this);
        }
    }
}
