import javax.sound.sampled.*;
import java.io.File;

class SoundPlayer {
    private Clip clip;

    public SoundPlayer(String soundFileName) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(soundFileName));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null && !clip.isRunning()) {
            clip.setFramePosition(0); // rewind to the beginning
            clip.start();
        }
    }
}
