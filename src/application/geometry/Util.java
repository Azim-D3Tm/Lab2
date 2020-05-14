package application.geometry;

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

}
