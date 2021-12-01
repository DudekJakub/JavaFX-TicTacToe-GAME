import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Random;

public class TicTacToeGameWithAi extends Application {

    BorderPane root = new BorderPane();
    GridPane grid = new GridPane();
    Random random = new Random();
    private Board gameBoard;
    private Line line = new Line();
    private Text text = new Text(" TRWA ROZGRYWKA ");
    private final Button buttonPlayAgain = new Button(" PLAY AGAIN ");
    private boolean enemyTurn = false;
    private boolean playable = true;

    private Parent createContent() {
        root.setPrefSize(650, 750);


        GridPane.setHalignment(buttonPlayAgain, HPos.CENTER);
        grid.add(buttonPlayAgain, 0, 1);
        grid.add(text, 0, 0);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setGridLinesVisible(false);

        text.setFont(Font.font(40));

        root.setBottom(grid);

        gameBoard = new Board(event -> {

                Board.Cell cell = (Board.Cell) event.getSource();
                if(event.getButton() == MouseButton.PRIMARY && cell.getValue() == 0 && playable) {
                    gameBoard.placeMark(new Mark(1, true), new Text("X"), cell.x, cell.y);
                    enemyTurn = true;
                    if(cell.getValue() == 1 && !gameBoard.checkForCombo(gameBoard)) {
                        enemyMove();
                    }
                    if(gameBoard.checkForCombo(gameBoard) && gameBoard.combosPairs.get(0).getValue() == 1) {
                        playable = false;
                        gameBoard.addLineForBoard();
                        text.setText(" KONIEC GRY - WYGRYWA: X ");
                    } else if(gameBoard.checkForCombo(gameBoard) && gameBoard.combosPairs.get(0).getValue() == 2) {
                        playable = false;
                        gameBoard.addLineForBoard();
                        text.setText(" KONIEC GRY - WYGRYWA: O ");
                    } else if(gameBoard.allCellsList.size() == 9) {
                        text.setText(" REMIS ");
                    }
                }
                if(event.getButton() == MouseButton.SECONDARY) {
                    System.out.println(gameBoard.allCellsList);
                }

                if(event.getButton() == MouseButton.MIDDLE) {

                }
            enemyTurn = false;
        });

        buttonPlayAgain.setOnAction(event -> {
            playAgain();
        });

        gameBoard.addCombos();

        setFirstMove();

        VBox vBox = new VBox(gameBoard);
        vBox.setAlignment(Pos.CENTER);

        root.setCenter(vBox);

        return root;
    }

    private void enemyMove() {
        while (enemyTurn) {
            int x = random.nextInt(3);
            int y = random.nextInt(3);

            Board.Cell cell = gameBoard.getCell(x, y);
            if(cell.wasMarked) {
                gameBoard.placeMark(new Mark(1, true), new Text("X"), x, y);
            }
            else {
                gameBoard.placeMark(new Mark(1, true), new Text("O"), x, y);
                break;
            }
        }
    }

    private void setFirstMove() {
        int x = random.nextInt(3);
        int y = random.nextInt(3);

        Board.Cell cell = gameBoard.getCell(x, y);
        gameBoard.placeMark(new Mark(1, true), new Text("O"), cell.x, cell.y);
    }

    private void playAgain() {
        for (Text txt : gameBoard.getTextList()) {
            txt.setText("");
        }
        for (Board.Cell cell : gameBoard.getCombos()) {
            cell.setValue(0);
            cell.wasMarked = false;
            cell.mark = null; //put mark X or O available again
        }
        gameBoard.allCellsList.clear();
        gameBoard.combosPairs.clear();
        playable = true;
        text.setText(" TRWA ROZGRYWKA ");
        gameBoard.removeLineInBoard();
        setFirstMove();
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        scene.setFill(Color.BURLYWOOD);
        primaryStage.setTitle("TicTacToe GAME with AI");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
