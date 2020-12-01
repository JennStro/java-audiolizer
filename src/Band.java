import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        return new HashMap<String, String>(Map.of("C", "C1_ClassicClean.aif",
                "D", "D1_ClassicClean.aif",
                "E", "E1_ClassicClean.aif",
                "F", "F1_ClassicClean.aif",
                "G", "G1_ClassicClean.aif",
                "A", "A1_ClassicClean.aif",
                "H", "H1_ClassicClean.aif",
                "C2", "C2_ClassicClean.aif"));
    }

    @Override
    public HashMap<String, String> getDrums() {
        return new HashMap<String, String>(Map.of("C", "C1_SoCal.aif",
                "D", "D1_SoCal.aif",
                "E", "E1_SoCal.aif",
                "H", "H1_SoCal.aif",
                "C2", "C2_SoCal.aif",
                "D2", "D2_SoCal.aif"));
    }

    @Override
    public ArrayList<HashMap<String, String>> getInstruments() {
        return new ArrayList<>(List.of(getGuitar(), getPiano(), getDrums()));
    }

    @Override
    public ArrayList<String> getNotes(HashMap<String, String> instrument) {
        ArrayList<String> notes = new ArrayList<>();
        for (String note : instrument.keySet()) {
            notes.add(note);
        }
        return notes;
    }

    @Override
    public String getMainMethodSound() {
        return "Drums_main.aif";
    }
}
