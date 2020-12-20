import java.util.ArrayList;
import java.util.List;

public class MercuryLake implements Instruments {

    private Instrument getMercuryLake() {
        return new Instrument().addNote("C", "C1_MercuryLake.aif")
                .addNote("D", "D1_MercuryLake.aif")
                .addNote("E", "E1_MercuryLake.aif")
                .addNote("F", "F1_MercuryLake.aif")
                .addNote("G", "G1_MercuryLake.aif")
                .addNote("A", "A_MercuryLake.aif")
                .addNote("H", "H1_Mercury_Lake.aif")
                .addNote("C", "C2_MercuryLake.aif");
    }

    private Instrument getDrums() {
        return new Instrument().addNote("C", "C1_SoCal.aif")
                .addNote("D", "D1_SoCal.aif")
                .addNote("E", "E1_SoCal.aif")
                .addNote("H", "H1_SoCal.aif")
                .addNote("C2", "C2_SoCal.aif")
                .addNote("D2", "D2_SoCal.aif");
    }

    @Override
    public ArrayList<Instrument> getInstruments() {
        return new ArrayList<>(List.of(getMercuryLake(), getDrums()));
    }

    @Override
    public String getMainMethodSound() {
        return "A_MercuryLake.aif";
    }
}
