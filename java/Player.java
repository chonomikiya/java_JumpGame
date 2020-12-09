package kdg.chono;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;


public  class Player extends GameObject {
	boolean initdraw = false;
	static int JUMP = -32;
	static int GRAVITY = 4;
	int elapsed_time = 0;
	int foot_y = 640;
	boolean foothold = false;
	boolean jump_judgement = true;
//	boolean overhead = false;
	boolean goal = false;
	boolean seLanding = false;
	
	

	public Player(int x, int y, BufferedImage img) {
		super(x, y, img, 0, 0, 64, 32);
		// TODO Auto-generated constructor stub
	}
	public void draw(Graphics g, ImageObserver io) {
		if(initdraw == false) { 
			//left draw
		g.drawImage(image1,  chara_x, chara_y, chara_x+32,  chara_y+image_h,
				image_x, image_y, image_x+32, image_y+image_h, io);
		}else {
			//right draw
			g.drawImage(image1,  chara_x, chara_y, chara_x+32,  chara_y+image_h,
					image_x+32, image_y, image_x+32+32, image_y+image_h, io);
		}
		move();
	}
	public void gravity( boolean space) {
		
		if(jump_judgement == false)	elapsed_time += GRAVITY;
		if(jump_judgement == true)	elapsed_time = 0;

		chara_y += elapsed_time ;
		
	}
	public void move( boolean left, boolean right, boolean space ) {
		if (chara_y < 0)	chara_y = 0;
		if (chara_x < 0)	chara_x = 0;

		if (chara_x > GameBase.SCREEN_W - 32)
							chara_x = GameBase.SCREEN_W - 32;
		if (left == true)	{
			chara_x -= 8;
			initdraw = false;
			}
		if (right == true)	{
			chara_x += 8;
			initdraw = true;
			}
		if(space == true && jump_judgement == true) {
			elapsed_time = JUMP;
			jump_judgement = false;
			Mylogger.logger("jump");
		}
	}
	public void hitbox(GameObject gameobj) {
	}
	
	public boolean nextstage() {
		if(jump_judgement == true && chara_y < 64)
			return true;
		return false;
	}

	
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

}
