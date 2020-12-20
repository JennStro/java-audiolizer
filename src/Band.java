import java.util.ArrayList;
import java.util.List;

public class Band implements Instruments {

    private Instrument getPiano() {
        return new Instrument().addNote("C", "resources/Piano/C1_Piano.aif")
                .addNote("D", "resources/Piano/D1_Piano.aif")
                .addNote("E", "resources/Piano/E1_Piano.aif")
                .addNote("G", "resources/Piano/G1_Piano.aif")
                .addNote("A", "resources/Piano/A1_Piano.aif");
    }

    private Instrument getGuitar() {
        return new Instrument().addNote("C", "resources/Guitar/C1_ClassicClean.aif")
                .addNote("D", "resources/Guitar/D1_ClassicClean.aif")
                .addNote("E", "resources/Guitar/E1_ClassicClean.aif")
                .addNote("F", "resources/Guitar/F1_ClassicClean.aif")
                .addNote("G", "resources/Guitar/G1_ClassicClean.aif")
                .addNote("A", "resources/Guitar/A1_ClassicClean.aif")
                .addNote("H", "resources/Guitar/H1_ClassicClean.aif")
                .addNote("C2", "resources/Guitar/C2_ClassicClean.aif");
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
        return new ArrayList<>(List.of(getPiano(), getGuitar(), getDrums()));
    }

    @Override
    public int numberOfInstruments() {
        return getInstruments().size();
    }

    @Override
    public String getMainMethodSound() {
        return "resources/FG1_Piano.aif";
    }
}
