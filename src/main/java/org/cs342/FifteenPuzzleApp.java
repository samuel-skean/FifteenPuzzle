package org.cs342;

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
		
		Parent root = FXMLLoader.load(getClass().getResource("/org/cs342/FXML/WelcomeScreen.fxml"));
		Scene scene = new Scene(root, 400,400);
		scene.getStylesheets().add(getClass().getResource("/org/cs342/styles/style1.css").toExternalForm());
	
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}
