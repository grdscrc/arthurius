package levels;

import java.awt.Canvas;
import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import characters.Arthurius;
import characters.Goblin;
import characters.Wizard;
import gameframework.base.MoveStrategyDefaultImpl;
import gameframework.base.MoveStrategyStraightLine;
import gameframework.game.Game;
import gameframework.game.GameEntity;
import gameframework.game.GameLevelDefaultImpl;
import gameframework.game.GameMovableDriverDefaultImpl;
import gameframework.game.MoveBlockerChecker;
import gameframework.game.MoveBlockerCheckerDefaultImpl;
import gameplay.Boots;
import gameplay.Door;
import gameplay.Jail;
import gameplay.MoveBlockers;
import gameplay.Mud;
import gameplay.OverlapRules;
import gameplay.Projectile;
import gameplay.Shooter;
import gameplay.SuperCandy;
import gameplay.Wall;
import gameplay.Water;
import utils.Observer;

public abstract class GameLevel extends GameLevelDefaultImpl implements Observer<Shooter>{
	Canvas canvas;

	public static final int SPRITE_SIZE = 16;
	
	protected Arthurius _Arthurius;
	protected OverlapRules overlapRules;
	protected MoveBlockerChecker moveBlockerChecker = new MoveBlockerCheckerDefaultImpl();

	public GameLevel(Game g) {
		super(g);
	}
	
	protected void createGoblins(List<Point> goblinsPos, List<Point> goblinsTargets){
		assert(goblinsPos.size() == goblinsTargets.size());
		
		Goblin goblin;
		for (int t = 0; t < goblinsPos.size(); t++) {
            GameMovableDriverDefaultImpl goblinDriver = new GameMovableDriverDefaultImpl();
            MoveStrategyDefaultImpl strat = new MoveStrategyDefaultImpl();
            goblinDriver.setStrategy(strat);
            goblinDriver.setmoveBlockerChecker(moveBlockerChecker);
            goblin = new Goblin(canvas);
            goblin.setDriver(goblinDriver);
            goblin.setPosition(goblinsPos.get(t));
            goblin.setFireDirection(goblinsTargets.get(t));
            universe.addGameEntity(goblin);
            overlapRules.addGoblin(goblin);
            goblin.register(this);
		}
	}

	protected void fillUniverse(int[][] tab){
		// Empty the universe from what was already here
		Iterator<GameEntity> entities = universe.gameEntities();
		GameEntity entity;
		while(entities.hasNext()){
			entity = entities.next();
			universe.removeGameEntity(entity);
		}
		
		// Filling up the universe with basic non movable entities and inclusion in the universe
		for (int i = 0; i < 31; ++i) {
			for (int j = 0; j < 28; ++j) {
				if (tab[i][j] == 0) {
					universe.addGameEntity(new Boots(canvas, new Point(j * SPRITE_SIZE, i * SPRITE_SIZE)));
				}
				if (tab[i][j] == 1) {
					universe.addGameEntity(new Wall(canvas, j * SPRITE_SIZE, i * SPRITE_SIZE));
				}
				if (tab[i][j] == 2) {
					universe.addGameEntity(new SuperCandy(canvas, new Point(j * SPRITE_SIZE, i * SPRITE_SIZE)));
				}
				if (tab[i][j] == 4) {
					universe.addGameEntity(new Jail(new Point(j * SPRITE_SIZE, i * SPRITE_SIZE)));
				}
				if (tab[i][j] == 6) {
					universe.addGameEntity(new Mud(canvas, new Point(j * SPRITE_SIZE, i * SPRITE_SIZE)));
				}
				if (tab[i][j] == 7) {
					universe.addGameEntity(new Water(canvas, j * SPRITE_SIZE, i * SPRITE_SIZE));
				}
				if (tab[i][j] == 8) {
					universe.addGameEntity(new Door(canvas, new Point(j * SPRITE_SIZE, i * SPRITE_SIZE)));
				}
			}
		}
	}
	
	@Override
	public void update(Shooter shooter) {
		if(shooter instanceof Goblin){
			GameMovableDriverDefaultImpl pDriv = new GameMovableDriverDefaultImpl();
			MoveStrategyStraightLine strat = new MoveStrategyStraightLine(shooter.getPosition(), shooter.getFireDirection());
			moveBlockerChecker.setMoveBlockerRules(new MoveBlockers());
			pDriv.setStrategy(strat);
			pDriv.setmoveBlockerChecker(moveBlockerChecker);
			Projectile p = new Projectile(canvas, shooter);
			p.setDriver(pDriv);
			p.setPosition(shooter.getPosition());
			universe.addGameEntity(p);
		}
		else if(shooter instanceof Wizard){
			GameMovableDriverDefaultImpl projectileDriver = new GameMovableDriverDefaultImpl();
			MoveStrategyStraightLine strat = new MoveStrategyStraightLine(shooter.getPosition(), _Arthurius.getPosition());
			moveBlockerChecker.setMoveBlockerRules(new MoveBlockers());
			projectileDriver.setStrategy(strat);
			projectileDriver.setmoveBlockerChecker(moveBlockerChecker);
			Projectile projectile = new Projectile(canvas, shooter);
			projectile.setDriver(projectileDriver);
			projectile.setPosition(shooter.getPosition());
			universe.addGameEntity(projectile);
		}
	}

}
