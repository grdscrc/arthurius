package gameplay;

import gameframework.base.ObservableValue;
import gameframework.base.MoveStrategyRandom;
import gameframework.base.Overlap;
import gameframework.base.Overlappable;
import gameframework.game.GameEntity;
import gameframework.game.GameMovableDriverDefaultImpl;
import gameframework.game.GameUniverse;
import gameframework.game.OverlapRulesApplierDefaultImpl;

import java.awt.Point;
import java.util.Vector;

import characters.Arthurius;
import characters.Wizard;
import characters.Goblin;


public class OverlapRules extends OverlapRulesApplierDefaultImpl {
	protected GameUniverse universe;
	protected Vector<Goblin> vGoblins = new Vector<Goblin>();
	protected GameEntity boss;

	// Time duration during which pacman is invulnerable and during which ghosts
	// can be eaten (in number of cycles)
	static final int INVULNERABLE_DURATION = 10;
	static final int BERZERK_DURATION = 20;
	static final int REPULSION_SHIFT_MUD = 5;
	static final int REPULSION_SHIFT_ENEMY = 20;
	protected Point pacManStartPos;
	protected Point ghostStartPos;
	protected boolean manageHeroDeath;
	private final ObservableValue<Integer> score;
	private final ObservableValue<Integer> life;
	private final ObservableValue<Boolean> levelCompleted;
	private final ObservableValue<Boolean> gameOver;
	private int totalNbGums = 0;
	private int nbEatenGums = 0;
	private ObservableValue<String>[] stuff;

	public OverlapRules(Point pacPos, Point gPos,
			ObservableValue<Integer> life, ObservableValue<Integer> score,
			ObservableValue<String>[] stuff, ObservableValue<Boolean> endOfGame,
			ObservableValue<Boolean> gameOver) {
		pacManStartPos = (Point) pacPos.clone();
		ghostStartPos = (Point) gPos.clone();
		this.life = life;
		this.score = score;
		this.levelCompleted = endOfGame;
		this.gameOver = gameOver;
		this.stuff = stuff;
	}

	public void setUniverse(GameUniverse universe) {
		this.universe = universe;
	}

	public void setTotalNbGums(int totalNbGums) {
		this.totalNbGums = totalNbGums;
	}

	public void addGoblin(Goblin g) {
		vGoblins.addElement(g);
	}

	public void addBoss(Wizard w) {
		boss = w;
	}

	public void applyOverlapRules(Vector<Overlap> overlappables) {
		manageHeroDeath = true;
		super.applyOverlapRules(overlappables);
	}

	public void overlapRule(Arthurius arthurius, Goblin g) {
		repulseHeroHandler(arthurius, g, REPULSION_SHIFT_ENEMY);
		if (arthurius.isVulnerable()) {
			heroHurtHandler(arthurius);
		}
	}

	public void overlapRule(Arthurius arthurius, Projectile g) {
		if (!arthurius.isVulnerable()) {
			universe.removeGameEntity(g);
		} else {
			heroHurtHandler(arthurius);
		}
	}

	public void overlapRule(Goblin g, SuperCandy spg) {
	}

	public void overlapRule(Goblin g, Boots spg) {
	}

	public void overlapRule(Goblin g, TeleportPairOfPoints teleport) {
		g.setPosition(teleport.getDestination());
	}

	public void overlapRule(Arthurius a, TeleportPairOfPoints teleport) {
		a.setPosition(teleport.getDestination());
	}

	public void overlapRule(Goblin g, Jail jail) {
		if (!g.isActive()) {
			g.setAlive(true);
			MoveStrategyRandom strat = new MoveStrategyRandom();
			GameMovableDriverDefaultImpl ghostDriv = (GameMovableDriverDefaultImpl) g
					.getDriver();
			ghostDriv.setStrategy(strat);
			g.setPosition(ghostStartPos);
		}
	}

	public void overlapRule(Goblin g, Projectile p) {
		if(!g.equals(p.getShooter()))
			universe.removeGameEntity(p);
	}

	public void overlapRule(Arthurius arthurius, SuperCandy spg) {
		score.setValue(score.getValue() + 5);
		universe.removeGameEntity(spg);
		pacgumEatenHandler();
		arthurius.setInvulnerable(BERZERK_DURATION);
		for (Goblin goblin : vGoblins) {
			goblin.setAfraid(BERZERK_DURATION);
		}
	}

	public void overlapRule(Arthurius arthurius, Mud m) {
		if(stuff[0].getValue()=="Vide"){
			repulseHeroHandler(arthurius, m, REPULSION_SHIFT_MUD);
		}
	}

	public void overlapRule(Arthurius a, Door d) {
		levelCompleted.setValue(true);
	}

	public void overlapRule(Arthurius a, Sword s) {
		a.addEquipment("Epee");
		stuff[1].setValue("Epee");
		universe.removeGameEntity(s);	
	}

	public void overlapRule(Arthurius a, Wizard wizard) {
		if(stuff[1].getValue() == "Epee"){
			universe.removeGameEntity(wizard);
			levelCompleted.setValue(true);
		}
	}

	public void overlapRule(Arthurius a, Boots b) {
		a.addEquipment("Bottes");
		stuff[0].setValue("Bottes");
		universe.removeGameEntity(b);

	}

	private void pacgumEatenHandler() {
		nbEatenGums++;
		if (nbEatenGums >= totalNbGums) {
			levelCompleted.setValue(true);
		}
	}

	private void heroHurtHandler(Arthurius arthurius){
		if (manageHeroDeath) {
			life.setValue(life.getValue() - 1);

			if(life.getValue() == 0)
				gameOver.setValue(true);
			else
				arthurius.setInvulnerable(INVULNERABLE_DURATION);

			manageHeroDeath = false;
		}
	}

	private void repulseHeroHandler(Arthurius arthurius, Overlappable obstacle, int distance){
		Point newPosition = new Point(arthurius.getPosition());
		Point obstaclePos = obstacle.getPosition();		
		Point direction = arthurius.getSpeedVector().getDirection();

		if(direction.getX() != 0 && direction.getX() != 0)
			newPosition.translate((int) direction.getX() * -1, (int) direction.getY() * -1);
		else if(direction.getX() != 0)
			newPosition.translate((int) direction.getX() * -1, 0);

		arthurius.setPosition(newPosition);
	}
}
