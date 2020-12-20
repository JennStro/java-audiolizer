import java.util.ArrayList;
import java.util.List;

public class WaveSpace implements Instruments {

    private Instrument getWave() {
        return new Instrument().addNote("C", "resources/Effects/C1_WaveSpace.aif")
                .addNote("C#", "resources/Effects/C#1_WaveSpace.aif")
                .addNote("D", "resources/Effects/D1_WaveSpace.aif")
                .addNote("E", "resources/Effects/E1_WaveSpace.aif")
                .addNote("F", "resources/Effects/F1_WaveSpace.aif")
                .addNote("G", "resources/Effects/G1_WaveSpace.aif")
                .addNote("A", "resources/Effects/A1_WaveSpace.aif")
                .addNote("H", "resources/Effects/H1_WaveSpace.aif")
                .addNote("C2", "resources/Effects/C2_WaveSpace.aif");
    }

    @Override
    public ArrayList<Instrument> getInstruments() {
        return new ArrayList<>(List.of(getWave()));
    }

    @Override
    public int numberOfInstruments() {
        return getInstruments().size();
    }

    @Override
    public String getMainMethodSound() {
        return "resources/Main_WaveSpace.aif";
    }
}
