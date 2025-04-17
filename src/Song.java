import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Song {
    private File filePath;
    private String title;
    private Clip clip;
    private boolean isPaused;
    private long pausePosition = 0;
    private final int skipSecond = 5000000;

    public Song(String title, File filePath) {
        this.title = title;
        this.filePath = filePath;
    }

    public void start() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(filePath);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    public void pause() {
        if(clip.isRunning()) {
            pausePosition = clip.getMicrosecondPosition();
            clip.stop();
            status();
            isPaused = true;
        }
    }

    public void resume() {
        if(isPaused) {
            clip.setMicrosecondPosition(pausePosition);
            clip.start();
            status();
            isPaused = false;
        }
    }

    public void stop() {
        clip.stop();
        clip.close();
    }

    public void skipForward() {
        if(clip.getMicrosecondPosition() + skipSecond <= clip.getMicrosecondLength()) {
            long current = clip.getMicrosecondPosition();
            clip.setMicrosecondPosition(current + skipSecond);
        }
    }

    public void skipBack() {
        if(clip.getMicrosecondPosition() - skipSecond >= 0) {
            long current = clip.getMicrosecondPosition();
            clip.setMicrosecondPosition(current - skipSecond);
        }
    }

    public void status() {
        new Thread(() -> {
            while(clip != null && clip.isRunning()) {
                long current = clip.getMicrosecondPosition() / 1000000;
                long total = clip.getMicrosecondLength() / 1000000;
                int barSize = 40;
                int filled = (int)((double) current / total * barSize);
                String bar = "[" + "#".repeat(filled) + "-".repeat(barSize - filled) + "]";
                System.out.print("\r" + bar + " " + current + " / " + total + " sec");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
            System.out.println();
        }).start();
    }

    @Override
    public String toString() {
        return this.title;
    }
}
