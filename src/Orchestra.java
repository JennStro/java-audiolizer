import java.util.ArrayList;
import java.util.List;

public class Orchestra implements Instruments {
    @Override
    public ArrayList<Instrument> getInstruments() {
        return new ArrayList<>(List.of(
                new Instrument().addNote("1","resources/Strings/String1.aif")
        .addNote("2","resources/Strings/String2.aif")
        .addNote("3","resources/Strings/String3.aif")
        .addNote("4","resources/Strings/String4.aif")
        .addNote("5","resources/Strings/String5.aif")
        .addNote("6","resources/Strings/String6.aif")
        .addNote("7","resources/Strings/String7.aif")
        .addNote("8", "resources/Strings/StringOne1.aif")
        .addNote("9", "resources/Strings/StringOne_2.aif")
        .addNote("10", "resources/Strings/StringOne3.aif")
        .addNote("11", "resources/Strings/StringOne4.aif")
        .addNote("12", "resources/Strings/StringOne5.aif")));
    }

    @Override
    public int numberOfInstruments() {
        return getInstruments().size();
    }
}

