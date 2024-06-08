import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class FifteenPuzzleAppGUI extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("15-Puzzle Game");
		
		Parent root = FXMLLoader.load(getClass().getResource("/FXML/WelcomeScreen.fxml"));
		Scene scene = new Scene(root, 400,400);
		scene.getStylesheets().add("/styles/style1.css");
	
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
