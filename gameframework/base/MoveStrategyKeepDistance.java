package gameframework.base;

public class MoveStrategyKeepDistance implements MoveStrategy {

	int distanceToKeep;
	
	public MoveStrategyKeepDistance(int dist) {
		this.distanceToKeep = dist;
	}

	public SpeedVector getSpeedVector(Movable self) {
		self.getPosition().translate(1, 1);
		return new SpeedVectorDefaultImpl(self.getPosition());
//		double dist = self.getPosition().distance(a.getPosition());
//		
//		int xDirection = (int) Math.rint((self.getPosition().getX() - a.getPosition().getX())
//				/ dist);
//		int yDirection = (int) Math.rint((self.getPosition().getY() - a.getPosition().getY())
//				/ dist);
//		
//		SpeedVector move;
//		if(dist>distanceToKeep){
//			 move = new SpeedVectorDefaultImpl(new Point(xDirection,
//				yDirection));
//		}
//		else{
//			 move = new SpeedVectorDefaultImpl(new Point(-xDirection,
//						-yDirection));
//		}
//		return move;
	}
}
