package application.geometry;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class Util {
	
	public static Point2D lerp(double delta, Point2D a, Point2D b) {
		if(delta<0||delta>1) {
			throw new IllegalArgumentException("delta must be between 0 and 1!");
		}
		return a.add((b.getX()-a.getX())*delta, (b.getY()-a.getY())*delta);
	}

    public static double bernstein(double t, int n, int k) {
        return binomial(n, k) * Math.pow(1 - t, n - k) * Math.pow(t, k);
    }

    public static int binomial(int n, int k) {
        if (k > n || k < 0)
            return 0;
        if ((n == k) || (k == 0)) {
            return 1;
        }
        return binomial(n - 1, k) + binomial(n - 1, k - 1);
    }
    public static Point3D multiplyByMatrix(Point3D vector, Matrix matrix) {
        if (matrix == null) throw new NullPointerException("Matrix is null");
        if (matrix.getColumns() != 4) throw new IllegalArgumentException("Width of matrix != 4");
        
        double x = vector.getX() * matrix.values[0][0] + vector.getY() * matrix.values[1][0] + vector.getZ() * matrix.values[2][0] + matrix.values[3][0];
        double y = vector.getX() * matrix.values[0][1] + vector.getY() * matrix.values[1][1] + vector.getZ() * matrix.values[2][1] + matrix.values[3][1];
        double z = vector.getX() * matrix.values[0][2] + vector.getY() * matrix.values[1][2] + vector.getZ() * matrix.values[2][2] + matrix.values[3][2];
        
        double w =  vector.getX() * matrix.values[0][3] + vector.getY() * matrix.values[1][3] + vector.getZ() * matrix.values[2][3] + matrix.values[3][3];
        if(w!=0) {
        	x/=w;
        	y/=w;
        	z/=w;
        }
        return new Point3D(x, y, z);
    }
    
    public static List<Triangle> generateCube(){
    	List<Triangle> result = new ArrayList<Triangle>();
    	Point3D a1 = new Point3D(0,0,0);
    	Point3D a2 = new Point3D(1,0,0);
    	Point3D a3 = new Point3D(1,1,0);
    	Point3D a4 = new Point3D(0,1,0);
    	Point3D b1 = new Point3D(0,0,1);
    	Point3D b2 = new Point3D(1,0,1);
    	Point3D b3 = new Point3D(1,1,1);
    	Point3D b4 = new Point3D(0,1,1);
    	
    	//up
    	result.add(new Triangle(a1,a2,a3,1));
    	result.add(new Triangle(a2,a3,a4,1));
    	//down
    	result.add(new Triangle(b1,b2,b3,1));
    	result.add(new Triangle(b2,b3,b4,1));
    	//front
    	result.add(new Triangle(a1,a2,b1,1));
    	result.add(new Triangle(b1,b2,a2,1));
    	//back
    	result.add(new Triangle(a3,a4,b3,1));
    	result.add(new Triangle(b3,b4,a4,1));
    	//left
    	result.add(new Triangle(a1,a3,b1,1));
    	result.add(new Triangle(b1,b3,a3,1));
    	//right
    	result.add(new Triangle(a2,a4,b2,1));
    	result.add(new Triangle(b2,b4,a4,1));
    	
    	return result;
    }
    
    public static boolean intersects(List<Point2D> figure, Line2D line) {
    	Point2D[]polygon = new Point2D[figure.size()];
    	polygon = figure.toArray(polygon);
    	for(int i = 0; i<figure.size(); i++) {
    		Line2D iline;
    		if(i == 0) {
    			iline = new Line2D(figure.get(figure.size()-1), figure.get(0));
    		}else {
    			iline = new Line2D(figure.get(i-1), figure.get(i));
    		}
    		if(iline.intersects(line)) return true;
    	}
    	return Line2D.isInside(polygon, line.a)||Line2D.isInside(polygon, line.b);
    }

}
