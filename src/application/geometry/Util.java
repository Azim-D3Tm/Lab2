package application.geometry;

import javafx.geometry.Point2D;

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
	
}
