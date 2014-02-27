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
import utils.Observer;

public class Goblin extends GameMovable implements Drawable, GameEntity,
		Overlappable, Shooter {
	protected static DrawableImage image = null;
	protected boolean movable = true;
	protected int afraidTimer = 0;
	protected int maxAfraidTimer = 0;
	public int shootTimer = 0;
	protected boolean active = true;
	private final SpriteManagerDefaultImpl spriteManager;
	public static final int RENDERING_SIZE = 32;
	public static int SHOOT_INTERVAL = 15;
	protected Point fireDirection = null;
	private Observer<Shooter> levelObserver;
	
	public Goblin(Canvas defaultCanvas) {
		spriteManager = new SpriteManagerDefaultImpl("images/goblin.gif",
				defaultCanvas, RENDERING_SIZE, 6);
		spriteManager.setTypes(
				//
				"left",
				"right",
				"up",
				"down",//
				"beginAfraid-left",
				"beginAfraid-right",
				"beginAfraid-up",
				"beginAfraid-down", //
				"endAfraid-left", "endAfraid-right",
				"endAfraid-up",
				"endAfraid-down", //
				"inactive-left", "inactive-right", "inactive-up",
				"inactive-down", //
				"unused");
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
		String spriteType = "";
		Point pos = getPosition();
		Point target = getFireDirection();
		movable = true;

		if (!isActive()) {
			spriteType = "inactive-";
		} else if (afraidTimer > maxAfraidTimer / 2) {
			spriteType = "beginAfraid-";
		} else if (isAfraid()) {
			spriteType = "endAfraid-";
		}

		if (target.getX() < pos.getX()) {
			spriteType += "left";
		} else if (target.getX() > pos.getX()) {
			spriteType += "right";
		} else if (target.getY() < pos.getY()) {
			spriteType += "up";
		} else {
			spriteType += "down";
		}

		spriteManager.setType(spriteType);
		spriteManager.draw(g, getPosition());
	}

	@Override
	public void oneStepMoveAddedBehavior() {
		if(isAfraid()) afraidTimer--;
		else shoot();
		spriteManager.increment();
	}

	public Rectangle getBoundingBox() {
		return (new Rectangle(0, 0, RENDERING_SIZE, RENDERING_SIZE));
	}

	public void shoot() {
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

	public void register(Observer<Shooter> ob) {
		levelObserver = ob;
	}

	@Override
	public void unregister(Observer<Shooter> ob) {
		levelObserver = ob;
	}

	@Override
	public void notify(Shooter s) {
		levelObserver.update(this);
	}



}
