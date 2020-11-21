import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.*;
import com.sun.jdi.request.MethodEntryRequest;

import java.util.*;

public class Debugger {

    private Class debugee;
    private HashMap<String, String> methodSounds;
    private ArrayList<String> methods;
    // Not actual scream, this is just what the instrument was called in GarageBand :)
    private ArrayList<String> screamNotes;

    public Debugger() {
        this.methods = new ArrayList<>();
        this.methodSounds = new HashMap<>();
        this.screamNotes = new ArrayList<>(List.of(
               "ScreamLead_C1.aif",
                "ScreamLead_D1.aif",
                "ScreamLead_E1.aif",
                "ScreamLead_F1.aif",
                "ScreamLead_G1.aif",
                "ScreamLead_A1.aif",
                "ScreamLead_H1.aif",
                "ScreamLead_C2.aif"));
    }

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
        connectorArguments.get("options").setValue("-cp " + System.getProperty("java.class.path"));
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
        methodEntryRequest.addClassExclusionFilter("java.*");
        methodEntryRequest.addClassExclusionFilter("jdk.*");
        methodEntryRequest.addClassExclusionFilter("sun.*");
        methodEntryRequest.enable();
    }

    public void registerMethods(VirtualMachine virtualMachine) {
        EventSet events;
    try {
      while ((events = virtualMachine.eventQueue().remove()) != null) {
        for (Event event : events) {
          if (event instanceof ExceptionEvent) {
            System.out.println(event.toString());
          }

          if (event instanceof MethodEntryEvent) {
            Method enteredMethod = ((MethodEntryEvent) event).method();
            this.methods.add(enteredMethod.name());
          }
          virtualMachine.resume();
        }
      }
        }
        catch (VMDisconnectedException e) {
                System.out.println("Virtual Machine is disconnected.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getMethodSounds() {
        return this.methodSounds;
    }

    public ArrayList<String> getMethods() {
        return this.methods;
    }

   public void assignNotesToMethods() {
        int sound = 0;
        for (String method : getMethods()) {
            if (method.contains("main")) {
                methodSounds.put(method, "resources/Drums_main.aif");
            } else {
                methodSounds.put(method, "resources/"+screamNotes.get(sound));
            }
            sound = ((sound+1) % screamNotes.size());
        }
    }

    public static void main(String[] args) {
        Debugger debugger = new Debugger();
        debugger.setDebugee(InstrumentMain.class);

        VirtualMachine virtualMachine;
        try {
            virtualMachine = debugger.connectAndLaunchVirtualMachine();
            debugger.listenToMethodEntryEvents(virtualMachine);
            virtualMachine.eventRequestManager().createExceptionRequest(null, true, true).enable();
            debugger.registerMethods(virtualMachine);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> methods = debugger.getMethods();
        debugger.assignNotesToMethods();

        for (String method : methods) {
            System.out.println(method);
            if (method.contains("main")) {
                AudioPlayer player = new AudioPlayer();
                player.playAndDelay(debugger.getMethodSounds().get(method), 2500L);
            } else {
                AudioPlayer player = new AudioPlayer();
                player.playAndDelayThenStop(debugger.getMethodSounds().get(method), 2500L, 3000L);
            }
        }
    }
}
