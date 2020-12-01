import java.util.ArrayList;
import java.util.HashMap;

public interface Instruments {


    /**
     *
     * @return a list of all instruments.
     *
     * An instrument is a map containing notes and path to soundfile. <"C", "path/to/intrument_C_note">
     *
     */
    ArrayList<HashMap<String, String>> getInstruments();

    /**
     *
     * @param instrument
     * @return the notes that this instrument in playing
     */
    ArrayList<String> getNotes(HashMap<String, String> instrument);

    /**
     *
     * @return path to file that will be played when program is started
     */
    String getMainMethodSound();
}
