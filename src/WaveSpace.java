import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaveSpace implements Instruments {

    private HashMap<String, String> getWave() {
        return new HashMap<>(Map.of("C", "C1_WaveSpace.aif",
                "C#", "C#1_WaveSpace.aif",
                "D", "D1_WaveSpace.aif",
                "E", "E1_WaveSpace.aif",
                "F", "F1_WaveSpace.aif",
                "G", "G1_WaveSpace.aif",
                "A", "A1_WaveSpace.aif",
                "H", "H1_WaveSpace.aif",
                "C2", "C2_WaveSpace.aif",
                "Drum", "trommer.aif"));

    }

    @Override
    public ArrayList<HashMap<String, String>> getInstruments() {
        return new ArrayList<>(List.of(getWave()));
    }

    @Override
    public String getMainMethodSound() {
        return "Main_WaveSpace.aif";
    }
}
