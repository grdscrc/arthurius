package characters;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import gameframework.base.Drawable;
import gameframework.base.Overlappable;
import gameframework.game.GameEntity;
import gameframework.game.GameMovable;
import gameframework.game.SpriteManager;
import gameframework.game.SpriteManagerDefaultImpl;
import utils.Observable;
import utils.Observer;
import utils.army.AgeFactory;
import utils.army.ArmedUnit;
import utils.army.InfantryMan;
import utils.army.Soldier;
import utils.army.VisitorClassicForArmedUnit;
import utils.army.VisitorFunForArmedUnit;

public class Arthurius extends GameMovable implements Drawable, GameEntity, Overlappable, ArmedUnit, Observable<Arthurius>
{
	
	private static Arthurius _singleton=null;
	protected Soldier soldier;
	protected List<String> equipments = new ArrayList<String>();
	
	protected final SpriteManager spriteManager;
	public static final int RENDERING_SIZE = 32;
	protected boolean movable = true;
	protected int vulnerableTimer = 0;
	private ArrayList<Observer<Arthurius>> observers = new ArrayList<Observer<Arthurius>>();	

	private Arthurius(String name,Canvas defaultCanvas) {
		soldier=new InfantryMan("Arthurius");
		spriteManager = new SpriteManagerDefaultImpl("images/pac1.gif",
				defaultCanvas, RENDERING_SIZE, 6);
		spriteManager.setTypes(
				"right",
				"left",
				"up",
				"down",
				"static");
	}
	
	
	public static synchronized Arthurius getInstance(Canvas defaultCanvas){
		if (_singleton==null)
			return new Arthurius("Arthurius",defaultCanvas);
		else
			return _singleton;
	}

	public static synchronized Arthurius getInstance() throws Exception{
		if (_singleton==null)
			throw new Exception("Singleton not yet instantiated");
		else
			return _singleton;
	}


	public void setInvulnerable(int timer) {
		vulnerableTimer = timer;
	}

	public boolean isVulnerable() {
		return (vulnerableTimer <= 0);
	}

	public void draw(Graphics g) {
		String spriteType = "";
		Point tmp = getSpeedVector().getDirection();
		movable = true;
//		if (!isVulnerable()) {
//			spriteType += "invulnerable-";
//		}

		if (tmp.getX() == 1) {
			spriteType += "right";
		} else if (tmp.getX() == -1) {
			spriteType += "left";
		} else if (tmp.getY() == 1) {
			spriteType += "down";
		} else if (tmp.getY() == -1) {
			spriteType += "up";
		} else {
			spriteType = "static";
			spriteManager.reset();
			movable = false;
		}
		spriteManager.setType(spriteType);
		spriteManager.draw(g, getPosition());

	}

	@Override
	public void oneStepMoveAddedBehavior() {
		if (!isVulnerable()) {
			vulnerableTimer--;
			spriteManager.blink();
		}
		if (movable) {
			spriteManager.increment();
			if(getSpeedVector().getSpeed() > 0)
				notify(this);
		}
	}

	public Rectangle getBoundingBox() {
		return (new Rectangle(0, 0, RENDERING_SIZE, RENDERING_SIZE));
	}

	public void addEquipment(String equipmentType) {
		if (alive()) { // XXX "else" not treated
			if (equipments.contains(equipmentType)) {
				return; // decoration not applied
			} else {
				try {
					equipments.add(equipmentType);
				} catch (Exception e) {
					throw new RuntimeException("Unknown equipment type "
							+ e.toString());
				}
			}
		}
	}

	public String getName() {
		return soldier.getName();
	}

	public float getHealthPoints() {
		return soldier.getHealthPoints();
	}

	public AgeFactory getAge() {
		return null;
	}

	public boolean alive() {
		return soldier.alive();
	}

	public void heal() {
		soldier.heal();
	}

	public float strike() {
		return soldier.strike();
	}

	public boolean parry(float force) {
		return soldier.parry(force);
	}

	public void accept(VisitorClassicForArmedUnit v) {
		v.visit(this);
	}

	public <T> T accept(VisitorFunForArmedUnit<T> v) {
		return v.visit(this);
	}

	//Observable by goblins
	@Override
	public void register(Observer<Arthurius> ob) {
		observers.add(ob);
	}

	@Override
	public void unregister(Observer<Arthurius> ob) {
		observers.remove(ob);
	}

	@Override
	public void notify(Arthurius s) {
		for(Observer<Arthurius> obs : observers)
			obs.update(this);
	}

}
