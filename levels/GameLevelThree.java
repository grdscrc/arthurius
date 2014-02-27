package levels;

import gameframework.base.MoveStrategyKeyboard;
import gameframework.base.MoveStrategyStraightLine;
import gameframework.game.CanvasDefaultImpl;
import gameframework.game.Game;
import gameframework.game.GameLevelDefaultImpl;
import gameframework.game.GameMovableDriverDefaultImpl;
import gameframework.game.GameUniverseDefaultImpl;
import gameframework.game.GameUniverseViewPortDefaultImpl;
import gameframework.game.MoveBlockerChecker;
import gameframework.game.MoveBlockerCheckerDefaultImpl;
import gameframework.game.OverlapProcessor;
import gameframework.game.OverlapProcessorDefaultImpl;
import gameplay.Boots;
import gameplay.Door;
import gameplay.MoveBlockers;
import gameplay.Mud;
import gameplay.OverlapRules;
import gameplay.Projectile;
import gameplay.Shooter;
import gameplay.SuperCandy;
import gameplay.Sword;
import gameplay.TeleportPairOfPoints;
import gameplay.Wall;
import gameplay.Water;

import java.awt.Canvas;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import characters.Arthurius;
import characters.Wizard;
import characters.Goblin;
import utils.Observer;



public class GameLevelThree extends GameLevelDefaultImpl implements Observer<Shooter>  {
	Canvas canvas;
	
