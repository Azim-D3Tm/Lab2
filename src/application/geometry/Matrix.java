package application.geometry;

import javafx.geometry.Point3D;

public class Matrix{
	double values[][];
	
	
	public Matrix(int rows, int columns) {
		values = new double[rows][columns];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				values[i][j] = 0;
			}
		}
	}
	
	public Matrix(Matrix matrix) {
		int rows = matrix.getRows();
		int columns = matrix.getColumns();
		values = new double[rows][columns];
		
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++) {
				values[i][j] = matrix.values[i][j];
			}
		}
	}
	
	
    public static Matrix makeTranslationMatrix(Point3D position) {
        Matrix result = Matrix.makeIdentityMatrix(4);

        result.values[3][0] = position.getX();
        result.values[3][1] = position.getY();
        result.values[3][2] = position.getZ();

        return result;
    }
	
    public static Matrix makeMatrixPointAt(Point3D pos, Point3D direction, Point3D up) {
        Point3D newForward = new Point3D(direction.getX(), direction.getY(), direction.getZ());
        newForward.normalize();

        Point3D newUp = newForward.multiply(up.dotProduct(newForward));
        newUp = up.subtract(newUp);
        newUp.normalize();

        Point3D newRight = newUp.crossProduct(newForward).normalize();

        Matrix result = new Matrix(4, 4);

        Point3D posMod = pos.multiply(-1);


        result.values[0][0] = newRight.getX();
        result.values[0][1] = newUp.getX();
        result.values[0][2] = newForward.getX();
        result.values[0][3] = 0.0;
        result.values[1][0] = newRight.getY();
        result.values[1][1] = newUp.getY();
        result.values[1][2] = newForward.getY();
        result.values[1][3] = 0.0;
        result.values[2][0] = newRight.getZ();
        result.values[2][1] = newUp.getZ();
        result.values[2][2] = newForward.getZ();
        result.values[2][3] = 0.0;
        result.values[3][0] = posMod.dotProduct(newRight);
        result.values[3][1] = posMod.dotProduct(newUp);
        result.values[3][2] = posMod.dotProduct(newForward);
        result.values[3][3] = 1.0;

        return result;
    }
    
	public static Matrix makeIdentityMatrix(int size) {
        Matrix result = new Matrix(size, size);
        for (int i = 0; i < size; i++)
            result.values[i][i] = 1;
        return result;
    }
	
    public static Matrix makeMultiplicationMatrix(double x, double y, double z) {
        Matrix matrix = makeIdentityMatrix(4);

        matrix.values[0][0] = x;
        matrix.values[1][1] = y;
        matrix.values[2][2] = z;

        return matrix;
    }
 
    public Matrix multiply(double k) {
        Matrix result = new Matrix(getRows(), getColumns());
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[0].length; j++) {
                result.values[i][j] = k * values[i][j];
            }
        }
        return result;
    }
    
    public Matrix multiply(Matrix matrix) {
        if (this.getColumns() != matrix.getRows()) throw new IllegalArgumentException("Size mismatch");
        Matrix result = new Matrix(this.getRows(), matrix.getColumns());
        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < matrix.getColumns(); j++) {
                for (int k = 0; k < this.getColumns(); k++) {
                    result.values[i][j] += values[i][k] * matrix.values[k][j];
                }
            }
        }
        return result;
    }
    
    public int getRows() {
    	return values.length;
    }
    public int getColumns() {
    	return values[0].length;
    }
    
    public static Matrix rotationMatrix(int coordinate, double angle, int size) {
    	Matrix result = Matrix.makeIdentityMatrix(size);
    	double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        if (coordinate == 0) {
        	result.values[0][0] = 1;
        	result.values[1][1] = cos;
        	result.values[1][2] = -sin;
        	result.values[2][1] = sin;
        	result.values[2][2] = cos;
        } else if (coordinate == 1) {
        	result.values[0][0] = cos;
        	result.values[0][2] = sin;
        	result.values[1][1] = 1;
        	result.values[2][0] = -sin;
        	result.values[2][2] = cos;
        } else if (coordinate == 2) {
        	result.values[0][0] = cos;
            result.values[0][1] = -sin;
            result.values[1][0] = sin;
            result.values[1][1] = cos;
            result.values[2][2] = 1;
        }
        
        return result;
    }
}
