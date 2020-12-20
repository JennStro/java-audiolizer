import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.*;
import com.sun.jdi.request.MethodEntryRequest;

import java.io.IOException;
import java.lang.management.PlatformLoggingMXBean;
import java.util.*;

public class Audiolizer {

    private VirtualMachine virtualMachine;

    private final Instruments instruments;
    private HashMap<String, String> mappedSounds;
    private Class debugee;
    private final HashMap<String, String> methodSounds;
    private final HashMap<String, Integer> lengthOfMethod;
    private final ArrayList<String> methodsInExecutionOrder;
    private final HashMap<String, ArrayList<String>> classes;
    private Long delayInMilliseconds = 1000L;

    public Audiolizer(Instruments intruments, Class<Main> debugee) {
        this.classes = new HashMap<>();
        this.methodSounds = new HashMap<>();
        this.lengthOfMethod = new HashMap<>();
        this.methodsInExecutionOrder = new ArrayList<>();
        this.instruments = intruments;
        this.debugee = debugee;
        this.mappedSounds = new HashMap<>();

        this.virtualMachine = connectAndLaunchVirtualMachine();
    }

    /**
     * Create connector and give it the debuggee's main class, connect this debugger to the VM and launch the VM.
     *
     * @return Virtual Machine for debugging
     */
    public VirtualMachine connectAndLaunchVirtualMachine() {
        LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
        Map<String, Connector.Argument> connectorArguments = launchingConnector.defaultArguments();
        connectorArguments.get("main").setValue(debugee.getName());
        connectorArguments.get("options").setValue("-cp " + System.getProperty("java.class.path"));
        try {
            return launchingConnector.launch(connectorArguments);
        } catch (IOException | IllegalConnectorArgumentsException | VMStartException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void playMusic() {
        listenToExceptionEvents();
        listenToMethodEntryEvents();

        EventSet events;
        try {
            while ((events = virtualMachine.eventQueue().remove()) != null) {
                for (Event event : events) {
                    if (event instanceof ExceptionEvent) {
                        System.out.println(event.toString());
                    }

                    if (event instanceof MethodEntryEvent) {
                        AudioPlayer player = new AudioPlayer();
                        String methodName = ((MethodEntryEvent) event).method().toString();

                        if (mappedSounds.containsKey(methodName)) {
                            System.out.println(methodName + " " + mappedSounds.get(methodName));
                            player.playAndDelay(mappedSounds.get(methodName), delayInMilliseconds);

                        } else {
                            String randomSoundFile = getRandomSoundFile();
                            System.out.println(methodName + " " + randomSoundFile);
                            mappedSounds.put(methodName, randomSoundFile);
                            player.playAndDelay(randomSoundFile, delayInMilliseconds);
                        }
                    }
                    virtualMachine.resume();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (VMDisconnectedException e) {
            System.out.println("Virtual Machine is disconnected.");
        }
    }

    /**
     * Enable methodEntryRequest to get notified when a method is entried  in the debugee class.
     *
     * @see <a href="https://docs.oracle.com/javase/8/docs/jdk/api/jpda/jdi/com/sun/jdi/event/MethodEntryEvent.html">MethodEntryEvent</a>
     *
     */
    public void listenToMethodEntryEvents() {
        MethodEntryRequest methodEntryRequest = virtualMachine.eventRequestManager().createMethodEntryRequest();
        methodEntryRequest.addClassExclusionFilter("java.*");
        methodEntryRequest.addClassExclusionFilter("jdk.*");
        methodEntryRequest.addClassExclusionFilter("sun.*");
        methodEntryRequest.enable();
    }

    public void listenToExceptionEvents() {
        virtualMachine.eventRequestManager().createExceptionRequest(null, true, true).enable();
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

        int minimum_length_of_method = 1;
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

    public HashMap<String, String> getMethodSounds() {
        return this.methodSounds;
    }


   public void assignNotesToMethods() {
        int instrument = 0;

       System.out.println(getClasses());

        for (Map.Entry<String, ArrayList<String>> clazz : getClasses().entrySet()) {

            ArrayList<String> methods = clazz.getValue();

            Instrument instrumentToPlay = this.instruments.getInstruments().get(instrument);
            ArrayList<String> notes = instrumentToPlay.getNoteCharacters();
            int noteNumber = 0;

            for (String method : methods) {
                if (method.contains("Main.main")) {
                    methodSounds.put(method, "resources/" + this.instruments.getMainMethodSound());
                    // Do not use an instrument for the main class.
                    instrument = instrument - 1;
                } else {
                    String note = notes.get(noteNumber);
                    methodSounds.put(method, "resources/" + instrumentToPlay.getSoundFile(note));
                    noteNumber = (noteNumber + 1) % notes.size();
                }
            }

            instrument = (instrument+1) % this.instruments.getInstruments().size();
        }
    }

    public Instrument getRandomInstrument() {
        return this.instruments.getInstruments().get(new Random().nextInt(this.instruments.numberOfInstruments()));
    }

    public String getRandomSoundFile() {
        try {
            return getRandomInstrument().getRandomSoundFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * How long each note should play in milliseconds.
     * @param delayInMilliseconds
     */
    public void setSpeed(Long delayInMilliseconds) {
        this.delayInMilliseconds = delayInMilliseconds;
    }

    public static void main(String[] args) {
        Audiolizer audiolizer = new Audiolizer(new WaveSpace(), Main.class);
        audiolizer.setSpeed(1000L);
        audiolizer.playMusic();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
