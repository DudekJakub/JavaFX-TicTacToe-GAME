import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class TicTacToeGame extends Application {

    private Pane root = new Pane();
    private boolean playable = true;
    private boolean turnX = true;
    private Tile[][] board = new Tile[3][3];
    private List<Combination> combos = new ArrayList<>();

    private Parent drawContent() {
        root.setPrefSize(780, 780);

        Button buttonPlayAgain = new Button("Play again?");

        for (int k=1; k<2; k++){
            for (int l=1; l<2; l++) {
                Tile tileBottom = new Tile(600, 150, Pos.CENTER_LEFT);
                tileBottom.setTranslateY(k * 600);

                Tile tileRight = new Tile(150, 600, Pos.CENTER);
                tileRight.setTranslateX(l * 600);

                Tile tileCorner = new Tile(150, 150, Pos.CENTER);
                tileCorner.setTranslateY(l * 600);
                tileCorner.setTranslateX(k * 600);

                tileBottom.getChildren().add(buttonPlayAgain);

                root.getChildren().addAll(tileBottom, tileRight, tileCorner);
            }
        }

        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                Tile tile = new Tile(200, 200, Pos.CENTER);
                tile.setTranslateX(j * 200);
                tile.setTranslateY(i * 200);

                root.getChildren().add(tile);
                board[j][i] = tile;

                tile.setOnMouseClicked(event -> {
                    if (!playable)
                        return;

                    if (event.getButton() == MouseButton.PRIMARY){
                        if (!turnX)
                            return;

                        tile.drawX();
                        turnX = false;
                        checkIfGameIsPlayable();
                    }
                    else if (event.getButton() == MouseButton.SECONDARY){
                        if (turnX)
                            return;

                        tile.drawO();
                        turnX = true;
                        checkIfGameIsPlayable();
                    }
                });
            }
        }

        // horyzontalnie
        for (int y=0; y<3; y++) {
            combos.add(new Combination(board[0][y], board[1][y], board[2][y]));
        }

        // wertykalnie
        for (int x=0; x<3; x++) {
            combos.add(new Combination(board[x][0], board[x][1], board[x][2]));
        }

        // ukośnie
            combos.add(new Combination(board[0][0], board[1][1], board[2][2]));
            combos.add(new Combination(board[2][0], board[1][1], board[0][2]));

        return root;
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        mainStage.setScene(new Scene(drawContent()));
        mainStage.show();
    }

    private void checkIfGameIsPlayable() {
        for (Combination combo : combos) {
            if (combo.isComplete()) {
                playable = false;
                root.getChildren().add(combo.getLine());
                combo.playWinAnimation(combo);

                break;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
