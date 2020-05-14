package application.geometry;

import javafx.geometry.Point3D;

public class Camera {
	public Point3D location;
	public Point3D direction;
	public Matrix projectionMatrix;
	
	public double aspectRatio;
	public static Point3D up = new Point3D(0, 1, 0);
	public double speed = 0.3;
	
	private double near = 0.1, far = 1000.0, fov = 90.0;
	private double fovRad = 1.0 / Math.tan(Math.toRadians(fov * 0.5));
	
	public double pitch=0, yaw=0;
	
	public Camera(double aspect) {
		aspectRatio = aspect;
		projectionMatrix = new Matrix(4, 4);

        projectionMatrix.values[0][0] = aspectRatio * fovRad;
        projectionMatrix.values[1][1] = fovRad;
        projectionMatrix.values[2][2] = far / (far - near);
        projectionMatrix.values[3][2] = (-far * near) / (far - near);
        projectionMatrix.values[2][3] = 1.0;
        location = new Point3D(20,20, -20);
        direction = new Point3D(0, 0, 1);
	}
	
	public void move(double forward, double right, double up, double rotateX, double rotateY) {
        direction = Util.multiplyByMatrix(direction, Matrix.rotationMatrix(0, rotateX, 4)); //rotate camera
        direction = Util.multiplyByMatrix(direction, Matrix.rotationMatrix(1, rotateY, 4));
        
        Point3D moveForward = direction.multiply(forward * speed); //calculate movement
        Point3D moveRight = Camera.up.crossProduct(direction).normalize().multiply(right * speed);
        
        location = location.add(moveForward).add(moveRight);  //apply movement
        location = location.add(new Point3D(0, up * speed, 0));
	}
	
}
