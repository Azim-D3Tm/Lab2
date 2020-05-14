package application.geometry;

import javafx.geometry.Point2D;

public class Line2D {
	public Point2D a, b;

	public Line2D(Point2D a, Point2D b) {
		this.a = a;
		this.b = b;
	}

	public boolean intersects(Line2D other) {
		return doIntersect(this, other);
	}


	private static boolean onSegment(Point2D p, Point2D q, Point2D r) { 
		return (
				q.getX() <= Math.max(p.getX(), r.getX()) && 
				q.getX() >= Math.min(p.getX(), r.getX()) && 
				q.getY() <= Math.max(p.getY(), r.getY()) && 
				q.getY() >= Math.min(p.getY(), r.getY()) );
	} 

	private static int orientation(Point2D p, Point2D q, Point2D r) {
		double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY()); //dot product

		if (val == 0) return 0; // colinear 
		return (val > 0)? 1: -1; // clock or counterclock wise 
	}

	public static boolean doIntersect(Line2D a, Line2D b) {
		Point2D p1 = a.a, q1 = a.b, p2 = b.a, q2 = b.b;
		// Find the four orientations needed for general and 
		// special cases 
		int o1 = orientation(p1, q1, p2); 
		int o2 = orientation(p1, q1, q2); 
		int o3 = orientation(p2, q2, p1); 
		int o4 = orientation(p2, q2, q1); 
		// General case 
		if (o1 != o2 && o3 != o4) 
			return true; 

		// Special Cases 
		// p1, q1 and p2 are colinear and p2 lies on segment p1q1 
		if (o1 == 0 && onSegment(p1, p2, q1)) return true; 
		// p1, q1 and q2 are colinear and q2 lies on segment p1q1 
		if (o2 == 0 && onSegment(p1, q2, q1)) return true; 
		// p2, q2 and p1 are colinear and p1 lies on segment p2q2 
		if (o3 == 0 && onSegment(p2, p1, q2)) return true; 
		// p2, q2 and q1 are colinear and q1 lies on segment p2q2 
		if (o4 == 0 && onSegment(p2, q1, q2)) return true; 

		return false; // Doesn't fall in any of the above cases 
	}

	public static boolean isInside(Point2D polygon[], Point2D p) { 
		int n = polygon.length;
		// There must be at least 3 vertices in polygon[] 
		if (n < 3) return false; 
		// Create a point for line segment from p to infinite 
		Point2D extreme = new Point2D(Double.MAX_VALUE, p.getY()); 

		// Count intersections of the above line  
		// with sides of polygon 
		int count = 0, i = 0; 
		do { 
			int next = (i + 1) % n; 

			// Check if the line segment from 'p' to  
			// 'extreme' intersects with the line  
			// segment from 'polygon[i]' to 'polygon[next]' 
			if (doIntersect(new Line2D(polygon[i], polygon[next]), new Line2D(p, extreme)))  { 
				// If the point 'p' is colinear with line  
				// segment 'i-next', then check if it lies  
				// on segment. If it lies, return true, otherwise false 
				if (orientation(polygon[i], p, polygon[next]) == 0) { 
					return onSegment(polygon[i], p, polygon[next]); 
				}
				count++; 
			} 
			i = next; 
		} while (i != 0); 

		// Return true if count is odd
		return (count % 2 == 1); 
	}
}
