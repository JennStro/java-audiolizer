import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.*;
import com.sun.jdi.request.MethodEntryRequest;

import java.util.Map;

public class Debugger {

    private Class debugee;

    public void setDebugee(Class debugee) {
        this.debugee = debugee;
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
     * Enable methodEntryRequest to get notified when a method is entried  in the debugee class.
     *
     * @see <a href="https://docs.oracle.com/javase/8/docs/jdk/api/jpda/jdi/com/sun/jdi/event/MethodEntryEvent.html">MethodEntryEvent</a>
     *
     * @param virtualMachine
     */
    public void listenToMethodEntryEvents(VirtualMachine virtualMachine) {
        MethodEntryRequest methodEntryRequest = virtualMachine.eventRequestManager().createMethodEntryRequest();
        methodEntryRequest.addClassFilter("instrument*");
        methodEntryRequest.enable();
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
        if(player.isPlaying()) {
            try {
                Thread.sleep(player.timeLeft());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            player.stop();
        }
    }

    public static void main(String[] args) {
        Debugger debugger = new Debugger();
        debugger.setDebugee(instrumentMain.class);

        AudioPlayer player = new AudioPlayer();

        try {
            VirtualMachine virtualMachine = debugger.connectAndLaunchVirtualMachine();
            debugger.listenToMethodEntryEvents(virtualMachine);

            EventSet events;
            while ((events = virtualMachine.eventQueue().remove()) != null) {
                for (Event event : events) {
                    if (event instanceof MethodEntryEvent) {
                        Method enteredMethod = ((MethodEntryEvent) event).method();
                        System.out.println("METHOD: "+enteredMethod.toString());

                        if ("instrumentMain.main(java.lang.String[])".equals(enteredMethod.toString())) {
                            player.play("main.aif", 1500L);
                        } else {
                            player.play("Piano_C3.aif", 1000L);
                        }
                    }
                    virtualMachine.resume();
                }
            }
            debugger.waitForAudioPlayerToFinish(player);
        } catch (VMDisconnectedException e) {
            System.out.println("Virtual Machine is disconnected.");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