	// 0 : Botte; 1 : Walls; 2 : SuperPacgums; 3 : Doors; 4 : Jail; 5 : empty
	// 6: Mud 7:Water 8:Door 10: sword 
	// Note: teleportation points are not indicated since they are defined by
	// directed pairs of positions.
	static int[][] tab = { 
		    { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1, 1, 1, 1, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,10, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

	public static final int SPRITE_SIZE = 16;
	public static int NUMBER_OF_GOBLINS = 8;
	
	protected Vector<Goblin> vG = new Vector<Goblin>();
	
	protected Arthurius _Arthurius;
	protected static MoveBlockerChecker moveBlockerChecker = new MoveBlockerCheckerDefaultImpl();
	private OverlapRules r;

	@Override
	protected void init() {
		OverlapProcessor overlapProcessor = new OverlapProcessorDefaultImpl();

		moveBlockerChecker.setMoveBlockerRules(new MoveBlockers());
		
		
		OverlapRules overlapRules = new OverlapRules(new Point(14 * SPRITE_SIZE, 17 * SPRITE_SIZE),
				new Point(14 * SPRITE_SIZE, 15 * SPRITE_SIZE), life[0], score[0],stuff, levelCompleted, gameOver);
		overlapProcessor.setOverlapRules(overlapRules);

		universe = new GameUniverseDefaultImpl(moveBlockerChecker, overlapProcessor);
		overlapRules.setUniverse(universe);

		gameBoard = new GameUniverseViewPortDefaultImpl(canvas, universe);
		((CanvasDefaultImpl) canvas).setDrawingGameBoard(gameBoard);
		r = overlapRules;

		int totalNbGums = 0;
		
		// Filling up the universe with basic non movable entities and inclusion in the universe
		for (int i = 0; i < 31; ++i) {
			for (int j = 0; j < 28; ++j) {
				if (tab[i][j] == 0) {
					universe.addGameEntity(new Boots(canvas, new Point(j * SPRITE_SIZE, i * SPRITE_SIZE)));
					totalNbGums++;
				}
				if (tab[i][j] == 1) {
					universe.addGameEntity(new Wall(canvas, j * SPRITE_SIZE, i * SPRITE_SIZE));
				}
				if (tab[i][j] == 2) {
					universe.addGameEntity(new SuperCandy(canvas, new Point(j * SPRITE_SIZE, i * SPRITE_SIZE)));
					totalNbGums++;
				}
				if (tab[i][j] == 10) {
					universe.addGameEntity(new Sword(canvas, new Point(j * SPRITE_SIZE, i * SPRITE_SIZE)));
				}
				if (tab[i][j] == 6) {
					universe.addGameEntity(new Mud(canvas, new Point(j * SPRITE_SIZE, i * SPRITE_SIZE)));
					totalNbGums++;
				}
				if (tab[i][j] == 7) {
					universe.addGameEntity(new Water(canvas, j * SPRITE_SIZE, i * SPRITE_SIZE));
				}
				if (tab[i][j] == 8) {
					universe.addGameEntity(new Door(canvas, new Point(j * SPRITE_SIZE, i * SPRITE_SIZE)));
					totalNbGums++;
				}
			}
		}
		overlapRules.setTotalNbGums(totalNbGums);

		// Two teleport points definition and inclusion in the universe
		// (west side to east side)
		universe.addGameEntity(new TeleportPairOfPoints(new Point(0 * SPRITE_SIZE, 14 * SPRITE_SIZE), new Point(
				25 * SPRITE_SIZE, 14 * SPRITE_SIZE)));
		// (east side to west side)
		universe.addGameEntity(new TeleportPairOfPoints(new Point(27 * SPRITE_SIZE, 14 * SPRITE_SIZE), new Point(
				2 * SPRITE_SIZE, 14 * SPRITE_SIZE)));
		
		_Arthurius = Arthurius.getInstance(canvas);
		GameMovableDriverDefaultImpl arthDriver = new GameMovableDriverDefaultImpl();
		MoveStrategyKeyboard keyStr = new MoveStrategyKeyboard();
		arthDriver.setStrategy(keyStr);
		arthDriver.setmoveBlockerChecker(moveBlockerChecker);
		canvas.addKeyListener(keyStr);
		_Arthurius.setDriver(arthDriver);
		_Arthurius.setPosition(new Point(1 * SPRITE_SIZE, 1 * SPRITE_SIZE));
		universe.addGameEntity(_Arthurius);
			
		//Wizard time
		Wizard greatWizard;
		greatWizard = new Wizard(canvas);
		greatWizard.setPosition(		new Point(10 * SPRITE_SIZE,10 * SPRITE_SIZE));
		greatWizard.setFireDirection(	new Point( 9 * SPRITE_SIZE, 9 * SPRITE_SIZE));
		greatWizard.register(this);
		universe.addGameEntity(greatWizard);
		overlapRules.addBoss(greatWizard);
		
		//...and his goblin buddies
		List<Point> goblinsPos = new ArrayList<Point>();
		goblinsPos.add(new Point( 5 * SPRITE_SIZE, 6 * SPRITE_SIZE));
		goblinsPos.add(new Point( 6 * SPRITE_SIZE, 5 * SPRITE_SIZE));
		goblinsPos.add(new Point( 5 * SPRITE_SIZE,19 * SPRITE_SIZE));
		goblinsPos.add(new Point( 6 * SPRITE_SIZE,20 * SPRITE_SIZE));
		goblinsPos.add(new Point(19 * SPRITE_SIZE, 5 * SPRITE_SIZE));
		goblinsPos.add(new Point(20 * SPRITE_SIZE, 6 * SPRITE_SIZE));
		goblinsPos.add(new Point(19 * SPRITE_SIZE,20 * SPRITE_SIZE));
		goblinsPos.add(new Point(20 * SPRITE_SIZE,19 * SPRITE_SIZE));

		List<Point>goblinsFire = new ArrayList<Point>();
		int[][] fireDirs = new int[][] {
				{ 0, 1}, //down
				{ 1, 0}, //right
				{ 0,-1}, //up
				{ 1, 0}, //right
				{-1, 0}, //left
				{ 0, 1}, //down
				{-1, 0}, //left
				{ 0,-1}  //up
		};
		
		int index = 0;
		for(Point pos : goblinsPos){
			goblinsFire.add((Point) pos.clone());
			goblinsFire.get(goblinsFire.size()-1)
			.translate(fireDirs[index][0], fireDirs[index][1]);
			index++;
		}
		
//		Goblin myGoblin;
//		for (int t = 0; t < NUMBER_OF_GOBLINS; ++t) {
//			myGoblin = new Goblin(canvas);
//			myGoblin.setPosition(goblinsPos.get(t));
//			myGoblin.setFireDirection(goblinsFire.get(t));
//			myGoblin.register(this);
//			universe.addGameEntity(myGoblin);
//			(overlapRules).addGoblin(myGoblin);
//		}
	}

	public GameLevelThree(Game g) {
		super(g);
		canvas = g.getCanvas();
	}
	
	//Créer un projectile quand un Shooter tire
	@Override
	public void update(Shooter shooter) {
		if(shooter instanceof Goblin){
			System.out.println("Goblin shooting");
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
//			System.out.println("Wizard shooting at: " +shooter.getFireDirection().getX()+";"+shooter.getFireDirection().getY());
		}
	}
}
