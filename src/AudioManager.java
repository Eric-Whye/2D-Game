/* Eric Whye
Student Number: 19336881
 */
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class AudioManager {
    private static final AudioManager instance = new AudioManager();
    public static AudioManager getInstance(){return instance;}

    private AudioManager(){}

    public void playSound(String filename, float changeSoundLevel){
        try {
            if (filename.isEmpty())return;
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filename));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(changeSoundLevel);//Change volumne
            clip.start();

        }catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {throw new RuntimeException(e);}
    }
    public void playRandomSound(String[] filenames, float changeSoundLevel){
        try {
            if (filenames.length==0)return;
            String filename = filenames[new Random().nextInt(filenames.length)];
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filename));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(changeSoundLevel);//Change volumne
            clip.start();
        }catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {throw new RuntimeException(e);}
    }
    public void playOnLoop(String filename, float changeSoundLevel){
        try {
            if (filename.isEmpty())return;
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filename));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(changeSoundLevel);//Change volumne
            clip.start();
        }catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {throw new RuntimeException(e);}
    }
}
