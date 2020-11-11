import com.sun.jdi.Bootstrap;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.request.ClassPrepareRequest;

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

    /**
     * Enable classPrepareRequest in order to get notified when a class is prepared and gives a classPrepareEvent.
     *
     * A class is prepared when the static fields for a class is created.
     * @see <a href="https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-5.html#jvms-5.4.2">JVM specifications</a>
     *
     * @param virtualMachine
     */
    public void enableClassPrepareRequest(VirtualMachine virtualMachine) {
        ClassPrepareRequest classPrepareRequest = virtualMachine.eventRequestManager().createClassPrepareRequest();
        classPrepareRequest.addClassFilter(debugee.getName());
        classPrepareRequest.enable();
    }

    public static void main(String[] args) {
        Debugger debugger = new Debugger();
        debugger.setDebugee(ExampleProgram.class);
        int[] breakpoints = {5, 6};
        debugger.setBreakpointLines(breakpoints);
        try {
            VirtualMachine vm = debugger.connectAndLaunchVirtualMachine();
            vm.resume();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
