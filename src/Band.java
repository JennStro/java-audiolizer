import java.util.ArrayList;
import java.util.List;

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

    private Instrument getGuitar() {
        return new Instrument().addNote("C", "C1_ClassicClean.aif")
                .addNote("D", "D1_ClassicClean.aif")
                .addNote("E", "E1_ClassicClean.aif")
                .addNote("F", "F1_ClassicClean.aif")
                .addNote("G", "G1_ClassicClean.aif")
                .addNote("A", "A1_ClassicClean.aif")
                .addNote("H", "H1_ClassicClean.aif")
                .addNote("C2", "C2_ClassicClean.aif");
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
        return new ArrayList<>(List.of(getPiano(), getGuitar(), getDrums()));
    }

    @Override
    public String getMainMethodSound() {
        return "FG1_Piano.aif";
    }
}
