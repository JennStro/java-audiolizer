import com.sun.jdi.Bootstrap;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.VirtualMachine;

import java.util.Map;

public class Debugger {

    private Class debugee;
    private int[] breakpointLines;

    public Class getDebugee() {
        return debugee;
    }

    public void setDebugee(Class debugee) {
        this.debugee = debugee;
    }

    public int[] getBreakpointLines() {
        return breakpointLines;
    }

    public void setBreakpointLines(int[] breakpointLines) {
        this.breakpointLines = breakpointLines;
    }

    /**
     * Create connector and give it the debuggee's main class, connect this debugger to the VM and launch the VM.
     *
     * @return Virtual Machine for debugging
     * @throws Exception
     */
    public VirtualMachine connectAndLaunchVirtualMachine() throws Exception {
        LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
        Map<String, Connector.Argument> connectorArguments = launchingConnector.defaultArguments();
        connectorArguments.get("main").setValue(debugee.getName());
        return launchingConnector.launch(connectorArguments);
    }
}
