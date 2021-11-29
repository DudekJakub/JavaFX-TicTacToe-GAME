import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class TicTacToeGameWithAi extends Application {

    BorderPane root = new BorderPane();
    GridPane grid = new GridPane();
    private Board gameBoard;
    private boolean enemyTurn = false;
    private final Button buttonPlayAgain = new Button("PLAY AGAIN");
    private Text text = new Text("TRWA ROZGRYWKA...");

    private Parent createContent() {
        root.setPrefSize(800, 800);
        root.setRight(new Text("TicTacToe  \nJakub Dudek  "));
        root.setTop(new Text("PLAYER - X  \nCOMPUTER - O"));

        grid.add(buttonPlayAgain, 0, 1);
        grid.add(text, 0, 0);
        grid.setAlignment(Pos.BASELINE_CENTER);
        grid.setPadding(new Insets(10,10,10, 10));
        grid.setGridLinesVisible(false);

        text.setFont(Font.font(48));

        root.setBottom(grid);

        gameBoard = new Board(event -> {

                Board.Cell cell = (Board.Cell) event.getSource();
                if(event.getButton() == MouseButton.PRIMARY && cell.getValue() == 0) {
                    gameBoard.placeMark(new Mark(1, true), new Text("X"), cell.x, cell.y);
                    enemyTurn = true;
                    if(cell.getValue() == 1 && !gameBoard.checkForCombo(gameBoard)) {
                        enemyMove();
                    }
                    if(gameBoard.checkForCombo(gameBoard) && gameBoard.combosPairs.get(0).getValue() == 1) {
                        text.setText("   KONIEC GRY - WYGRYWA ' X '   ");
                    } else if(gameBoard.checkForCombo(gameBoard) && gameBoard.combosPairs.get(0).getValue() == 2) {
                        text.setText("   KONIEC GRY - WYGRYWA ' O '   ");
                    }
                }
                enemyTurn = false;

            if(event.getButton() == MouseButton.SECONDARY){
                System.out.println(gameBoard.comboProcesor(gameBoard, cell.getCellID()));
            }
            if(event.getButton() == MouseButton.MIDDLE){
                System.out.println(gameBoard.combos.size());
                System.out.println(gameBoard.combos);
            }
        });

        gameBoard.addCombos();

        int x = random.nextInt(3);
        int y = random.nextInt(3);
        Board.Cell cell = gameBoard.getCell(x, y);
        gameBoard.placeMark(new Mark(1, true), new Text("O"), cell.x, cell.y);

        VBox vBox = new VBox(gameBoard);
        vBox.setAlignment(Pos.CENTER);

        root.setCenter(vBox);

        return root;
    }

    Random random = new Random();

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

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("TicTacToe GAME with AI");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
