package characters;

import gameframework.base.Drawable;
import gameframework.base.DrawableImage;
import gameframework.base.Overlappable;
import gameframework.game.GameEntity;
import gameframework.game.GameMovable;
import gameframework.game.SpriteManagerDefaultImpl;
import gameplay.Shooter;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import utils.Observer;

public class Wizard extends GameMovable implements Drawable, GameEntity,
		Overlappable, Shooter {
	protected static DrawableImage image = null;
	protected boolean movable = true;
	protected int afraidTimer = 0;
	protected int maxAfraidTimer = 0;
	public static int shootTimer = 0;
	protected boolean active = true;
	private final SpriteManagerDefaultImpl spriteManager;
	public static final int RENDERING_SIZE = 150;
	public static int SHOOT_INTERVAL = 3;
	
	public Point fireDirection = new Point(0,0);
	private ArrayList<Observer<Shooter>> observers = new ArrayList<Observer<Shooter>>();
	
	public Wizard(Canvas defaultCanvas) {
		spriteManager = new SpriteManagerDefaultImpl("images/sim01.gif",
				defaultCanvas, RENDERING_SIZE, 1);
		spriteManager.setTypes("down");
	}

	public boolean isAfraid() {
		return afraidTimer > 0;
	}

	public void setAfraid(int timer) {
		maxAfraidTimer = afraidTimer = timer;
	}

	public boolean isActive() {
		return active;
	}

	public void setAlive(boolean aliveState) {
		active = aliveState;
	}

	public void draw(Graphics g) {
//		String spriteType = "";
//		Point tmp = getSpeedVector().getDirection();

//		if (!isActive()) {
//			spriteType = "inactive-";
//		} else if (afraidTimer > maxAfraidTimer / 2) {
//			spriteType = "beginAfraid-";
//		} else if (isAfraid()) {
//			spriteType = "endAfraid-";
//		}

		spriteManager.setType("down");
		spriteManager.draw(g, getPosition());
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		if(!isAfraid()) shoot();
	}

	public Rectangle getBoundingBox() {
		return (new Rectangle(0, 0, RENDERING_SIZE, RENDERING_SIZE));
	}

	public void shoot() {
		if(isAfraid()) return;
		
		if(shootTimer > 0) shootTimer--;
		else {
			notify(this);
			shootTimer = SHOOT_INTERVAL;
		}
	}

	@Override
	public Point getFireDirection() {
		return fireDirection;
	}

	@Override
	public void setFireDirection(Point p) {
		fireDirection = p;
	}

	@Override
	public void register(Observer<Shooter> ob) {
		observers.add(ob);
	}

	@Override
	public void unregister(Observer<Shooter> ob) {
		observers.remove(ob);
	}

	@Override
	public void notify(Shooter s) {
		for(Observer<Shooter> o : observers){
			o.update(this);
		}
	}
}
