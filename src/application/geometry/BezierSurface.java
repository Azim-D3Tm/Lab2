package application.geometry;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point3D;

public class BezierSurface {
	
	private List<List<Point3D>> criticalPoints;
	private static double precision = 0.05;
	private Point3D[][]points;
	
	public BezierSurface(List<List<Point3D>> ctrlPoints) {
		criticalPoints = new ArrayList<>(ctrlPoints);
		points = new Point3D[(int) (1.0 / precision)][(int) (1.0 / precision)];
		
		
		
	}
}
