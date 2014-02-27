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
import gameplay.Jail;
import gameplay.MoveBlockers;
import gameplay.Mud;
import gameplay.OverlapRules;
import gameplay.Projectile;
import gameplay.Shooter;
import gameplay.SuperCandy;
import gameplay.TeleportPairOfPoints;
import gameplay.Wall;
import gameplay.Water;

import java.awt.Canvas;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import characters.Arthurius;
import characters.Goblin;
import utils.Observer;

public class GameLevelOne extends GameLevelDefaultImpl implements Observer<Shooter>  {
	Canvas canvas;

	// 0 : Botte; 1 : Walls; 2 : SuperPacgums; 3 : Doors; 4 : Jail; 5 : empty 6: Mud 7:Water 8:Door
	// Note: teleportation points are not indicated since they are defined by
	// directed pairs of positions.
	static int[][] tab = { 
		    { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 7, 7, 7, 7, 7, 7, 7, 7, 1, 5, 5, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 7, 7, 7, 7, 7, 7, 7, 7, 1, 5, 5, 1, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 7, 7, 7, 7, 7, 7, 7, 7, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 7, 7, 7, 7, 7, 7, 7, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 7, 7, 7, 7, 7, 7, 7, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 7, 7, 7, 7, 7, 7, 7, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 7, 7, 7, 7, 7, 7, 7, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 7, 7, 7, 7, 7, 7, 7, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 7, 7, 7, 7, 7, 7, 7, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 1, 1, 1, 1, 1, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 8, 6, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 5, 5, 5, 5, 1 },
			{ 1, 5, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1, 5, 5, 1, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 0, 5, 5, 5, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 1, 5, 5, 1, 5, 5, 5, 5, 5, 5, 5, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };

	public static final int SPRITE_SIZE = 16;
	public static final int NUMBER_OF_GOBLINS = 1;//4;
	
	protected Arthurius _Arthurius;
	protected static MoveBlockerChecker moveBlockerChecker = new MoveBlockerCheckerDefaultImpl();

	@Override
	protected void init() {
		OverlapProcessor overlapProcessor = new OverlapProcessorDefaultImpl();

		moveBlockerChecker.setMoveBlockerRules(new MoveBlockers());
		
		
		OverlapRules overlapRules = new OverlapRules(new Point(14 * SPRITE_SIZE, 17 * SPRITE_SIZE),
				new Point(14 * SPRITE_SIZE, 15 * SPRITE_SIZE), life[0], score[0],
				stuff, levelCompleted, gameOver);
		overlapProcessor.setOverlapRules(overlapRules);

		universe = new GameUniverseDefaultImpl(moveBlockerChecker, overlapProcessor);
		overlapRules.setUniverse(universe);

		gameBoard = new GameUniverseViewPortDefaultImpl(canvas, universe);
		((CanvasDefaultImpl) canvas).setDrawingGameBoard(gameBoard);

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
				if (tab[i][j] == 4) {
					universe.addGameEntity(new Jail(new Point(j * SPRITE_SIZE, i * SPRITE_SIZE)));
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
		
		
		// Pacman definition and inclusion in the universe
		_Arthurius = Arthurius.getInstance(canvas);
		GameMovableDriverDefaultImpl arthDriver = new GameMovableDriverDefaultImpl();
		MoveStrategyKeyboard keyStr = new MoveStrategyKeyboard();
		arthDriver.setStrategy(keyStr);
		arthDriver.setmoveBlockerChecker(moveBlockerChecker);
		canvas.addKeyListener(keyStr);
		_Arthurius.setDriver(arthDriver);
		_Arthurius.setPosition(new Point(1 * SPRITE_SIZE, 1 * SPRITE_SIZE));
		universe.addGameEntity(_Arthurius);
			
		//Goblins time !
		List<Point> goblinsPos = new ArrayList<Point>();
		goblinsPos.add(new Point( 1 * SPRITE_SIZE,20 * SPRITE_SIZE));
//		goblinsPos.add(new Point( 3 * SPRITE_SIZE,28 * SPRITE_SIZE));
//		goblinsPos.add(new Point(10 * SPRITE_SIZE, 4 * SPRITE_SIZE));
//		goblinsPos.add(new Point(17 * SPRITE_SIZE,28 * SPRITE_SIZE));

		List<Point>goblinsFire = new ArrayList<Point>();
		int[][] fireDirs = new int[NUMBER_OF_GOBLINS][2];
		fireDirs[0] = new int[] { 1, 0};
//		fireDirs[1] = new int[] { 0,-1};
//		fireDirs[2] = new int[] { 0, 1};
//		fireDirs[3] = new int[] { 0,-1};
//		
		int index = 0;
		for(Point pos : goblinsPos){
			goblinsFire.add((Point) pos.clone());
			goblinsFire.get(goblinsFire.size()-1)
			.translate(fireDirs[index][0], fireDirs[index][1]);
			index++;
		}
		
		Goblin myGoblin;
		for (int t = 0; t < NUMBER_OF_GOBLINS; ++t) {
			myGoblin = new Goblin(canvas);
			myGoblin.setPosition(goblinsPos.get(t));
			myGoblin.setFireDirection(goblinsFire.get(t));
			myGoblin.register(this);
			universe.addGameEntity(myGoblin);
		}
	}

	public GameLevelOne(Game g) {
		super(g);
		canvas = g.getCanvas();
	}

	//Créer un projectile quand un Shooter tire
	@Override
	public void update(Shooter s) {
		GameMovableDriverDefaultImpl pDriv = new GameMovableDriverDefaultImpl();
		MoveStrategyStraightLine strat = new MoveStrategyStraightLine(s.getPosition(), (Point) s.getFireDirection().clone());
		moveBlockerChecker.setMoveBlockerRules(new MoveBlockers());
		pDriv.setStrategy(strat);
		pDriv.setmoveBlockerChecker(moveBlockerChecker);
		Projectile p = new Projectile(canvas, s);
		p.setDriver(pDriv);
		p.setPosition(s.getPosition());
		universe.addGameEntity(p);
	}
}
