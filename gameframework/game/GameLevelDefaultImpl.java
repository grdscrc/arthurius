package gameframework.game;

import gameframework.base.ObservableValue;

import java.util.Date;

/**
 * To be implemented with respect to a specific game. Expected to initialize the
 * universe and the gameBoard
 */

public abstract class GameLevelDefaultImpl extends Thread implements GameLevel {
	private static final int MINIMUM_DELAY_BETWEEN_GAME_CYCLES = 75;
	protected GameUniverse universe;
	protected GameUniverseViewPort gameBoard;
	protected ObservableValue<Integer> score[];
	protected ObservableValue<Integer> life[];
	protected ObservableValue<String> stuff[];
	protected ObservableValue<Boolean> levelCompleted;
	protected ObservableValue<Boolean> gameOver;

	boolean pauseGameLoop;
	protected final Game g;
	
	private Thread levelThread;

	protected abstract void init();

	public GameLevelDefaultImpl(Game g) {
		this.g = g;
		this.score = g.score();
		this.life = g.life();
		this.stuff=g.stuff();
	}

	@Override
	public void start() {
		levelCompleted = g.levelCompleted();
		gameOver = new ObservableValue<Boolean>(false);
		init();
		levelThread = new Thread(this);
		levelThread.start();
		try {
			levelThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void reset() {
		init();
	}
	
	@Override
	public void run() {
		Thread thisThread = Thread.currentThread();
		pauseGameLoop = false;
		// main game loop :
		long start;
		while (thisThread == levelThread && !this.isInterrupted()) {
			start = new Date().getTime();
			if(!pauseGameLoop){
				gameBoard.paint();
				universe.allOneStepMoves();
				universe.processAllOverlaps();
			}

			try {
				long sleepTime = MINIMUM_DELAY_BETWEEN_GAME_CYCLES
						- (new Date().getTime() - start);
				if (sleepTime > 0) {
					Thread.sleep(sleepTime);
				}
			} catch (Exception e) {
			}
		}
	}

	public void end() {
		levelThread = null;
	}

	public void pause() {
		pauseGameLoop = true;
		gameBoard.display("PAUSE", 25, 50);
	}

	public void unpause() {
		pauseGameLoop = false;
	}

	protected void overlap_handler() {
	}

}
