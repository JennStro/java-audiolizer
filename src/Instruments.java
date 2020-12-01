import java.util.ArrayList;
import java.util.HashMap;

public interface Instruments {

    /**
     *
     * @return the map containing notes and path to file of piano sound. <"C", "path/to/intstrument/C/note">
     */
    public HashMap<String, String> getPiano();

    /**
     *
     * @return the map containing notes and path to file of guitar sound. <"C", "path/to/intrument/C/note">
     */
    public HashMap<String, String> getGuitar();

    /**
     *
     * @return a list of all instruments
     */
    public HashMap<String, String>[] getInstruments();

    /**
     *
     * @param instrument
     * @return the notes that this instrument in playing
     */
    public ArrayList<String> getNotes(HashMap<String, String> instrument);
}
