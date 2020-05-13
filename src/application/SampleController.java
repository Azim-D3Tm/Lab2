package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import application.geometry.Util;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SampleController implements Initializable, Runnable{
	@FXML
	private Canvas canvas;
	@FXML 
	private ComboBox<String> modeBox;
	@FXML
	private TextField deltaText;
	@FXML
	private Tab curveTab;
	@FXML
	private Tab surfaceTab;
	@FXML
	private TabPane tabPane;
	
	private GraphicsContext cg;
	
	private ArrayList<Point2D> dots = new ArrayList<>();
	private int dragged = -1;
	private double deltaCurve = 0.05;
	
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
		
	}

	public void renderCurve() {
		cg.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
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
		}
		result.add(dots.get(dots.size()-1));
		
		for(int i = 0; i < result.size()-1; i++) {
			Point2D a = result.get(i);
			Point2D b = result.get(i+1);
			
			cg.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
		}
		//Polyline line = new Polyline();
		//line.getPoints().addAll((Double[]) result.stream().map(element -> new Double[] {element.getX(), element.getY()}).toArray());
		
	}
	
	
	public void onMouseDragged(MouseEvent event) {
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
	
	private boolean inBounds(double x, double y) {
		if(x<5||y<5) {
			return false;
		}
		if(x+5>canvas.getWidth()||y+5>canvas.getHeight()) {
			return false;
		}
		return true;
	}
	
	private Point2D getBordered(double x, double y) {
		if(x<5) x = 6;
		if(y<5) y = 6;
		if(x+5>canvas.getWidth()) x = canvas.getWidth()-6;
		if(y+5>canvas.getHeight()) y = canvas.getHeight()-6;
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
		cg = canvas.getGraphicsContext2D();
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
        	if(delta<30) {
        		continue;
        	}
        	lastTime = System.currentTimeMillis();
        	render();
        }
	}
}