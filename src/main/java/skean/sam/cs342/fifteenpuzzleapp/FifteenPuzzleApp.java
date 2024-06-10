package skean.sam.cs342.fifteenpuzzleapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class FifteenPuzzleApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("15-Puzzle Game");
		
		Parent root = FXMLLoader.load(getClass().getResource("/skean/sam/cs342/fifteenpuzzleapp/FXML/WelcomeScreen.fxml"));
		Scene scene = new Scene(root, 400,400);
		scene.getStylesheets().add(getClass().getResource("/skean/sam/cs342/fifteenpuzzleapp/styles/style1.css").toExternalForm());
	
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
