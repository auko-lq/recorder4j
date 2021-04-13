package com.aukocharlie.recorder4j.launch;

import com.aukocharlie.recorder4j.exception.BadLaunchingConnectorException;
import com.aukocharlie.recorder4j.exception.MissingLaunchingConnectorException;
import com.aukocharlie.recorder4j.exception.UnsupportVMOperationException;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import lombok.Setter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author auko
 * @date 2021/3/16 15:13
 */
public class Launcher {

    @Setter
    private String mainClassName = null;

    @Setter
    private String[] mainArgs = new String[]{};

    private final VMOptions options = new VMOptions();

    private static final String COMMAND_LINE_LAUNCH = "com.sun.jdi.CommandLineLaunch";


    public Context launch() throws VMStartException, IllegalConnectorArgumentsException, IOException, BadLaunchingConnectorException {
        LaunchingConnector launchingConnector = findLaunchingConnector();
        VirtualMachine vm = launchingConnector.launch(setArguments(launchingConnector));

        vm.setDebugTraceMode(VirtualMachine.TRACE_NONE);
        checkVMOperations(vm);

        Context context = new Context(mainClassName, mainArgs, options, vm);
        context.setConnected(true);
        context.setDead(false);
        return context;
    }

    private LaunchingConnector findLaunchingConnector() {
        List<Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();
        for (Connector connector : connectors) {
            if (connector.name().equals(COMMAND_LINE_LAUNCH)) {
                return (LaunchingConnector) connector;
            }
        }
        throw new MissingLaunchingConnectorException("Can't find the specified launching connector: " + COMMAND_LINE_LAUNCH);
    }

    private Map<String, Connector.Argument> setArguments(LaunchingConnector connector) throws BadLaunchingConnectorException {
        Map<String, Connector.Argument> arguments = connector.defaultArguments();
        Optional.ofNullable(arguments.get("main"))
                .orElseThrow(() -> new BadLaunchingConnectorException("Can't get `main` argument from launching connector"))
                .setValue(String.format("%s %s", mainClassName, String.join(" ", mainArgs)));
        Optional.ofNullable(arguments.get("options"))
                .orElseThrow(() -> new BadLaunchingConnectorException("Can't get `options` argument from launching connector"))
                .setValue(options.toString());
        Optional.ofNullable(arguments.get("quote"))
                .orElseThrow(() -> new BadLaunchingConnectorException("Can't get `quote` argument from launching connector"))
                .setValue("\"");
        return arguments;
    }

    /**
     * Notes: quotes must be double quotes
     */
    public void addVMOption(String option) {
        this.options.addOption(option);
    }

    private void checkVMOperations(VirtualMachine vm) {
        doAssert(vm.canGetInstanceInfo(), "Virtual machine does not support getting instance info");
        doAssert(vm.canGetMethodReturnValues(), "Virtual machine does not support getting method return values");
        doAssert(vm.canWatchFieldAccess(), "Virtual machine does not support watching field access");
        doAssert(vm.canWatchFieldModification(), "Virtual machine does not support watching field modification");
    }

    private void doAssert(boolean condition, String message) {
        if (!condition) {
            throw new UnsupportVMOperationException(message);
        }
    }

}
