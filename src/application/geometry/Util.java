package application.geometry;

import javafx.geometry.Point2D;

public class Util {
	
	public static Point2D lerp(double delta, Point2D a, Point2D b) {
		if(delta<0||delta>1) {
			throw new IllegalArgumentException("delta must be between 0 and 1!");
		}
		return a.add((b.getX()-a.getX())*delta, (b.getY()-a.getY())*delta);
	}
	
	
}