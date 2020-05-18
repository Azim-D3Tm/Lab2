package application.geometry;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;

public class Triangle {
	public Point3D p1, p2, p3, n1, n2, n3;
	public Color lineColor, frontColor, backColor;
	
	public Triangle(Point3D p1, Point3D p2, Point3D p3, double opacity) {
		this(p1,p2,p3, Color.BLACK, Color.color(0,1,1, opacity), Color.color(1, 1, 0, opacity));
	}
	
	public Triangle(Point3D p1, Point3D p2, Point3D p3, Color lineColor, Color frontColor, Color backColor) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.lineColor = lineColor;
		this.frontColor = frontColor;
		this.backColor = backColor;
		this.n1 = this.n2 = this.n3 = getNormal();
	}
	public Triangle(Point3D p1, Point3D p2, Point3D p3, Point3D n1, Point3D n2, Point3D n3, Color lineColor, Color frontColor, Color backColor) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.n1 = n1;
		this.n2 = n2;
		this.n3 = n3;
		this.lineColor = lineColor;
		this.frontColor = frontColor;
		this.backColor = backColor;
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
	
	public Point3D getNormal() {
		Point3D line = p2.subtract(p1);
		Point3D line2 = p3.subtract(p2);
		return line.crossProduct(line2).normalize();
	}
	
	public Point3D getNormal(int i) {
		switch(i) {
		case 0:
			return n1;
		case 1:
			return n2;
		case 2:
			return n3;
		default:
			return n1;
		}
	}
	
	
	public Point3D getCentrePoint() {
		return new Point3D(
				(p1.getX()+p2.getX()+p3.getX())/3,
				(p1.getY()+p2.getY()+p3.getY())/3,
				(p1.getZ()+p2.getZ()+p3.getZ())/3);
	}
	
	public void shiftToView(double width, double height) {
		p1 = Util.shiftPointToView(p1, width, height);
    	p2 = Util.shiftPointToView(p2, width, height);
    	p3 = Util.shiftPointToView(p3, width, height);
	}
	
	@Override
	public String toString() {
		return "{"+p1+", "+p2+", "+p3+"}";
	}
	
	public void calculateLuminance(LightSource light) {
		Point3D offset = this.getCentrePoint().subtract(light.location);
		Point3D direction = offset.normalize();
		double luminance = this.getNormal().dotProduct(direction);
		double distance = new Point3D(0,0,0).distance(offset);
		if(distance>=light.intensity) {
			luminance = 1;
		}else {
			luminance = (distance/light.intensity);
		}
		
		boolean l = luminance<0;
		this.frontColor = Util.combineColors(this.frontColor, l?light.color:light.color.invert(), luminance>0?luminance:-luminance);
		this.backColor = Util.combineColors(this.backColor, l?light.color:light.color.invert(), luminance>0?luminance:-luminance);
	}
}
