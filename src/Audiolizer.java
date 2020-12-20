import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.*;
import com.sun.jdi.request.MethodEntryRequest;

import java.util.*;

public class Audiolizer {

    private final Instruments instruments;
    private Class debugee;
    private HashMap<String, String> methodSounds;
    private HashMap<String, Integer> lengthOfMethod;
    private ArrayList<String> methodsInExecutionOrder;
    private HashMap<String, ArrayList<String>> classes;
    private final int minimum_length_of_method = 1;

    public Audiolizer(Instruments intruments) {
        this.classes = new HashMap<>();
        this.methodSounds = new HashMap<>();
        this.lengthOfMethod = new HashMap<>();
        this.methodsInExecutionOrder = new ArrayList<>();
        this.instruments = intruments;
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
    private void addMethod(Method method) throws AbsentInformationException {
        methodsInExecutionOrder.add(method.toString());

        String callingClass = callingClass(method);

        if (!classes.containsKey(callingClass)) {
            ArrayList<String> classMethods = new ArrayList<>();
            classMethods.add(method.toString());
            classes.put(callingClass, classMethods);

            int numberOfLinesInMethod = method.allLineLocations().size();
            lengthOfMethod.put(method.toString(), numberOfLinesInMethod + minimum_length_of_method);
        } else {
            classes.get(callingClass).add(method.toString());

            int numberOfLinesInMethod = method.allLineLocations().size();
            lengthOfMethod.put(method.toString(), numberOfLinesInMethod + minimum_length_of_method);
        }
    }

    public int getLengthOfMethod(String methodName) {
        System.out.println(lengthOfMethod);
        return lengthOfMethod.get(methodName);
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
        } catch (AbsentInformationException e) {
        e.printStackTrace();
    }
    }

    public HashMap<String, String> getMethodSounds() {
        return this.methodSounds;
    }


   public void assignNotesToMethods() {
        int instrument = 0;

       System.out.println(getClasses());

        for (Map.Entry<String, ArrayList<String>> clazz : getClasses().entrySet()) {

            ArrayList<String> methods = clazz.getValue();

            HashMap<String, String> instrumentToPlay = this.instruments.getInstruments().get(instrument);
            ArrayList<String> notes = this.instruments.getNotes(instrumentToPlay);
            int noteNumber = 0;

            for (String method : methods) {
                if (method.contains("Main.main")) {
                    methodSounds.put(method, "resources/" + this.instruments.getMainMethodSound());
                    // Do not use an instrument for the main class.
                    instrument = instrument - 1;
                } else {
                    String note = notes.get(noteNumber);
                    methodSounds.put(method, "resources/" + instrumentToPlay.get(note));
                    noteNumber = (noteNumber + 1) % notes.size();
                }
            }

            instrument = (instrument+1) % this.instruments.getInstruments().size();
        }
    }

    public HashMap<String, String> getRandomInstrument() {
        return this.instruments.getInstruments().get(new Random().nextInt(this.instruments.getInstruments().size()-1));
    }

    public String getRandomSoundFile() {
        ArrayList<String> notes = this.instruments.getNotes(getRandomInstrument());
        return getRandomInstrument()
    }

    public ArrayList<String> getMethodsInExecutionOrder() {
        return methodsInExecutionOrder;
    }

    public static void main(String[] args) {
        Audiolizer audiolizer = new Audiolizer(new MercuryLake());
        audiolizer.setDebugee(Main.class);

        VirtualMachine virtualMachine;
        try {
            virtualMachine = audiolizer.connectAndLaunchVirtualMachine();
            audiolizer.listenToMethodEntryEvents(virtualMachine);
            virtualMachine.eventRequestManager().createExceptionRequest(null, true, true).enable();
            //audiolizer.registerClassesAndMethods(virtualMachine);

            EventSet events;
            try {
                while ((events = virtualMachine.eventQueue().remove()) != null) {
                    for (Event event : events) {
                        if (event instanceof ExceptionEvent) {
                            System.out.println(event.toString());
                        }

                        if (event instanceof MethodEntryEvent) {
                            Method enteredMethod = ((MethodEntryEvent) event).method();

                            //addMethod(enteredMethod);
                        }
                        virtualMachine.resume();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (AbsentInformationException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> methods = audiolizer.getMethodsInExecutionOrder();
        audiolizer.assignNotesToMethods();

        for (String method : methods) {
            System.out.println(method);
            if (method.contains("Main.main")) {
                AudioPlayer player = new AudioPlayer();
                player.playAndDelay(audiolizer.getMethodSounds().get(method), 5000L);
            } else {
                AudioPlayer player = new AudioPlayer();
                System.out.println(audiolizer.getMethodSounds().get(method));
                player.playAndDelay(audiolizer.getMethodSounds().get(method), 5000L);
            }
        }
    }
}
