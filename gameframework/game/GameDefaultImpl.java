package gameframework.game;

import gameframework.base.ObservableValue;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * Create a basic game application with menus and displays of lives and score
 */
public class GameDefaultImpl implements Game, Observer {
	protected static final int NB_ROWS = 31;
	protected static final int NB_COLUMNS = 28;
	protected static final int SPRITE_SIZE = 16;
	public static final int MAX_NUMBER_OF_PLAYER = 4;
	public static final int NUMBER_OF_LIVES = 5;

	protected CanvasDefaultImpl defaultCanvas = null;
	protected ObservableValue<Integer> score[] = new ObservableValue[MAX_NUMBER_OF_PLAYER];
	protected ObservableValue<Integer> life[] = new ObservableValue[MAX_NUMBER_OF_PLAYER];
	protected ObservableValue<String> stuff[] = new ObservableValue[4];

	// initialized before each level
	protected ObservableValue<Boolean> levelCompleted = null;
	protected ObservableValue<Boolean> gameOver = null;

	private Frame f;
	private GameLevelDefaultImpl currentPlayedLevel = null;

	protected int levelNumber;
	protected ArrayList<GameLevel> gameLevels;

	protected Label stuffText;
	protected Label epeeT;
	protected Label lifeText, scoreText;
	protected Label information;
	protected Label informationValue;
	protected Label lifeValue, scoreValue;
	protected Label currentLevel;
	protected Label currentLevelValue;

	public GameDefaultImpl() {
		for (int i = 0; i < MAX_NUMBER_OF_PLAYER; ++i) {
			score[i] = new ObservableValue<Integer>(0);
			life[i] = new ObservableValue<Integer>(0);
		}
		for (int i = 0; i < 4; ++i) {
			stuff[i]= new ObservableValue<String>("Vide");
		}
		lifeText = new Label("Lives:");
		scoreText = new Label("Score:");
		information = new Label("State:");
		informationValue = new Label("Playing");
		currentLevel = new Label("Level:");
		createGUI();
	}

