import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class WelcomeScreenController implements Initializable {
	
	@FXML
	Parent root;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		PauseTransition pause = new PauseTransition(Duration.seconds(4));
		pause.play();
		pause.setOnFinished(e -> newGame() );

	}
	
	private void newGame() {
		try {
			Parent newRoot = FXMLLoader.load(getClass().getResource("/FXML/GameScreen.fxml"));
			root.getScene().setRoot(newRoot);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
