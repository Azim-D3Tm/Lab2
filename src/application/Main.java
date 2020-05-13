package application;
	
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.geometry.BezierSurface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;


public class Main extends Application {
	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Sample.fxml"));
			
			Parent root = loader.load();
			Scene scene = new Scene(root,1200,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			primaryStage.setOnCloseRequest(event->{
				Platform.exit();
				System.exit(0);
			});
			ComboBox<String> box = (ComboBox<String>) scene.lookup("#modeBox");
			//SampleController controller = loader.getController();
			//controller.run();
			
			box.getItems().addAll("Add", "Move", "Remove");
			box.setValue("Add");
			
			Random r = new Random(System.currentTimeMillis());
			
			List<List<Point3D>> t = new ArrayList<>();
			for(int i = 0; i < 5; i++) {
				ArrayList<Point3D> l = new ArrayList<>();
				for(int j = 0; j < 5; j++) {
					l.add(new Point3D(i*10,j*10,r.nextDouble()%50));
				}
				t.add(l);
			}
			BezierSurface surface = new BezierSurface(t);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
