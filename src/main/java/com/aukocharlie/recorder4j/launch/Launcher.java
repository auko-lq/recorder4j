package com.aukocharlie.recorder4j.launch;

import com.aukocharlie.recorder4j.exception.BadLaunchingConnectorException;
import com.aukocharlie.recorder4j.exception.MissingLaunchingConnectorException;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import lombok.Getter;
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


    public VMWrapper launch() throws MissingLaunchingConnectorException, VMStartException, IllegalConnectorArgumentsException, IOException, BadLaunchingConnectorException {
        LaunchingConnector launchingConnector = findLaunchingConnector();
        VirtualMachine vm = launchingConnector.launch(setArguments(launchingConnector));
        return new VMWrapper(mainClassName, mainArgs, options, vm);
    }

    private LaunchingConnector findLaunchingConnector() throws MissingLaunchingConnectorException {
        return Bootstrap.virtualMachineManager().defaultConnector();
//        List<Connector> connectors = Bootstrap.virtualMachineManager().allConnectors();
//        for (Connector connector : connectors) {
//            if (connector.name().equals(COMMAND_LINE_LAUNCH)) {
//                return (LaunchingConnector) connector;
//            }
//        }
//        throw new MissingLaunchingConnectorException("Can't find the specified launching connector: " + COMMAND_LINE_LAUNCH);
    }

    private Map<String, Connector.Argument> setArguments(LaunchingConnector connector) throws BadLaunchingConnectorException {
        Map<String, Connector.Argument> arguments = connector.defaultArguments();
        Optional.of(arguments.get("main"))
                .orElseThrow(() -> new BadLaunchingConnectorException("Can't get `main` argument from launching connector"))
                .setValue(String.format("%s %s", mainClassName, String.join(" ", mainArgs)));
        Optional.of(arguments.get("options"))
                .orElseThrow(() -> new BadLaunchingConnectorException("Can't get `options` argument from launching connector"))
                .setValue(options.toString());
        return arguments;
    }

    public void addVMOption(String option) {
        this.options.addOption(option);
    }

}
