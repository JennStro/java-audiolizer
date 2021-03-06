import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Instrument {

    private final HashMap<String, String> instrument = new HashMap<>();

    public Instrument addNote(String note, String soundFile) {
        instrument.put(note, soundFile);
        return this;
    }

    public String getSoundFile(String note) {
        return instrument.get(note);
    }

    public ArrayList<String> getNoteCharacters() {
        return new ArrayList<>(instrument.keySet());
    }

    public String getRandomSoundFile() {
        return getSoundFile(getNoteCharacters().get(new Random().nextInt(getNoteCharacters().size()-1)));
    }

    public ArrayList<String> getSoundFiles() {
        return new ArrayList<>(instrument.values());
    }

}
