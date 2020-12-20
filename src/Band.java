import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Band implements Instruments {

    private Instrument getPiano() {
        return new Instrument().addNote("C", "C1_Piano.aif")
                .addNote("D", "D1_Piano.aif")
                .addNote("E", "E1_Piano.aif")
                .addNote("F", "F1_Piano.aif")
                .addNote("G", "G1_Piano.aif")
                .addNote("A", "A1_Piano.aif")
                .addNote("H", "H1_Piano.aif")
                .addNote("C2", "C2_Piano.aif");
    }

    private HashMap<String, String> getGuitar() {
        return new HashMap<String, String>(Map.of("C", "C1_ClassicClean.aif",
                "D", "D1_ClassicClean.aif",
                "E", "E1_ClassicClean.aif",
                "F", "F1_ClassicClean.aif",
                "G", "G1_ClassicClean.aif",
                "A", "A1_ClassicClean.aif",
                "H", "H1_ClassicClean.aif",
                "C2", "C2_ClassicClean.aif"));
    }

    private HashMap<String, String> getDrums() {
        return new HashMap<String, String>(Map.of("C", "C1_SoCal.aif",
                "D", "D1_SoCal.aif",
                "E", "E1_SoCal.aif",
                "H", "H1_SoCal.aif",
                "C2", "C2_SoCal.aif",
                "D2", "D2_SoCal.aif"));
    }

    @Override
    public ArrayList<HashMap<String, String>> getInstruments() {
        return new ArrayList<>(List.of(getPiano(), getGuitar(), getDrums()));
    }

    @Override
    public String getMainMethodSound() {
        return "FG1_Piano.aif";
    }
}
