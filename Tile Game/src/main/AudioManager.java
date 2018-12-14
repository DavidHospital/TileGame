package main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {
	
	private static HashMap<String, Clip> clips;
	
	public static void init() {
		loadWaveFiles();
	}
	
	private static void loadWaveFiles() {
		clips = new HashMap<>();
		
		File f = new File ("assets/audio");	
		for (File wav : f.listFiles()) {											//for all files within f
			try {								
				StringTokenizer st = new StringTokenizer (wav.getName(), ".");	
				String name = st.nextToken();
				
				/*	put the name of the image (located before the '.' in the file extension)
				 * 	into 'images' along with the read file
				 */
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(wav);
				Clip clip = AudioSystem.getClip();
				clip.open(audioIn);
				
				clips.put(name, clip);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void Play(String clipName, float volume, boolean loop) {
		if (clips.containsKey(clipName)) {
			Clip clip = clips.get(clipName);
			
			// Adjust the volume
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(20.f * (float) Math.log10(volume));
			
			// Reset the frame position to the start
			clip.setFramePosition(0);
			if (loop) {
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			} else {
				clip.start();
			}
		}
	}
	
	public static void Stop(String clipName) {
		if (clips.containsKey(clipName)) {
			Clip clip = clips.get(clipName);
			clip.stop();
		}
	}
}