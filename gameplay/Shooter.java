package gameplay;

import java.awt.Point;

import utils.Observable;

public interface Shooter extends Observable<Shooter> {
	public void shoot();
	public Point getPosition();
	public Point getFireDirection();
	void setFireDirection(Point p);
}
