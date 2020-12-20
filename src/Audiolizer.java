import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.event.*;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.nio.sctp.PeerAddressChangeNotification;

import java.io.IOException;
import java.lang.management.PlatformLoggingMXBean;
import java.util.*;

public class Audiolizer {

    private VirtualMachine virtualMachine;

    private final Instruments instruments;
    private HashMap<String, String> mappedSounds;
    private Class debugee;
    private Long delayInMilliseconds = 1000L;

    private AudioPlayer currentPlayer;

    public Audiolizer(Instruments intruments, Class<Main> debugee) {
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
                        this.currentPlayer = player;
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

    public boolean isPlaying() {
        return currentPlayer.isPlaying();
    }

    public static void main(String[] args) {
        Audiolizer audiolizer = new Audiolizer(new Orchestra(), Main.class);
        audiolizer.setSpeed(4000L);
        audiolizer.playMusic();
        while (audiolizer.isPlaying()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
