package com.aukocharlie.recorder4j;


import com.aukocharlie.recorder4j.constant.CommonConstants;
import com.aukocharlie.recorder4j.exception.BadLaunchingConnectorException;
import com.aukocharlie.recorder4j.exception.InvalidRecorderArgumentException;
import com.aukocharlie.recorder4j.launch.EventRegistrar;
import com.aukocharlie.recorder4j.launch.Launcher;
import com.aukocharlie.recorder4j.launch.Context;
import com.aukocharlie.recorder4j.result.EventHandler;
import com.aukocharlie.recorder4j.result.OutputManager;
import com.aukocharlie.recorder4j.source.SourceManager;
import com.aukocharlie.recorder4j.target.TargetManager;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.VMStartException;

import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @author auko
 */
public class Recorder {

    private TargetManager targetManager;
    private OutputManager outputManager;
    private SourceManager sourceManager;
    private Launcher launcher;

    private Recorder(Builder builder) {
        this.targetManager = builder.targetManager;
        this.outputManager = builder.outputManager;
        this.sourceManager = builder.sourceManager;
        this.launcher = builder.launcher;
        this.targetManager.checkTarget();
    }

    public static Builder builder() {
        return new Builder();
    }

    public void run() {
        try {
            Context context = launcher.launch();
            context.setOutputManager(outputManager);
            context.setSourceManager(sourceManager);
            context.setTargetManager(targetManager);
            Runtime.getRuntime().addShutdownHook(new Thread(context::shutdown));

            EventRegistrar registrar = new EventRegistrar(context);
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
        private final SourceManager sourceManager = new SourceManager();
        private final Launcher launcher = new Launcher();

        /**
         * Scan the specified package to obtain annotation information
         *
         * @param targetPackage package name
         */
        public Builder scanPackage(String targetPackage) {
            if (targetPackage == null) {
                throw new InvalidRecorderArgumentException("`scanPackage` argument is invalid: `targetPackage` can't not be null");
            }
            targetManager.scanPackage(new String[]{targetPackage});
            return this;
        }

        public Builder scanPackage(String[] targetPackages) {
            if (targetPackages == null) {
                throw new InvalidRecorderArgumentException("`scanPackage` argument is invalid: `targetPackages` can't not be null");
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
                throw new InvalidRecorderArgumentException("`main` argument is invalid: `mainClass` must not be null");
            }
            URL location = mainClass.getProtectionDomain().getCodeSource().getLocation();
            launcher.addVMOption(String.format("-cp \"%s\"", location.getFile()));
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
                throw new InvalidRecorderArgumentException("`outPutReplace` argument is invalid: `reg` can't be null");
            }
            if (replacement == null) {
                throw new InvalidRecorderArgumentException("`outPutReplace` argument is invalid: `replacement` can't be null");
            }
            outputManager.addReplacePattern(Pattern.compile(reg), replacement);
            return this;
        }

        public Builder srcRelativeRootPath(String relativePath) {
            if (relativePath == null) {
                throw new InvalidRecorderArgumentException("`srcRelativeRootPath` argument is invalid: `relativePath` can't be null");
            }
            sourceManager.setSrcRoot(CommonConstants.WORKING_DIR + relativePath);
            return this;
        }

        public Builder srcAbsoluteRootPath(String path) {
            if (path == null) {
                throw new InvalidRecorderArgumentException("`srcAbsoluteRootPath` argument is invalid: `path` can't be null");
            }
            sourceManager.setSrcRoot(path);
            return this;
        }

        /**
         * If `display` is true, then the detailed position of the method invocation
         * in the source code will be displayed when the method is called,
         * such as 18:8 -> 18:14, which means that the position is in the 8th to 14th columns of the 18th row
         */
        public Builder displayMethodPosition(boolean display) {
            // TODO: This part requires a deeper analysis of the source code
            if (display) {
                throw new InvalidRecorderArgumentException("This part has not been completed yet");
            }
            outputManager.setDisplayMethodPosition(display);
            return this;
        }

        public Recorder build() {
            return new Recorder(this);
        }
    }
}
