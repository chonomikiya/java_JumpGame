package kdg.chono;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

/**
 * GameBaseのスーパークラス
 * @author chono
 *
 */
public class GameBaseMethod {
	
	public boolean spkey = false;
	public boolean pkey = false;
	public boolean rkey = false;
	public boolean leftkey = false;
	public boolean rightkey = false;
	public boolean nkey = false;
	
	public JFrame frame1;
	public Sequencer midiseq = null;

	public GameBaseMethod() {
		super();
	}

	public BufferedReader StageReader(String stage) {
		 BufferedReader br = null;
					InputStream in = getClass().getResourceAsStream(stage); 
			          br = new BufferedReader(new InputStreamReader(in));
		return br;
	}

	public BufferedImage ImageReader(String imagename) {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(getClass().getResource(imagename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bi;
	}

	public Clip soundReader(String fname) {
		Clip clip = null;
		try {
			AudioInputStream aistream = AudioSystem.
				getAudioInputStream(getClass().getResource(fname));
			DataLine.Info info = new DataLine.Info(Clip.class, aistream.getFormat());
			clip = (Clip)AudioSystem.getLine(info);
			clip.open(aistream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		return clip;
	}

	public void midiReader(String fname) {
		if (midiseq == null){
			try {
				midiseq = MidiSystem.getSequencer();
				midiseq.open();
			} catch (MidiUnavailableException e1) {
				e1.printStackTrace();
			}
		}
		try {
			Sequence seq = MidiSystem.getSequence(getClass().getResource(fname));
			midiseq.setSequence(seq);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void drawStringCenter(String str, int y, Graphics g) {
		int fw = frame1.getWidth() / 2;
		FontMetrics fm = g.getFontMetrics();
		int strw = fm.stringWidth(str) /2 ;
		g.drawString(str,fw-strw,y);
	}

	public void keyPressedGameMain(int keycode) {
		// TODO Auto-generated method stub
		if (keycode == KeyEvent.VK_SPACE){spkey = true;}
		if (keycode == KeyEvent.VK_P)	{pkey = true;}
		if (keycode == KeyEvent.VK_R)	{rkey = true;}
		if (keycode == KeyEvent.VK_N)	{nkey = true;}
		if (keycode == KeyEvent.VK_LEFT)	{leftkey = true;}
		if (keycode == KeyEvent.VK_RIGHT)	{rightkey = true;}
		
	}

	public void keyReleasedGameMain(int keycode) {
		// TODO Auto-generated method stub
		if (keycode == KeyEvent.VK_SPACE){spkey = false;}
		if (keycode == KeyEvent.VK_P)	{pkey = false;}
		if (keycode == KeyEvent.VK_R)	{rkey = false;}
		if (keycode == KeyEvent.VK_N)	{nkey = false;}
		if (keycode == KeyEvent.VK_LEFT)	{leftkey = false;;}
		if (keycode == KeyEvent.VK_RIGHT)	{rightkey = false;}
		
	}

}