import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.*;
import com.sun.jdi.request.MethodEntryRequest;

import java.util.*;

public class Debugger {

    private Class debugee;
    private HashSet<String> classes = new HashSet<>();
    private HashMap<String, String> methodSounds = new HashMap<>();
    private ArrayList<String> methods = new ArrayList<>();

    public void setDebugee(Class debugee) {
        this.debugee = debugee;
    }

    public HashSet<String> getClasses() {
        return classes;
    }

    public void addClass(String className) {
        classes.add(className);
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

    public ArrayList<String> getMethods() {
        return this.methods;
    }

    public static void main(String[] args) {
        Debugger debugger = new Debugger();
        debugger.setDebugee(InstrumentMain.class);

        VirtualMachine virtualMachine = null;
        try {
            virtualMachine = debugger.connectAndLaunchVirtualMachine();
            debugger.listenToMethodEntryEvents(virtualMachine);
            virtualMachine.eventRequestManager().createExceptionRequest(null, true, true).enable();
            debugger.registerMethods(virtualMachine);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> methods = debugger.getMethods();
        for (String method : methods) {
            if (method.contains("main")) {
                AudioPlayer player = new AudioPlayer();
                player.playAndDelay("resources/Drums_main.aif", 2500L);
            } else if (method.contains("init")) {
                System.out.println(debugger.getClasses());
                AudioPlayer player = new AudioPlayer();
                player.playAndDelayThenStop("resources/ScreamLead_C2.aif", 500L, 3000L);
            } else {
                if (method.contains("World")) {
                    AudioPlayer player = new AudioPlayer();
                    player.playAndDelayThenStop("resources/ScreamLead_D1.aif", 500L, 3000L);
                } else {
                    AudioPlayer player = new AudioPlayer();
                    player.playAndDelayThenStop("resources/ScreamLead_A1.aif", 500L, 3000L);
                }
            }
        }
    }
}
