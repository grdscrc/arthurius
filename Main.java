

import gameframework.game.GameLevel;
import gameplay.ArthuriusTheGame;

import java.util.ArrayList;

import levels.*;

public class Main {
	public static void main(String[] args) {
		ArthuriusTheGame g = new ArthuriusTheGame();
		ArrayList<GameLevel> levels = new ArrayList<GameLevel>();

		levels.add(new GameLevelOne(g));
		levels.add(new GameLevelTwo(g));
		levels.add(new GameLevelThree(g));
		
		g.setLevels(levels);
		g.createIntro();
		g.start();
	}
}