	public void createGUI() {
		f = new Frame("La quête d'Arthurius");
		f.dispose();
		f.setBackground(Color.BLACK);
		
		createMenuBar();
		Container c = createStatusBar();
		Container s= createStuffBorder();

		defaultCanvas = new CanvasDefaultImpl();
		defaultCanvas.setSize(SPRITE_SIZE * NB_COLUMNS, SPRITE_SIZE * NB_ROWS);
		f.add(defaultCanvas);
		f.add(c, BorderLayout.NORTH);
		f.add(s, BorderLayout.SOUTH);
		f.pack();
		f.setVisible(true);
				
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	private void createMenuBar() {
		MenuBar menuBar = new MenuBar();
		Menu file = new Menu("file");
		MenuItem start = new MenuItem("new game");
		MenuItem restartlevel = new MenuItem("restart level");
		MenuItem save = new MenuItem("save");
		MenuItem restore = new MenuItem("load");
		MenuItem quit = new MenuItem("quit");
		Menu game = new Menu("game");
		MenuItem pause = new MenuItem("pause");
		MenuItem resume = new MenuItem("resume");
		menuBar.add(file);
		menuBar.add(game);
		f.setMenuBar(menuBar);

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				start();
			}
		});
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		restartlevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restartLevel();
			}
		});
		restore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restore();
			}
		});
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		pause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pause();
			}
		});
		resume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resume();
			}
		});

		file.add(start);
		file.add(restartlevel);
		file.add(save);
		file.add(restore);
		file.add(quit);
		game.add(pause);
		game.add(resume);
	}
	
	private Container createStuffBorder() {
		JPanel Stuff = new JPanel();
		Stuff.setBackground(Color.WHITE);
		BoxLayout layout = new BoxLayout(Stuff,0);
		
		Stuff.setLayout(layout);

		
		Label titre=new Label("Equipement:");
		Stuff.add(titre);
		
		stuffText = new Label(stuff[0].getValue());
		Stuff.add(stuffText);
		
		epeeT = new Label (stuff[1].getValue());
		Stuff.add(epeeT);

	
		for(int i=2;i<MAX_NUMBER_OF_PLAYER;i++){
			Label st = new Label(stuff[i].getValue());
			Stuff.add(st);
			
		}
		
	
		
		return Stuff;
	}
	
	private Container createStatusBar() {
		JPanel c = new JPanel();
		c.setBackground(Color.WHITE);
		GridBagLayout layout = new GridBagLayout();
		c.setLayout(layout);
		lifeValue = new Label(Integer.toString(life[0].getValue()));
		scoreValue = new Label(Integer.toString(score[0].getValue()));
		currentLevelValue = new Label(Integer.toString(levelNumber));
		c.add(lifeText);
		c.add(lifeValue);
		c.add(scoreText);
		c.add(scoreValue);
		c.add(currentLevel);
		c.add(currentLevelValue);
		c.add(information);
		c.add(informationValue);
		return c;
	}

	public Canvas getCanvas() {
		return defaultCanvas;
	}

	public void start() {
		for (int i = 0; i < MAX_NUMBER_OF_PLAYER; ++i) {
			score[i].addObserver(this);
			life[i].addObserver(this);
			life[i].setValue(NUMBER_OF_LIVES);
			score[i].setValue(0);
		}
		for (int i = 0; i < 4; ++i) {
			stuff[i].addObserver(this);
			stuff[i].setValue("Vide");
		}
		levelNumber = 0;
		while(true){
			levelCompleted = new ObservableValue<Boolean>(false);
			levelCompleted.addObserver(this);
			gameOver = new ObservableValue<Boolean>(false);
			gameOver.addObserver(this);
			try {
				if (currentPlayedLevel != null && currentPlayedLevel.isAlive()) {
					currentPlayedLevel.interrupt();
					currentPlayedLevel = null;
				}
				currentPlayedLevel = (GameLevelDefaultImpl) gameLevels.get(levelNumber);
				levelNumber++;
				informationValue.setText("Playing");
				currentLevelValue.setText(Integer.toString(levelNumber));
				currentPlayedLevel.start();
				currentPlayedLevel.join();
				if(gameOver.getValue())
					levelNumber = 0;
			} catch (Exception e) {
				System.out.println("Exception in main game loop !");
				e.printStackTrace();
				return;
			}
		}

	}

	public void restore() {
		System.out.println("restore(): Unimplemented operation");
	}
	
	public void restartLevel(){
		currentPlayedLevel.init();
	}

	public void save() {
		System.out.println("save(): Unimplemented operation");
	}

	public void pause() {
		currentPlayedLevel.pause();
	}

	public void resume() {
		currentPlayedLevel.unpause();
	}

	public ObservableValue<Integer>[] score() {
		return score;
	}

	public ObservableValue<Integer>[] life() {
		return life;
	}

	public ObservableValue<Boolean> levelCompleted() {
		return levelCompleted;
	}

	public ObservableValue<Boolean> gameOver() {
		return gameOver;
	}

	public void setLevels(ArrayList<GameLevel> levels) {
		gameLevels = levels;
	}

	public void update(Observable o, Object arg) {
		if (o == levelCompleted) {
			if (levelCompleted.getValue()) {
				informationValue.setText(":D");
				currentPlayedLevel.interrupt();
				currentPlayedLevel.end();
			}
		} else if (o == gameOver) {
			if (gameOver.getValue()) {
				try {
					informationValue.setText("Nooo :'(");

					//TODO: replace by a more explicit countdown
					Thread.sleep(2000);

					currentPlayedLevel.reset();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else
				informationValue.setText("Playing");
		} else {
			for (ObservableValue<Integer> lifeObservable : life) {
				if (o == lifeObservable) {
					int lives = ((ObservableValue<Integer>) o).getValue();
					lifeValue.setText(Integer.toString(lives));
					if (lives == 0) {
						gameOver.setValue(true);
					}
				}
			}
			for (ObservableValue<Integer> scoreObservable : score) {
				if (o == scoreObservable) {
					scoreValue
							.setText(Integer
									.toString(((ObservableValue<Integer>) o)
											.getValue()));
				}
			}
			for (ObservableValue<String> stuffObservable : stuff) {
				if (o == stuffObservable) {
					
					String t = (((ObservableValue<String>) o).getValue());
					if(t == "Bottes")
						stuffText.setText(t);
					if(t=="Epee")
						epeeT.setText(t);
					
				
				}
			}
		}
	}

	@Override
	public ObservableValue<String>[] stuff() {
		return stuff;
	}
}
