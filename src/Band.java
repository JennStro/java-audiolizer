import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Band implements Instruments {

    @Override
    public HashMap<String, String> getPiano() {
        return new HashMap<String, String>(Map.of("C", "ScreamLead_C1.aif",
                "D", "ScreamLead_D1.aif",
                "E", "ScreamLead_E1.aif",
                "F", "ScreamLead_F1.aif",
                "G", "ScreamLead_G1.aif",
                "A", "ScreamLead_A1.aif",
                "H", "ScreamLead_H1.aif",
                "C2", "ScreamLead_C2.aif"));
    }

    @Override
    public HashMap<String, String> getGuitar() {
        return new HashMap<String, String>(Map.of("C", "ScreamLead_C1.aif",
                "D", "ScreamLead_D1.aif",
                "E", "ScreamLead_E1.aif",
                "F", "ScreamLead_F1.aif",
                "G", "ScreamLead_G1.aif",
                "A", "ScreamLead_A1.aif",
                "H", "ScreamLead_H1.aif",
                "C2", "ScreamLead_C2.aif"));
    }

    @Override
    public HashMap<String, String>[] getInstruments() {
        return (HashMap<String, String>[]) new Object[]{getGuitar(), getPiano()};
    }

    @Override
    public ArrayList<String> getNotes(HashMap<String, String> instrument) {
        ArrayList<String> notes = new ArrayList<>();
        for (String note : instrument.keySet()) {
            notes.add(note);
        }
        return notes;
    }
}
