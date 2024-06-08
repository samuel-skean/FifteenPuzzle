import javafx.scene.control.Button;

public class GameButton extends Button {
	private int xPos, yPos;
	
	GameButton(int x, int y) {
		xPos = x;
		yPos = y;
	}
	
	public int getX() {
		return xPos;
	}
	
	public int getY() {
		return yPos;
	}
	
	
	public void setNumber(int number) {
		if (number == 0) {
			setDisable(true);
			setText("");
			getStyleClass().clear();
			getStyleClass().add("emptyGameSpace");
		} else {
			setDisable(false);
			setText("" + number);
			getStyleClass().clear();
			getStyleClass().add("filledGameSpace");
		}
	}
}
