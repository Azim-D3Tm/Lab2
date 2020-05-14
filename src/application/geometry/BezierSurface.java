package application.geometry;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point3D;

public class BezierSurface {
	
	private List<List<Point3D>> criticalPoints;
	private static double precision = 0.02;
	private Point3D[][]points;
	
	public BezierSurface(List<List<Point3D>> ctrlPoints) {
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
	
	public List<Triangle> translatePointsToTriangles() {
        List<Triangle> tris = new ArrayList<>();
        for (int i = 1; i < points.length; i++) {
            for (int j = 1; j < points[0].length; j++) {
            	Triangle t = new Triangle(points[i - 1][j - 1], points[i - 1][j], points[i][j - 1]);
                tris.add(t);
                t = new Triangle(points[i][j - 1], points[i - 1][j], points[i][j]);
                tris.add(t);
                if(points[i][j]==null) {
                	System.out.println("null at "+i+" "+j);
                }
                if(points[i][j-1]==null) {
                	System.out.println("null at "+i+" "+(j-1));
                }
                if(points[i-1][j]==null) {
                	System.out.println("null at "+(i-1)+" "+j);
                }
                if(points[i-1][j-1]==null) {
                	System.out.println("null at "+(i-1)+" "+(j-1));
                }
            }
        }
        return tris;
    }
	
}
