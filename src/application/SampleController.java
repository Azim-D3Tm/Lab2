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
import application.geometry.Line2D;
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
import javafx.scene.control.CheckBox;
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

	private List<Point2D> dots = new ArrayList<>();
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
	@FXML
	private CheckBox paintFaces;


	private GraphicsContext sg;

	private BezierSurface surface;
	private Camera camera;

	
	//lab 4, segments
	@FXML
	private Tab lab4Tab;
	@FXML
	private Canvas canvaslab4;
	@FXML
	private ComboBox<String> segmentsBox;
	@FXML
	private TextField segmentsAmount;
	private GraphicsContext l4g;
	
	private List<Line2D> segments;
	private List<Point2D> figure;
	private int fdragged = -1;

	public boolean running = true;

	public SampleController() {
	}

	public void onMouseClicked(MouseEvent event) {
		if(tabPane.getSelectionModel().getSelectedItem()==curveTab) {
			switch(modeBox.getValue()) {
			case "Add":
				dots.add(new Point2D(event.getX(), event.getY()));
				break;
			case "Move":
				dragged = -1;
				break;
			case "Remove":
				dots = removeDot(dots,event.getX(), event.getY());
				break;
			default:
				break;
			}
		}else {
			switch(segmentsBox.getValue()) {
			case "Add":
				figure.add(new Point2D(event.getX(), event.getY()));
				break;
			case "Move":
				fdragged = -1;
				break;
			case "Remove":
				figure = removeDot(figure, event.getX(), event.getY());
				break;
			default:
				break;
			}
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
		}else if(tabPane.getSelectionModel().getSelectedItem()==surfaceTab){
			renderSurface();
		}else {
			renderLab4();
		}
	}

	private void renderLab4() {
		l4g.clearRect(0, 0, canvaslab4.getWidth(), canvaslab4.getHeight());
		for(Line2D segment:segments) {
			if(Util.intersects(figure, segment)) {
				l4g.setStroke(Color.RED);
			}else {
				l4g.setStroke(Color.GREEN);
			}
			l4g.strokeLine(segment.a.getX(), segment.a.getY(), segment.b.getX(), segment.b.getY());
		}
		l4g.setStroke(Color.BLACK);
		for(int i = 0; i < figure.size()-1; i++) {
			l4g.strokeLine(figure.get(i).getX(), figure.get(i).getY(), figure.get(i+1).getX(), figure.get(i+1).getY());
		}
		l4g.strokeLine(figure.get(figure.size()-1).getX(), figure.get(figure.size()-1).getY(), figure.get(0).getX(), figure.get(0).getY());
		
		for(int i = 0; i < figure.size(); i++) {
			if(i==fdragged)l4g.setFill(Color.GREEN);
			l4g.fillOval(figure.get(i).getX()-5, figure.get(i).getY()-5, 10, 10);
			l4g.setFill(Color.BLACK);
		}
	}
	public void generateSegments(ActionEvent event) {
		int amount = segments.size();
		try {
			amount = Integer.valueOf(segmentsAmount.getText());
		}catch(Exception e){
			e.printStackTrace();
			segmentsAmount.setText(amount+"");
		}
		Random r = new Random(System.currentTimeMillis());
		double width = canvaslab4.getWidth(), height = canvaslab4.getHeight();
		segments.clear();
		for(int i = 0; i < amount; i++) {
			segments.add(new Line2D(
					new Point2D(r.nextDouble()*width, r.nextDouble()*height),
					new Point2D(r.nextDouble()*width, r.nextDouble()*height)
					));
		}
	}
	public void resetShape(ActionEvent event) {
		figure.clear();
		figure.add(new Point2D(500,500));
		figure.add(new Point2D(700,300));
		figure.add(new Point2D(600,200));
		figure.add(new Point2D(400,200));
		figure.add(new Point2D(300,300));
	}
	public void onFMousePressed(MouseEvent event) {
		if(!segmentsBox.getValue().equalsIgnoreCase("Move")) {
			return;
		}
		if(fdragged<0) {
			int dot = findDot(figure, event.getX(), event.getY());
			if(dot<0) {
				return;
			}
			fdragged = dot;
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
						t.frontColor,
						t.backColor);
			}
			double roty = getRotationAngleY();
			if(roty!=0) {
				Matrix ry = Matrix.rotationMatrix(0, roty, 4);
				t = new Triangle(
						Util.multiplyByMatrix(t.p1, ry),
						Util.multiplyByMatrix(t.p2, ry),
						Util.multiplyByMatrix(t.p3, ry),
						t.lineColor,
						t.frontColor,
						t.backColor);
			}
			Triangle translated = new Triangle(
					Util.multiplyByMatrix(t.p1, cameraMatrix),
					Util.multiplyByMatrix(t.p2, cameraMatrix),
					Util.multiplyByMatrix(t.p3, cameraMatrix),
					t.lineColor,
					t.frontColor,
					t.backColor);

			if(translated.p1.getZ()<0||translated.p2.getZ()<0||translated.p3.getZ()<0) {
				continue;
			}

			Triangle projected = new Triangle(
					Util.multiplyByMatrix(translated.p1, camera.projectionMatrix),
					Util.multiplyByMatrix(translated.p2, camera.projectionMatrix),
					Util.multiplyByMatrix(translated.p3, camera.projectionMatrix),
					translated.lineColor,
					translated.frontColor,
					translated.backColor);

			projected.shiftToView(canvasSurface.getWidth(), canvasSurface.getHeight());
			
			if(paintFaces.isSelected()) {
				double dot = t.getNormal().dotProduct(t.p1.subtract(camera.location));
				if(dot<0) {
					sg.setFill(t.frontColor);
				}else {
					sg.setFill(t.backColor);
				}
				//System.out.println(dot);
				
				sg.fillPolygon(projected.getXPoints(), projected.getYPoints(), 3);
			}
			
			sg.setStroke(t.lineColor);
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
			Map<Integer, List<Point2D>> layersMap = new HashMap<>();
			layersMap.put(0, dots);
			for(int i = 0; i<layers;i++) {
				List<Point2D> layer = layersMap.get(i);
				List<Point2D> interpolated = new ArrayList<>();
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
				dots.set(dragged, getBordered(event.getX(), event.getY(),canvasCurve.getWidth(),canvasCurve.getHeight()));
			}
		}else {
			if(!segmentsBox.getValue().equalsIgnoreCase("Move")) {
				return;
			}
			if(fdragged<0) {
				return;
			}
			if(inBounds(event.getX(), event.getY())) {
				figure.set(fdragged, new Point2D(event.getX(), event.getY()));
			}else {
				figure.set(fdragged, getBordered(event.getX(), event.getY(),canvaslab4.getWidth(),canvaslab4.getHeight()));
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
	private Point2D getBordered(double x, double y, double width, double height) {
		if(x<5) x = 6;
		if(y<5) y = 6;
		if(x+5>width) x = width-6;
		if(y+5>height) y = height-6;
		return new Point2D(x,y);
	}
	public void onMousePressed(MouseEvent event) {
		if(!modeBox.getValue().equalsIgnoreCase("Move")) {
			return;
		}
		if(dragged<0) {
			int dot = findDot(dots, event.getX(), event.getY());
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
		
		l4g = canvaslab4.getGraphicsContext2D();
		figure = new ArrayList<>();
		figure.add(new Point2D(500,500));
		figure.add(new Point2D(700,300));
		figure.add(new Point2D(600,200));
		figure.add(new Point2D(400,200));
		figure.add(new Point2D(300,300));

		double width = canvaslab4.getWidth(), height = canvaslab4.getHeight();
		segments = new ArrayList<>();
		for(int i = 0; i < 30; i++) {
			segments.add(new Line2D(
					new Point2D(r.nextDouble()*width, r.nextDouble()*height),
					new Point2D(r.nextDouble()*width, r.nextDouble()*height)
					));
		}
		new Thread(this).start();
	}

	private List<Point2D> removeDot(List<Point2D>dots, double x, double y) {
		for(int i = 0; i < dots.size(); i++) {
			Point2D dot = dots.get(i);
			if(Math.pow(x-dot.getX(),2)+Math.pow(y-dot.getY(), 2)<25) {
				dots.remove(i);
				i--;
			}
		}
		return dots;
	}
	private int findDot(List<Point2D> dots, double x, double y) {
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
