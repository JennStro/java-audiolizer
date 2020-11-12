import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {

    private Clip clip;

    public void play(String audioFilePath) {
        File audioFile = new File(audioFilePath);
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(audioFile);
            DataLine.Info dataLineInfo = new DataLine.Info(Clip.class, null);
            this.clip = (Clip) AudioSystem.getLine(dataLineInfo);
            clip.open(inputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    /**
     * @return true if this clip is playing.
     */
    public boolean isPlaying() {
        if (clip != null) {
            return timePlayed() < getLengthOfClip();
        }
        return false;
    }

    /**
     * @return Get length of clip in milliseconds.
     */
    public Long getLengthOfClip() {
        return convertToMilliseconds(clip.getMicrosecondLength());
    }

    public Long convertToMilliseconds(Long microseconds) {
        return microseconds / 1000;
    }

    /**
     *
     * @return number of milliseconds this clip has played
     */
    public Long timePlayed() {
        return convertToMilliseconds(clip.getMicrosecondPosition());
    }

}
