import java.util.ArrayList;
import java.util.HashMap;

public class Instrument {

    HashMap<String, String> instrument = new HashMap<>();

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
        return
    }

}
