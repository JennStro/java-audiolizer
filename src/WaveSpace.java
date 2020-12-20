import java.util.ArrayList;
import java.util.List;

public class WaveSpace implements Instruments {

    private Instrument getWave() {
        return new Instrument().addNote("C", "resources/C1_WaveSpace.aif")
                .addNote("C#", "resources/C#1_WaveSpace.aif")
                .addNote("D", "resources/D1_WaveSpace.aif")
                .addNote("E", "resources/E1_WaveSpace.aif")
                .addNote("F", "resources/F1_WaveSpace.aif")
                .addNote("G", "resources/G1_WaveSpace.aif")
                .addNote("A", "resources/A1_WaveSpace.aif")
                .addNote("H", "resources/H1_WaveSpace.aif")
                .addNote("C2", "resources/C2_WaveSpace.aif");
    }

    @Override
    public ArrayList<Instrument> getInstruments() {
        return new ArrayList<>(List.of(getWave()));
    }

    @Override
    public String getMainMethodSound() {
        return "resources/Main_WaveSpace.aif";
    }
}
