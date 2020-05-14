package application.geometry;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Triangle {
	public Point3D p1, p2, p3;
	public Color lineColor, fillColor;
	
	public Triangle(Point3D p1, Point3D p2, Point3D p3) {
		this(p1,p2,p3, Color.BLACK, Color.color(0,1,1, 0.3));
	}
	
	public Triangle(Point3D p1, Point3D p2, Point3D p3, Color lineColor, Color fillColor) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.lineColor = lineColor;
		this.fillColor = fillColor;
	}
	
	public Point3D[] getPoints() {
		return new Point3D[] {p1, p2, p3};
	}
	
	public double[] getXPoints() {
		return new double[] {p1.getX(), p2.getX(), p3.getX()};
	}
	
	public double[] getYPoints() {
		return new double[] {p1.getY(), p2.getY(), p3.getY()};
	}
	
	public void shiftToView(double width, double height) {
    	p1 = new Point3D((p1.getX()+1)*0.5*width, (p1.getY()*-1+1)*0.5*height, p1.getZ());
    	p2 = new Point3D((p2.getX()+1)*0.5*width, (p2.getY()*-1+1)*0.5*height, p2.getZ());
    	p3 = new Point3D((p3.getX()+1)*0.5*width, (p3.getY()*-1+1)*0.5*height, p3.getZ());
	}
	
	@Override
	public String toString() {
		return "{"+p1+", "+p2+", "+p3+"}";
	}
}
