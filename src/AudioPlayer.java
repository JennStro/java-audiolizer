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

    public boolean isPlaying() {
        if (clip != null) {
            return clip.isActive();
        }
        return false;
    }

}
