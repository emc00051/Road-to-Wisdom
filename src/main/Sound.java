package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	
	Clip clip;
	URL soundURL[]= new URL[30];
	long ms=0;
	
	public Sound() {
		
		soundURL[0]=getClass().getResource("/sound/8_bit_nostalgia_game.wav");
		soundURL[1]=getClass().getResource("/sound/coin.wav");
		soundURL[2]=getClass().getResource("/sound/powerup.wav");
		soundURL[3]=getClass().getResource("/sound/unlock.wav");
		soundURL[4]=getClass().getResource("/sound/fanfare.wav");
		soundURL[5]=getClass().getResource("/sound/hitmonster.wav");
		soundURL[6]=getClass().getResource("/sound/recievedamage.wav");
		soundURL[7]=getClass().getResource("/sound/swing.wav");
		soundURL[8]=getClass().getResource("/sound/gameover.wav");
		soundURL[9]=getClass().getResource("/sound/e6.wav");
		soundURL[10]=getClass().getResource("/sound/g6.wav");
		soundURL[11]=getClass().getResource("/sound/c6.wav");
		soundURL[12]=getClass().getResource("/sound/a6.wav");
	}
	
	public void setFile(int i) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip=AudioSystem.getClip();
			clip.open(ais);
		} catch (Exception e) {
			
		}
	}
	
	public void play() {
		if (clip != null) {
	        if (clip.isRunning()) {
	            clip.stop();
	        }
	        clip.setMicrosecondPosition(ms);
	        clip.start();
	    }
		else {
			return;
		}
	}
	
	public void loop() {
		
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	public void stop() {
		
		clip.stop();
	}
	public void pause() {
		ms=clip.getMicrosecondPosition();
		clip.stop();
	}
	public void resume() {
		clip.setMicrosecondPosition(ms);
		clip.start();
	}
}
