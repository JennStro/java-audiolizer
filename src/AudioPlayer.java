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
     * Play a file for given milliseconds and then stop the file.
     *
     * @param audioFilePath
     * @param milliseconds
     */
    public void play(String audioFilePath, Long milliseconds) {
        play(audioFilePath);
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clip.stop();
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
     *
     * @return How many milliseconds left to play. Return 0 if clip is null.
     */
    public Long timeLeft() {
        if (clip != null) {
            return getLengthOfClip() - timePlayed();
        }
        return 0L;
    }

    /**
     * @return Get length of clip in milliseconds. Return 0 if clip is null.
     */
    public Long getLengthOfClip() {
        if (clip != null) {
            return convertToMilliseconds(clip.getMicrosecondLength());
        }
        return 0L;
    }

    public Long convertToMilliseconds(Long microseconds) {
        return microseconds / 1000;
    }

    /**
     *
     * @return number of milliseconds this clip has played. 0 if clip is null.
     */
    public Long timePlayed() {
        if (clip != null) {
            return convertToMilliseconds(clip.getMicrosecondPosition());
        }
        return 0L;
    }

    public void stop() {
        clip.stop();
    }
}
