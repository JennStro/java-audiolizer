import java.util.ArrayList;
import java.util.List;

public class MercuryLake implements Instruments {

    private Instrument getMercuryLake() {
        return new Instrument().addNote("C", "resources/Effects/C1_MercuryLake.aif")
                .addNote("D", "resources/Effects/D1_MercuryLake.aif")
                .addNote("E", "resources/Effects/E1_MercuryLake.aif")
                .addNote("F", "resources/Effects/F1_MercuryLake.aif")
                .addNote("G", "resources/Effects/G1_MercuryLake.aif")
                .addNote("A", "resources/Effects/A_MercuryLake.aif")
                .addNote("H", "resources/Effects/H1_Mercury_Lake.aif")
                .addNote("C", "resources/Effects/C2_MercuryLake.aif");
    }

    private Instrument getDrums() {
        return new Instrument().addNote("C", "resources/Drums/C1_SoCal.aif")
                .addNote("D", "resources/Drums/D1_SoCal.aif")
                .addNote("E", "resources/Drums/E1_SoCal.aif")
                .addNote("H", "resources/Drums/H1_SoCal.aif")
                .addNote("C2", "resources/Drums/C2_SoCal.aif")
                .addNote("D2", "resources/Drums/D2_SoCal.aif");
    }

    @Override
    public ArrayList<Instrument> getInstruments() {
        return new ArrayList<>(List.of(getMercuryLake(), getDrums()));
    }

    @Override
    public int numberOfInstruments() {
        return getInstruments().size();
    }

}
