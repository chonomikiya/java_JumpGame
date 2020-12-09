package kdg.chono;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;

/**
 * mainクラス
 * @author chono
 * @version 1.0
 * @see GameBaseMethod
 * 
 */
public class GameBase extends GameBaseMethod {


	public static final int SCREEN_W = 480;
	public static final int SCREEN_H = 640;

	protected static final int GAMETITLE = 0;
	protected static final int GAMEMAIN = 2;
	protected static final int GAMECLEAR = 3;
	protected static final int GAMEOVER = 4;

	public BufferStrategy bstrategy;

	public BufferedImage backimage, charaimage, gameoverimage;

	public BufferedReader stage1, stage2, stage3;

	public Clip seFall, seORB, seWind, seLanding;

	Player player;
	/**
	 * ステージの管理に使う
	 */
	ArrayList<GameObject> stagelist = new ArrayList<GameObject>();

	protected int gamestate;

	int stagevalue = 1;
	int loadtime = 0;

	/**
	 * フレームの初期化と各種画像とBGMの読み込みを行う
	 * 
	 */
	public GameBase() {
		frame1 = new JFrame("title");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setBackground(Color.WHITE);
		frame1.setResizable(false);

		frame1.setVisible(true);
		Insets insets = frame1.getInsets();
		frame1.setSize(SCREEN_W + insets.left + insets.right, SCREEN_H + insets.top + insets.bottom);
		frame1.setLocationRelativeTo(frame1);

		frame1.createBufferStrategy(2);
		bstrategy = frame1.getBufferStrategy();
		frame1.setIgnoreRepaint(true);

		frame1.addKeyListener(new MyKeyAdapter());

		Timer t = new Timer();
		t.schedule(new MyTimerTask(), 10, 30);

		midiReader("stageBGM.midi");
		charaimage = ImageReader("java2D.png");
		backimage = ImageReader("backimage.jpg");
		gameoverimage = ImageReader("car_scrap.png");
		seFall = soundReader("hyun1.wav");
		seORB = soundReader("seORBget.wav");
		seWind = soundReader("wind.wav");
		seLanding = soundReader("landing1.wav");
		stage1 = StageReader("stage1.csv");
		stage2 = StageReader("stage2.csv");
		stage3 = StageReader("stage3.csv");
		goGameTitle();

	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GameBase();
	}

	/**
	 * ステージのarraylistの削除とplayerの座標の調整を行う
	 * @see initStage()
	 */
	public void nextStage() {
		stagelist.clear();
		player.scroll();
		if (stagevalue == 3) {
			seWind.loop(Clip.LOOP_CONTINUOUSLY);
		}
		initStage();
	}

	/**
	 * csvファイルからマップを読み込みarraylistにマップ情報を追加するメソッド
	 * @see Stage
	 * 
	 */
	public void initStage() {
		int array_stagedate[][] = new int[20][15];

		switch (stagevalue) {
		case 1:
			stage1 = StageReader("stage1.csv");
			array_stagedate = Stage.Stageset(stage1);
			stagevalue++;
			break;
		case 2:
			stage2 = StageReader("stage2.csv");
			array_stagedate = Stage.Stageset(stage2);
			stagevalue++;
			break;
		case 3:
			stage3 = StageReader("stage3.csv");
			array_stagedate = Stage.Stageset(stage3);
			break;

		}
		for (int i = 0; i < array_stagedate.length; i++) {
			for (int j = 0; j < array_stagedate[i].length; j++) {
				if (array_stagedate[i][j] == 1)
					stagelist.add(new Wall(32 * j, 32 * i, charaimage));
				if (array_stagedate[i][j] == 2)
					stagelist.add(new Ground(32 * j, 32 * i, charaimage));
				if (array_stagedate[i][j] == 3)
					stagelist.add(new Goal(32 * j, 32 * i, charaimage));
			}
		}
	}


	/**
	 * gamestateをrunGameTitleに変更するメソッド
	 * @see MyTimerTask
	 * @see runGameTitle
	 */
	
	public void goGameTitle() {
		gamestate = GAMETITLE;
	}
	/**
	 * gamestateをrunGameMainに変更するメソッド
	 * @see MyTimerTask
	 * @see runGameMain
	 */
	public void goGameMain() {
		stagevalue = 1;
		player = new Player(SCREEN_W, SCREEN_H - 68, charaimage);
		spkey = false;
		pkey = false;
		rkey = false;
		leftkey = false;
		rightkey = false;
		stagelist.clear();
		midiseq.setTickPosition(0);
		midiseq.start();
		initStage();
		gamestate = GAMEMAIN;
	}
	/**
	 * gamestateをrunGameClearに変更するメソッド
	 * @see MyTimerTask
	 */
	private void goGameClear() {
		gamestate = GAMECLEAR;
	}
	/**
	 * gamestateをrunGameOverに変更するメソッド
	 * @see MyTimerTask
	 */
	private void goGameOver() {
		gamestate = GAMEOVER;
	}

