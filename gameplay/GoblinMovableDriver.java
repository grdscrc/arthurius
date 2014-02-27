package gameplay;

import java.awt.Point;

import characters.Arthurius;
import utils.Observer;
import gameframework.base.Movable;
import gameframework.base.SpeedVector;
import gameframework.base.SpeedVectorDefaultImpl;
import gameframework.game.GameMovableDriverDefaultImpl;

public class GoblinMovableDriver extends GameMovableDriverDefaultImpl implements Observer<Arthurius> {
	private static Point target;
	private static boolean arthuriusFound = false;
	
	@Override
	public SpeedVector getSpeedVector(Movable m) {
		//SpeedVector currentSpeedVector;
		//currentSpeedVector = m.getSpeedVector();

		if(arthuriusFound){
			//Double distance = m.getPosition().distance(target);
		
			SpeedVector escapeSVector = new SpeedVectorDefaultImpl(target, 1);
			return escapeSVector;
		}
		return m.getSpeedVector();
}

	@Override
	public void update(Arthurius s) {
		target = s.getPosition();
		arthuriusFound = true;
	}
}