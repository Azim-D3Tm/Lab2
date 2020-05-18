package application.geometry;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point3D;

public class BezierSurface extends Model{
	
	public List<List<Point3D>> criticalPoints;
	private static double precision = 0.02;
	private Point3D[][]points;
	
	
	public BezierSurface(Point3D origin, List<List<Point3D>> ctrlPoints) {
		super(origin);
		criticalPoints = new ArrayList<>(ctrlPoints);
		points = new Point3D[(int) (1.0 / precision)][(int) (1.0 / precision)];
		calculatePoints();
	}
	public void calculatePoints() {
		for(double u = 0; u < 1; u+=precision) {
			for(double v = 0; v < 1; v+=precision) {
				calculatePoint(u,v);
			}
		}
        for (int i = 1; i < points.length; i++) {
            for (int j = 1; j < points[0].length; j++) {
            	Triangle t = new Triangle(points[i - 1][j - 1], points[i - 1][j], points[i][j - 1], 1);
            	faces.add(t);
                t = new Triangle(points[i][j - 1], points[i - 1][j], points[i][j], 1);
                faces.add(t);
            }
        }
	}
	
	private void calculatePoint(double u, double v) {
		
		int uindex = (int) Math.round(u * points.length);
		int vindex = (int) Math.round(v * points[0].length);
		
		double bPoly, sumX = 0, sumY = 0, sumZ = 0;

        for (int i = 0; i < criticalPoints.size(); i++) {
            for (int j = 0; j < criticalPoints.get(0).size(); j++) {
                bPoly = Util.bernstein(u, criticalPoints.size() - 1, i) * Util.bernstein(v, criticalPoints.get(0).size() - 1, j);

                sumX += bPoly * criticalPoints.get(i).get(j).getX();
                sumY += bPoly * criticalPoints.get(i).get(j).getY();
                sumZ += bPoly * criticalPoints.get(i).get(j).getZ();
            }
        }
		points[uindex][vindex] = new Point3D(sumX, sumY, sumZ);
	}
	
}
