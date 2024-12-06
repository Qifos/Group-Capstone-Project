package Sudoku;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

/**
 * This enum encapsulates all the sound effects for the Sudoku game.
 * 1. Define all your sound effect names and the associated wave file.
 * 2. To play a specific sound, simply invoke SoundEffect.SOUND_NAME.play().
 * 3. Optionally invoke the static method SoundEffect.initGame() to pre-load all the sound files.
 * 4. Use the static variable SoundEffect.volume to adjust or mute the sound.
 */
public enum SoundEffect {
    CORRECT_GUESS("eatfood.wav"),
    WRONG_GUESS("die.wav");

    /** Nested enumeration for specifying volume */
    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.MEDIUM; // Default volume level

    /** Each sound effect has its own clip, loaded with its own sound file. */
    private Clip clip;

    /** Private Constructor to construct each element of the enum with its own sound file. */
    SoundEffect(String soundFileName) {
        try {
            // Use URL (instead of File) to read from disk and JAR.
            URL url = this.getClass().getClassLoader().getResource(soundFileName);
            // Set up an audio input stream piped from the sound file.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            // Get a clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /** Play or replay the sound effect from the beginning. */
    public void play() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop(); // Stop the player if it is still running
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start(); // Start playing
        }
    }

    /** Optional static method to pre-load all the sound files. */
    static void initGame() {
        values(); // Calls the constructor for all the elements
    }
}
