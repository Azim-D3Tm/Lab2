package application.geometry;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class LightSource {
	public Point3D location;//, direction;
	public double intensity;
	public Color color;
	
	public LightSource(double intensity) {
		this(new Point3D(0,0,0), intensity);
	}
	
	public LightSource(Point3D location,  double intensity) {
		this(location, Color.color(1, 1, 1), intensity);
	}
	
	public LightSource(Point3D location, Color color, double intensity) {
		this.location = location;
		//this.direction = direction;
		this.intensity = intensity;
		this.color = color;
	}
}
