package application;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXMLLoader;


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
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
