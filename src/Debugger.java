import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.MethodEntryRequest;

import javax.sound.sampled.AudioSystem;
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
    public void listenToClassPrepareEvents(VirtualMachine virtualMachine) {
        ClassPrepareRequest classPrepareRequest = virtualMachine.eventRequestManager().createClassPrepareRequest();
        classPrepareRequest.addClassFilter(debugee.getName());
        classPrepareRequest.enable();
    }

    /**
     * Enable methodEntryRequest to get notified when a method is entried  in the debugee class.
     *
     * @see <a href="https://docs.oracle.com/javase/8/docs/jdk/api/jpda/jdi/com/sun/jdi/event/MethodEntryEvent.html">MethodEntryEvent</a>
     *
     * @param virtualMachine
     */
    public void listenToMethodEntryEvents(VirtualMachine virtualMachine) {
        MethodEntryRequest methodEntryRequest = virtualMachine.eventRequestManager().createMethodEntryRequest();
        methodEntryRequest.addClassFilter(debugee.getName());
        methodEntryRequest.enable();
    }

    public void setBreakPoints(VirtualMachine virtualMachine, ClassPrepareEvent event) throws AbsentInformationException {
        ClassType classType = (ClassType) event.referenceType();
        for(int lineNumber : breakpointLines) {
            Location location = classType.locationsOfLine(lineNumber).get(0);
            BreakpointRequest breakpointRequest = virtualMachine.eventRequestManager().createBreakpointRequest(location);
            breakpointRequest.enable();
        }
    }

    /**
     * Get the current stackframe. If the debugee class is in the current instruction of this frame, then get its
     * visible variables and print them.
     *
     * @param event
     * @throws IncompatibleThreadStateException
     * @throws AbsentInformationException
     */
    public void printVisibleVariables(LocatableEvent event) throws IncompatibleThreadStateException, AbsentInformationException {
        StackFrame stackFrame = event.thread().frame(0);
        if(stackFrame.location().toString().contains(debugee.getName())) {
            Map<LocalVariable, Value> visibleVariables = stackFrame.getValues(stackFrame.visibleVariables());
            System.out.println("Variables at " + stackFrame.location().toString() +  " > ");
            for (Map.Entry<LocalVariable, Value> entry : visibleVariables.entrySet()) {
                System.out.println(entry.getKey().name() + " = " + entry.getValue());
            }
        }
    }

    /**
     * Wait 1 ms before checking if player is playing the clip, because it takes some time before the
     * clip is actually activated. Wait until the player has finished.
     *
     * @param player
     */
    public void waitForAudioPlayerToFinish(AudioPlayer player) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(player.isPlaying()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Debugger debugger = new Debugger();
        debugger.setDebugee(ExampleProgram.class);

        AudioPlayer player = new AudioPlayer();

        try {
            VirtualMachine virtualMachine = debugger.connectAndLaunchVirtualMachine();
            debugger.listenToMethodEntryEvents(virtualMachine);

            EventSet events;
            while ((events = virtualMachine.eventQueue().remove()) != null) {
                for (Event event : events) {
                    if (event instanceof MethodEntryEvent) {
                        Method enteredMethod = ((MethodEntryEvent) event).method();
                        System.out.println("A method has been entered!!!");
                        System.out.println(enteredMethod.toString());
                        player.play("Piano_C3.aif");
                        debugger.waitForAudioPlayerToFinish(player);
                    }
                    virtualMachine.resume();
                }
            }
        } catch (VMDisconnectedException e) {
            System.out.println("Virtual Machine is disconnected.");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
