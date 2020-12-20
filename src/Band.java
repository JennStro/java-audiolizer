import java.util.ArrayList;
import java.util.List;

public class Band implements Instruments {

    private Instrument getPiano() {
        return new Instrument().addNote("C", "resources/C1_Piano.aif")
                .addNote("D", "resources/D1_Piano.aif")
                .addNote("E", "resources/E1_Piano.aif")
                .addNote("F", "resources/F1_Piano.aif")
                .addNote("G", "resources/G1_Piano.aif")
                .addNote("A", "resources/A1_Piano.aif")
                .addNote("H", "resources/H1_Piano.aif")
                .addNote("C2", "resources/C2_Piano.aif");
    }

    private Instrument getGuitar() {
        return new Instrument().addNote("C", "resources/C1_ClassicClean.aif")
                .addNote("D", "resources/D1_ClassicClean.aif")
                .addNote("E", "resources/E1_ClassicClean.aif")
                .addNote("F", "resources/F1_ClassicClean.aif")
                .addNote("G", "resources/G1_ClassicClean.aif")
                .addNote("A", "resources/A1_ClassicClean.aif")
                .addNote("H", "resources/H1_ClassicClean.aif")
                .addNote("C2", "resources/C2_ClassicClean.aif");
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
        return new ArrayList<>(List.of(getPiano(), getGuitar(), getDrums()));
    }

    @Override
    public String getMainMethodSound() {
        return "resources/FG1_Piano.aif";
    }
}
