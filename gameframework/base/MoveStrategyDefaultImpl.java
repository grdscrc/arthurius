package gameframework.base;

public class MoveStrategyDefaultImpl implements MoveStrategy {
	public SpeedVector getSpeedVector(Movable self) {
		return SpeedVectorDefaultImpl.createNullVector();
	}
}
