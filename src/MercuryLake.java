import java.util.ArrayList;
import java.util.List;

public class MercuryLake implements Instruments {

    private Instrument getMercuryLake() {
        return new Instrument().addNote("C", "resources/C1_MercuryLake.aif")
                .addNote("D", "resources/D1_MercuryLake.aif")
                .addNote("E", "resources/E1_MercuryLake.aif")
                .addNote("F", "resources/F1_MercuryLake.aif")
                .addNote("G", "resources/G1_MercuryLake.aif")
                .addNote("A", "resources/A_MercuryLake.aif")
                .addNote("H", "resources/H1_Mercury_Lake.aif")
                .addNote("C", "resources/C2_MercuryLake.aif");
    }

    private Instrument getDrums() {
        return new Instrument().addNote("C", "resources/C1_SoCal.aif")
                .addNote("D", "resources/D1_SoCal.aif")
                .addNote("E", "resources/E1_SoCal.aif")
                .addNote("H", "resources/H1_SoCal.aif")
                .addNote("C2", "resources/C2_SoCal.aif")
                .addNote("D2", "resources/D2_SoCal.aif");
    }

    @Override
    public ArrayList<Instrument> getInstruments() {
        return new ArrayList<>(List.of(getMercuryLake(), getDrums()));
    }

    @Override
    public String getMainMethodSound() {
        return "resources/A_MercuryLake.aif";
    }
}
