package gameplay;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Rectangle;

import gameframework.base.Drawable;
import gameframework.base.Overlappable;
import gameframework.game.GameEntity;
import gameframework.game.GameMovableDriverDefaultImpl;
import gameframework.game.GameMovableEphemeral;
import gameframework.game.SpriteManagerDefaultImpl;

public class Projectile extends GameMovableEphemeral implements Drawable, GameEntity, Overlappable{
	private final SpriteManagerDefaultImpl spriteManager;
	public static final int RENDERING_SIZE = 16;
	
	public Projectile(Canvas canvas) {
		spriteManager = new SpriteManagerDefaultImpl("images/shuriken.gif",
				canvas, RENDERING_SIZE, 6);
		spriteManager.setTypes("down");
		setDisappearTimer(5);
	}	

	public void draw(Graphics g) {
		spriteManager.draw(g, getPosition());
	}

	@Override
	public Rectangle getBoundingBox() {
		return (new Rectangle(0, 0, RENDERING_SIZE, RENDERING_SIZE));
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		if(getSpeedVector().getSpeed() > 0) spriteManager.increment();
		else{
			spriteManager.reset();
			decrementDisappearTimer();
		}
		System.out.println("Projectile pos: " + getPosition());
	}

	public void stop(){
		this.setDriver(new GameMovableDriverDefaultImpl());
	}
	
}
