import java.util.ArrayList;
import java.util.List;

public class WaveSpace implements Instruments {

    private Instrument getWave() {
        return new Instrument().addNote("C", "C1_WaveSpace.aif")
                .addNote("C#", "C#1_WaveSpace.aif")
                .addNote("D", "D1_WaveSpace.aif")
                .addNote("E", "E1_WaveSpace.aif")
                .addNote("F", "F1_WaveSpace.aif")
                .addNote("G", "G1_WaveSpace.aif")
                .addNote("A", "A1_WaveSpace.aif")
                .addNote("H", "H1_WaveSpace.aif")
                .addNote("C2", "C2_WaveSpace.aif");
    }

    @Override
    public ArrayList<Instrument> getInstruments() {
        return new ArrayList<>(List.of(getWave()));
    }

    @Override
    public String getMainMethodSound() {
        return "Main_WaveSpace.aif";
    }
}
