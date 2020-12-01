import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MercuryLake implements Instruments {

    private HashMap<String, String> getInstrument() {
        return new HashMap<String, String>(Map.of("C", "C1_MercuryLake.aif",
                "D", "D1_MercuryLake.aif",
                "E", "E1_MercuryLake.aif",
                "F", "F1_MercuryLake.aif",
                "G", "G1_MercuryLake.aif",
                "A", "A_MercuryLake.aif",
                "H", "H1_MercuryLake.aif",
                "1", "C2_MercuryLake.aif"));
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
        return new ArrayList<>(List.of(getInstrument(), getDrums()));
    }

    @Override
    public String getMainMethodSound() {
        return "A_MercuryLake.aif";
    }
}
