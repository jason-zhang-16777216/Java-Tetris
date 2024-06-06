package tetris;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class AudioPlayer {
	private static Clip song; 

	public static void addSong() {
		try {
			File bgmusic = new File("src/tetris/audio/game-music-loop-3-144252.wav");
			if (bgmusic == null) {
				throw new IOException("Sound File not Found");
			}
			
			AudioInputStream input = AudioSystem.getAudioInputStream(bgmusic);
            song = AudioSystem.getClip();
            song.open(input);
            song.start();
            // loop the audio
            song.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
	}
}
