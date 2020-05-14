package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import application.geometry.BezierSurface;
import application.geometry.Camera;
import application.geometry.Matrix;
import application.geometry.Triangle;
import application.geometry.Util;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SampleController implements Initializable, Runnable{
	//curve
	@FXML
	private Canvas canvasCurve;
	@FXML 
	private ComboBox<String> modeBox;
	@FXML
	private TextField deltaText;
	@FXML
	private Tab curveTab;
	@FXML
	private TabPane tabPane;

	private GraphicsContext cg;

	private ArrayList<Point2D> dots = new ArrayList<>();
	private int dragged = -1;
	private double deltaCurve = 0.05;


	//surface
	@FXML
	private Tab surfaceTab;
	@FXML
	private Canvas canvasSurface;
	@FXML
	private TextField rotX;
	@FXML
	private TextField rotY;
	@FXML
	private Label camLocation;
	@FXML
	private Label camDirection;


	private GraphicsContext sg;

	private BezierSurface surface;
	private Camera camera;

	@FXML
	private Tab lab3Tab;
	
	

	public boolean running = true;

	public SampleController() {
	}

	public void onMouseClicked(MouseEvent event) {
		switch(modeBox.getValue()) {
		case "Add":
			dots.add(new Point2D(event.getX(), event.getY()));
			break;
		case "Move":
			dragged = -1;
			break;
		case "Remove":
			removeDot(event.getX(), event.getY());
			break;
		default:
			break;
		}
	}

	public void onKeyPressed(KeyEvent event) {
		int moveRight = 0;
		int moveForward = 0;
		int moveUp = 0;
		double rotateX=0, rotateY=0;
		switch(event.getCode()) {
		case A:
			moveRight-=1;
			break;
		case D:
			moveRight+=1;
			break;
		case W:
			moveForward+=1;
			break;
		case S:
			moveForward-=1;
			break;
		case SPACE:
			moveUp+=1;
			break;
		case CONTROL:
			moveUp-=1;
			break;
		case T:
			rotateX = Math.toRadians(5);
			break;
		case G:
			rotateX = Math.toRadians(-5);
			break;
		case F:
			rotateY = Math.toRadians(5);
			break;
		case H:
			rotateY = Math.toRadians(-5);
			break;
		default:
			break;
		}
		camera.move(moveForward, moveRight, moveUp, rotateX, rotateY);
	}

	public void onKeyReleased(KeyEvent event) {

	}

	public void resetBtnPressed(ActionEvent event) {
		dots.clear();
		deltaCurve = 0.05;
		modeBox.setValue("Add");
		deltaText.setText(deltaCurve+"");
	}

	public void render() {
		if(tabPane.getSelectionModel().getSelectedItem()==curveTab) {
			renderCurve();
		}else {
			renderSurface();
		}

	}

	private void renderSurface() {
		sg.clearRect(0, 0, canvasSurface.getWidth(), canvasSurface.getHeight());

		Matrix cameraMatrix = Matrix.makeMatrixPointAt(camera.location,
				camera.direction,
				Camera.up);

		for(Triangle t : surface.translatePointsToTriangles()) {
			//for(Triangle t: Util.generateCube()) {
			double rotx = getRotationAngleX();
			if(rotx!=0) {
				Matrix rx = Matrix.rotationMatrix(0, rotx, 4);
				t = new Triangle(
						Util.multiplyByMatrix(t.p1, rx),
						Util.multiplyByMatrix(t.p2, rx),
						Util.multiplyByMatrix(t.p3, rx),
						t.lineColor,
						t.fillColor);
			}
			double roty = getRotationAngleY();
			if(roty!=0) {
				Matrix ry = Matrix.rotationMatrix(0, roty, 4);
				t = new Triangle(
						Util.multiplyByMatrix(t.p1, ry),
						Util.multiplyByMatrix(t.p2, ry),
						Util.multiplyByMatrix(t.p3, ry),
						t.lineColor,
						t.fillColor);
			}
			Triangle translated = new Triangle(
					Util.multiplyByMatrix(t.p1, cameraMatrix),
					Util.multiplyByMatrix(t.p2, cameraMatrix),
					Util.multiplyByMatrix(t.p3, cameraMatrix));


			/*
			if(translated.p1.getZ()<0||translated.p2.getZ()<0||translated.p3.getZ()<0) {
				continue;
			}*/

			Triangle projected = new Triangle(
					Util.multiplyByMatrix(translated.p1, camera.projectionMatrix),
					Util.multiplyByMatrix(translated.p2, camera.projectionMatrix),
					Util.multiplyByMatrix(translated.p3, camera.projectionMatrix));

			projected.shiftToView(canvasSurface.getWidth(), canvasSurface.getHeight());
			sg.setFill(t.fillColor);
			sg.fillPolygon(projected.getXPoints(), projected.getYPoints(), 3);
			sg.setFill(t.lineColor);
			sg.strokePolygon(projected.getXPoints(), projected.getYPoints(), 3);
		}
		drawCamInfo();
	}
	private double getRotationAngleY() {
		String text = rotY.getText();
		try {
			return Math.toRadians(Double.valueOf(text));
		}catch(Exception e) {
			e.printStackTrace();
			rotY.setText("0");
		}
		return 0;
	}

	private double getRotationAngleX() {
		String text = rotX.getText();
		try {
			return Math.toRadians(Double.valueOf(text));
		}catch(Exception e) {
			e.printStackTrace();
			rotX.setText("0");
		}
		return 0;
	}

	private void drawCamInfo() {
		Point3D l = camera.location;
		camLocation.setText(String.format("Camera location:  %.2f:%.2f:%.2f",+l.getX(),l.getY(),l.getZ()));

		Point3D d = camera.direction;
		camDirection.setText(String.format("Camera direction: %.2f:%.2f:%.2f",+d.getX(),d.getY(),d.getZ()));
	}


	public void renderCurve() {
		cg.clearRect(0, 0, canvasCurve.getWidth(), canvasCurve.getHeight());
		for (int i = 0; i <dots.size();i++) {
			Point2D dot = dots.get(i);
			if(i==dragged) {
				cg.setFill(Color.GREEN);
			}
			cg.fillOval(dot.getX()-5, dot.getY()-5, 10, 10);
			cg.setFill(Color.BLACK);
		}
		if(dots.size()>2) {
			drawBeizerCurve();
		}
	}
	private void drawBeizerCurve() {
		int layers = dots.size()-1;

		ArrayList<Point2D> result = new ArrayList<Point2D>();

		for(double delta = 0; delta <=1; delta+=this.deltaCurve) {
			Map<Integer, ArrayList<Point2D>> layersMap = new HashMap<>();
			layersMap.put(0, dots);
			for(int i = 0; i<layers;i++) {
				ArrayList<Point2D> layer = layersMap.get(i);
				ArrayList<Point2D> interpolated = new ArrayList<>();
				for(int j = 0; j < layer.size()-1; j++) {
					interpolated.add(Util.lerp(delta, layer.get(j), layer.get(j+1)));
				}
				layersMap.put(i+1, interpolated);
			}
			result.add(layersMap.get(layers).get(0));
			layersMap.clear();
		}
		result.add(dots.get(dots.size()-1));

		for(int i = 0; i < result.size()-1; i++) {
			Point2D a = result.get(i);
			Point2D b = result.get(i+1);

			cg.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
		}
		result.clear();

		//Polyline line = new Polyline();
		//line.getPoints().addAll((Double[]) result.stream().map(element -> new Double[] {element.getX(), element.getY()}).toArray());

	}

	public void onMouseDragged(MouseEvent event) {
		if(tabPane.getSelectionModel().getSelectedItem()==curveTab) {
			if(!modeBox.getValue().equalsIgnoreCase("Move")) {
				return;
			}
			if(dragged<0) {
				return;
			}
			if(inBounds(event.getX(), event.getY())) {
				dots.set(dragged, new Point2D(event.getX(), event.getY()));
			}else {
				dots.set(dragged, getBordered(event.getX(), event.getY()));
			}
		}
	}
	private boolean inBounds(double x, double y) {
		if(x<5||y<5) {
			return false;
		}
		if(x+5>canvasCurve.getWidth()||y+5>canvasCurve.getHeight()) {
			return false;
		}
		return true;
	}
	private Point2D getBordered(double x, double y) {
		if(x<5) x = 6;
		if(y<5) y = 6;
		if(x+5>canvasCurve.getWidth()) x = canvasCurve.getWidth()-6;
		if(y+5>canvasCurve.getHeight()) y = canvasCurve.getHeight()-6;
		return new Point2D(x,y);
	}
	public void onMousePressed(MouseEvent event) {
		if(!modeBox.getValue().equalsIgnoreCase("Move")) {
			return;
		}
		if(dragged<0) {
			int dot = findDot(event.getX(), event.getY());
			if(dot<0) {
				return;
			}
			dragged = dot;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("init");
		cg = canvasCurve.getGraphicsContext2D();
		deltaText.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					double t = Double.valueOf(newValue);
					if(t>0&&t<1) deltaCurve = t;

				}catch (Exception e) {
					deltaText.setText(deltaCurve+"");
				}
			}

		});



		sg = canvasSurface.getGraphicsContext2D();
		Random r = new Random(System.currentTimeMillis());

		List<List<Point3D>> t = new ArrayList<>();
		for(int i = 0; i < 5; i++) {
			ArrayList<Point3D> l = new ArrayList<>();
			for(int j = 0; j < 5; j++) {
				l.add(new Point3D(i*10,j*10,r.nextDouble()*50));
			}
			t.add(l);
		}
		surface = new BezierSurface(t);
		camera = new Camera(canvasSurface.getHeight()/canvasSurface.getWidth());

		new Thread(this).start();
	}

	private void removeDot(double x, double y) {
		for(int i = 0; i < dots.size(); i++) {
			Point2D dot = dots.get(i);
			if(Math.pow(x-dot.getX(),2)+Math.pow(y-dot.getY(), 2)<25) {
				dots.remove(i);
				i--;
			}
		}
	}
	private int findDot(double x, double y) {
		for(int i = 0; i < dots.size(); i++) {
			Point2D dot = dots.get(i);
			if(Math.pow(x-dot.getX(),2)+Math.pow(y-dot.getY(), 2)<25) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void run() {
		long lastTime = System.currentTimeMillis();
		long delta;

		while(running) {

			delta = System.currentTimeMillis() - lastTime;
			if(delta<60) {
				continue;
			}
			lastTime = System.currentTimeMillis();
			Platform.runLater(this::render);
		}
	}
}