	/**
	 * gamestateがGAMETITLEの時に実行するメソッド
	 * @param g Graphics 描画に使う
	 * @see MyTimerTask
	 * @see nextStage()
	 */
	public void runGameTitle(Graphics g) {
		g.drawImage(backimage, 0, 0, frame1);
		g.setColor(Color.BLACK);
		g.setFont(new Font("SansSerif", Font.BOLD, 50));
		drawStringCenter("JUMP GAME", 200, g);
		if (loadtime < 61) {
			loadtime++;
		} else {
			g.setFont(new Font("SansSerif", Font.BOLD, 30));
			drawStringCenter("Pキーでスタート", 270, g);
			drawStringCenter("操作説明", 330, g);
			g.setFont(new Font("SansSerif", Font.BOLD, 20));
			drawStringCenter("SPACEキーでジャンプ", 360, g);
			drawStringCenter("← → キーで移動", 390, g);
			drawStringCenter("ブロックを登って上に行く", 450, g);

		}
		if (loadtime < 20) {
			if (loadtime == 1) {
				initStage();
			}
			for (int i = 0; i < stagelist.size(); i++) {
				stagelist.get(i).draw(g, frame1);
			}
		} else if (loadtime < 40) {
			if (loadtime == 20) {
				stagelist.clear();
				initStage();
			}
			for (int i = 0; i < stagelist.size(); i++) {
				stagelist.get(i).draw(g, frame1);
			}
		} else if (loadtime < 60) {
			if (loadtime == 40) {
				stagelist.clear();
				initStage();
			}
			for (int i = 0; i < stagelist.size(); i++) {
				stagelist.get(i).draw(g, frame1);
			}
		}

	}

	/**
	 * gamestateがGAMEOVERの時に実行するメソッド
	 * @param g Graphics 描画に使う
	 */
	public void runGameOver(Graphics g) {
		g.drawImage(backimage, 0, 0, frame1);
		g.drawImage(gameoverimage, (SCREEN_W / 2) - (gameoverimage.getWidth() / 2), SCREEN_H / 2, frame1);
		g.setColor(Color.BLACK);
		g.setFont(new Font("SansSerif", Font.BOLD, 50));
		drawStringCenter("GAME OVER", 200, g);
		g.setFont(new Font("SansSerif", Font.BOLD, 30));
		drawStringCenter("Rキーでタイトルに戻る", 240, g);
	}

	/**
	 * gamestateがGAMEMAINの時に実行するメソッド
	 * @param g Graphics 描画に使う
	 * @see MyTimerTask
	 * @see nextStage()
	 * 
	 */
	public void runGameMain(Graphics g) {
		g.fillRect(0, 0, 480, 640);
		g.drawImage(backimage, 0, 0, frame1);

		// 初期化前にplayerの着地判定を保存
		player.seLanding = player.jump_judgement;

		player.move(leftkey, rightkey, spkey);
		// playerの地面判定を初期化
		player.jump_judgement = false;

		for (int i = 0; i < stagelist.size(); i++) {
			stagelist.get(i).draw(g, frame1);
		}
		for (int i = 0; i < stagelist.size(); i++) {
			stagelist.get(i).hitbox(player);
		}

		player.gravity(spkey);
		player.draw(g, frame1);
		// playerが空中から地面に着地した時にseを再生する
		if (player.seLanding == false && player.jump_judgement == true) {
			seLanding.setFramePosition(0);
			seLanding.start();
		}
		if (player.nextstage() == true) {
			nextStage();
		}
		if (player.goal == true) {
			player.goal = false;
			midiseq.stop();
			seWind.stop();
			seWind.flush();
			seORB.setFramePosition(0);
			seORB.start();
			goGameClear();
		}
		if (player.chara_y > SCREEN_H) {
			midiseq.stop();
			seWind.stop();
			seWind.flush();
			seFall.setFramePosition(0);
			seFall.start();
			goGameOver();
		}
	}

	/**
	 * gamestateがGAMEOVERの時に実行するメソッド
	 * @param g Graphics 描画に使う
	 * @see MyTimerTask
	 */
	public void runGameClear(Graphics g) {
		g.fillRect(0, 0, 480, 640);
		g.drawImage(backimage, 0, 0, frame1);
		g.setColor(Color.BLACK);
		g.setFont(new Font("SansSerif", Font.BOLD, 60));
		drawStringCenter("GAME CLEAR", 230, g);

		g.setFont(new Font("SansSerif", Font.BOLD, 30));
		g.drawString("Rキーでタイトルに戻る", 160, 290);
	}

	/**
	 * リアルタイム処理と呼び出すメソッドの管理
	 * 
	 *
	 */
	class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			Graphics g = bstrategy.getDrawGraphics();
			if (bstrategy.contentsLost() == false) {
				Insets insets = frame1.getInsets();
				g.translate(insets.left, insets.top);

				switch (gamestate) {
				case GAMETITLE:
					runGameTitle(g);
					break;
				case GAMECLEAR:
					runGameClear(g);
					break;
				case GAMEMAIN:
					runGameMain(g);
					break;
				case GAMEOVER:
					runGameOver(g);
					break;
				}
				bstrategy.show();
				g.dispose();
			}
		}
	}

	/**
	 * キーボードの入力を受け取るクラス
	 */
	class MyKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent ev) {
			if (gamestate == GAMEMAIN) {
				keyPressedGameMain(ev.getKeyCode());
			}
		}

		public void keyReleased(KeyEvent ev) {
			int keycode = ev.getKeyCode();

			switch (gamestate) {
			case GAMETITLE:
				if (keycode == KeyEvent.VK_P && loadtime > 60)
					goGameMain();
				break;
			case GAMEMAIN:
				keyReleasedGameMain(keycode);
				break;
			case GAMEOVER:
				if (keycode == KeyEvent.VK_R)
					goGameTitle();
				break;
			case GAMECLEAR:
				if (keycode == KeyEvent.VK_R)
					goGameTitle();
				break;
			}
		}
	}

}