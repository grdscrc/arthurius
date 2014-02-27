package gameplay;

import characters.Goblin;
import gameframework.game.IllegalMoveException;
import gameframework.game.MoveBlockerRulesApplierDefaultImpl;

public class MoveBlockers extends MoveBlockerRulesApplierDefaultImpl {

	public void moveBlockerRule(Goblin g, Wall w) throws IllegalMoveException {
		if (g.isActive()) {
			throw new IllegalMoveException();
		}
	}
	public void moveBlockerRule(Projectile p, Wall w) throws IllegalMoveException {
		p.stop();
	}
}
