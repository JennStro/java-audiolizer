import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.*;
import com.sun.jdi.request.MethodEntryRequest;

import java.util.*;

public class Debugger {

    private ArrayList<String> aNote;
    private ArrayList<String> cNote;
    private Class debugee;
    private HashMap<String, String> methodSounds;
    private ArrayList<String> methodsInExcecutionOrder;
    // Not actual scream, this is just what the instrument was called in GarageBand :)
    private ArrayList<String> screamNotes;
    private HashMap<String, ArrayList<String>> classes;

    public Debugger(Instruments intruments) {
        this.classes = new HashMap<>();
        this.methodSounds = new HashMap<>();
        this.methodsInExcecutionOrder = new ArrayList<>();
        this.cNote = new ArrayList<>(List.of("ScreamLead_C1.aif"));
        this.aNote = new ArrayList<>(List.of("ScreamLead_A1.aif"));
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

    /**
     *
     * @param method
     * @return the class that is calling this method
     */
    private String callingClass(Method method) {
        return method.toString().split("[.]")[0];
    }

    /**
     * Extract the class name that this method is called by and store both
     * class name and method name.
     * @param method
     */
    private void addMethod(Method method) {
        String callingClass = callingClass(method);
        String methodName = method.name();

        if (!classes.containsKey(callingClass)) {
            ArrayList<String> classMethods = new ArrayList<>();
            classMethods.add(methodName);
            classes.put(callingClass, classMethods);
        } else {
            classes.get(callingClass).add(methodName);
        }
    }

    public HashMap<String, ArrayList<String>> getClasses() {
        return this.classes;
    }

    public void registerClassesAndMethods(VirtualMachine virtualMachine) {
        EventSet events;
    try {
      while ((events = virtualMachine.eventQueue().remove()) != null) {
        for (Event event : events) {
          if (event instanceof ExceptionEvent) {
            System.out.println(event.toString());
          }

          if (event instanceof MethodEntryEvent) {
            Method enteredMethod = ((MethodEntryEvent) event).method();
            methodsInExcecutionOrder.add(enteredMethod.name());
            addMethod(enteredMethod);
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


   public void assignNotesToMethods() {
        int sound = 0;
        for (Map.Entry<String, ArrayList<String>> clazz : getClasses().entrySet()) {
            ArrayList<String> methods = clazz.getValue();
            for (String method : methods) {
                if (method.contains("main")) {
                    methodSounds.put(method, "resources/Drums_main.aif");
                } else {
                    methodSounds.put(method, "resources/" + screamNotes.get(sound));
                }
                sound = ((sound + 1) % screamNotes.size());
            }
        }
    }

    public ArrayList<String> getMethodsInExcecutionOrder() {
        return methodsInExcecutionOrder;
    }

    public static void main(String[] args) {
        Debugger debugger = new Debugger(new Band());
        debugger.setDebugee(Main.class);

        VirtualMachine virtualMachine;
        try {
            virtualMachine = debugger.connectAndLaunchVirtualMachine();
            debugger.listenToMethodEntryEvents(virtualMachine);
            virtualMachine.eventRequestManager().createExceptionRequest(null, true, true).enable();
            debugger.registerClassesAndMethods(virtualMachine);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> methods = debugger.getMethodsInExcecutionOrder();
        debugger.assignNotesToMethods();

        for (String method : methods) {
            System.out.println(method);
            if (method.contains("main")) {
                AudioPlayer player = new AudioPlayer();
                player.playAndDelay(debugger.getMethodSounds().get(method), 2500L);
            } else {
                AudioPlayer player = new AudioPlayer();
                player.playAndDelay(debugger.getMethodSounds().get(method), 500L);
            }
        }
    }
}
