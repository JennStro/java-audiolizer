import java.util.HashMap;

public class Instrument {

    HashMap<String, String> instrument = new HashMap<>();

    public Instrument addNote(String note, String soundFile) {
        instrument.put(note, soundFile);
        return this;
    }

}
