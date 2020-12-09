package kdg.chono;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class Goal extends GameObject {
	static double count = 1;
	Goal(int x, int y, BufferedImage img) {
		super(x, y, img, 0, 32*3, 32*8, 32);
		// TODO Auto-generated constructor stub
	}
	public void draw(Graphics g, ImageObserver io) {
		int drawint = image();
		g.drawImage(image1,  chara_x, chara_y, chara_x+32,  chara_y+image_h,
				image_x+drawint, image_y, image_x+drawint+32 , image_y+image_h, io);
		move();
	}
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}
	public void hitbox(Player myP) {
		if(myP.getx2() > getx1() && getx2() > myP.getx1() &&
				myP.gety2() > gety1() && gety2() > myP.gety1() ) {
			myP.goal = true;
		}
	}
	
	public int image() {
		count+=0.5;
		if(count > 8)	count = 1;
		return 32*(int)count;
	}

}
