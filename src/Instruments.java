import java.util.ArrayList;
import java.util.HashMap;

public interface Instruments {

    /**
     *
     * @return the map containing notes and path to file of piano sound. <"C", "path/to/intstrument/C/note">
     */
    HashMap<String, String> getPiano();

    /**
     *
     * @return the map containing notes and path to file of guitar sound. <"C", "path/to/intrument/C/note">
     */
    HashMap<String, String> getGuitar();

    /**
     *
     * @return the map containing notes and path to file of drum sound. <"C", "path/to/intrument/C/note">
     */
    HashMap<String, String> getDrums();

    /**
     *
     * @return a list of all instruments
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
