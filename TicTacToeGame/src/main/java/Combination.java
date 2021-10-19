import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Combination extends Tile{

    private Tile[] tiles;
    private Line line = new Line();
    public Timeline timeline = new Timeline();

    public Line getLine() {
        return line;
    }

    public Combination(Tile... tiles) {
        super(200, 200, Pos.CENTER);
        this.tiles = tiles;
    }

    public boolean isComplete() {
        if (tiles[0].getValue().isEmpty())
            return false;

        return tiles[0].getValue().equals(tiles[1].getValue()) && tiles[0].getValue().equals(tiles[2].getValue());
    }

    public void playWinAnimation(Combination combination) {
        line.setStartX(combination.tiles[0].getCenterX());
        line.setStartY(combination.tiles[0].getCenterY());
        line.setEndX(combination.tiles[0].getCenterX());
        line.setEndY(combination.tiles[0].getCenterY());

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new KeyValue(line.endXProperty(), combination.tiles[2].getCenterX()),
                new KeyValue(line.endYProperty(), combination.tiles[2].getCenterY())));
        timeline.play();
    }

}
