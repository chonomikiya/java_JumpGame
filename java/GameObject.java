package kdg.chono;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * ゲームで使うオブジェクトのスーパークラス
 * @author chono
 * @see Goal
 * @see Ground
 * @see Player
 * @see Wall
 */
public abstract class GameObject {
	void log(String str) {
		Mylogger.logger(str);
	}
	
	public int chara_x, chara_y;
	int image_x,image_y, image_w, image_h;
	int atari_w, atari_h;
	boolean hitboxlife = false;
	
	BufferedImage image1;
	GameObject(int x,int y, BufferedImage img,
			int ix, int iy, int iw, int ih){
		chara_x = x;	chara_y = y;
		image1 = img;
		image_x = ix;	image_y = iy;	image_w = iw;	image_h = ih;
	}
	/**
	 * 描画メソッド
	 * @param g Graphics 描画に使う
	 * @param io ImageObserver
	 */
	public void draw(Graphics g, ImageObserver io) {
		g.drawImage(image1,  chara_x, chara_y, chara_x+image_w,  chara_y+image_h,
				image_x, image_y, image_x+image_w,  image_y+image_h, io);
		move();
	}
	public void hitbox(Player myP) {
		//foot
		if((getx1() <= myP.getx1() && myP.getx1() < getx2())
			|| (getx1() < myP.getx2() && myP.getx2() <= getx2())
			|| (myP.getx1() == getx1() && myP.getx2() == getx2())) { 
			//ブロックの間に入れるようにするための処理
			//通常処理
			if(myP.gety2() <= gety1() && (myP.gety2()+myP.elapsed_time) >= gety1()) {
				myP.chara_y = gety1() -32;
				myP.elapsed_time = 0;
				myP.jump_judgement = true;
			}
		}
		//left&right
		if((myP.gety1() > gety1() && myP.gety1() < gety2())
			||(myP.gety2() < gety2() && myP.gety2() > gety1())
			||((myP.gety1()+1) > gety1() && (myP.gety2()-1 )< gety2())
			) {
			
			if((getx1() < myP.getx1() && myP.getx1() < getx2())){
				myP.chara_x = getx2();
				Mylogger.logger("myP.chara_x = getx2();");
			}
			if((getx1() < myP.getx2() && myP.getx2() < getx2())) {
				myP.chara_x = getx1() -32;
				Mylogger.logger("myP.chara_x = getx1() -32;");
			}
		}

		//head
		if((getx1() < myP.getx1() && myP.getx1() < getx2() )
				|| (getx1() < myP.getx2() && myP.getx2() < getx2())
				|| (myP.getx1()+1 )> getx1() && (myP.getx2()-1 )< getx2()) {
			if((myP.gety1() >= gety2() )
					&& 
					(myP.gety1()+myP.elapsed_time < gety2())
					) {
				//ブロックの処理順によって引っかかる事応急処置
				if((myP.getx1()+8 == getx2()) || (myP.getx2()-8 == getx1()))	return;
				//通常処理
				myP.chara_y = gety2();
				myP.elapsed_time = 0;
				Mylogger.logger("stop");
				Mylogger.logger(myP.getx1());
				Mylogger.logger(myP.gety1());
				Mylogger.logger(getx1());
				Mylogger.logger(gety1());

			}
		}

	}
	
	public int getx1()	{
		return chara_x;
		}
	public int getx2()	{
		return chara_x+32;
		}
	public int gety1()	{
		return chara_y;
		}
	public int gety2()	{
		return chara_y+32;
		}
	public void scroll() {
		chara_y += 32*17;
	}
	
	public abstract void move();
//	public abstract void gravity(boolean space);
}
