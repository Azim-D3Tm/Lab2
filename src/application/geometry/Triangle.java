package application.geometry;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Triangle {
	public Point3D p1, p2, p3;
	public Color color;
	
	public Triangle(Point3D p1, Point3D p2, Point3D p3) {
		this(p1,p2,p3, Color.BLACK);
	}
	
	public Triangle(Point3D p1, Point3D p2, Point3D p3, Color color) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.color = color;
	}
	
	public Point3D[] getPoints() {
		return new Point3D[] {p1,p2,p3};
	}
}
