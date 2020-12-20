import java.util.ArrayList;

public interface Instruments {


    /**
     *
     * @return a list of all instruments.
     *
     * An instrument is a map containing notes and path to soundfile. <"C", "path/to/intrument_C_note">
     *
     */
    ArrayList<Instrument> getInstruments();

    /**
     * The number of instruments
     */
    int numberOfInstruments();

    /**
     *
     * @return path to file that will be played when program is started
     */
    String getMainMethodSound();
}
