import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tile extends StackPane {

    private Text text = new Text();
    int width;
    int height;
    Pos pos;

    public Tile(int width, int height, Pos pos) {
        this.width = width;
        this.height = height;
        this.pos = pos;

        Rectangle border = new Rectangle(width, height);
        border.setFill(null);
        border.setStroke(Color.BLACK);

        text.setFont(Font.font(72));

        setAlignment(pos);
        getChildren().addAll(border, text);
    }

    public double getCenterX() {
        return getTranslateX() + 200/2;
    }

    public double getCenterY() {
        return getTranslateY() + 200/2;
    }

    public String getValue() {
        return text.getText();
    }

    public void drawX() {
        text.setText("X");
    }

    public void drawO() {
        text.setText("O");
    }
}
