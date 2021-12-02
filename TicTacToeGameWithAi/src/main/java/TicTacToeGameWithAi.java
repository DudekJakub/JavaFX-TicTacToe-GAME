import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Random;

public class TicTacToeGameWithAi extends Application {

    BorderPane root = new BorderPane();
    GridPane welcomeScreen = new GridPane();
    GridPane grid = new GridPane();
    Random random = new Random();
    private Stage primaryStage;
    private Board gameBoard;
    private Slider slider;
    private Label label;
    private Label winCounter = new Label();
    private final Text emptyRow = new Text("");
    private final Text leftTop = new Text(" JAKUB DUDEK - INDIVIDUAL PROJECT FOR KODILLA COURSE");
    private final Text rightTop = new Text("DATE: 02-12-2021 \n     VERSION: 1.0");
    private final Text welcomeTxt1 = new Text();
    private final Text welcomeTxt2 = new Text();
    private Text illegalMove = new Text("");
    private Text text = new Text("     GAME IN PROGRESS      ");
    private final Button buttonStart = new Button("START GAME");
    private final Button buttonNextRound = new Button(" NEXT ROUND ");
    private boolean enemyTurn = false;
    private boolean playable = true;
    private int roundNumber = 1;
    private int roundCounter = 1;
    private int xWin = 0;
    private int oWin = 0;

    private Parent createContent() {
        root.setPrefSize(650, 850);
        root.setBackground(new Background(new BackgroundFill(Color.BURLYWOOD, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().add(welcomeScreen);
        root.setLeft(leftTop);
        root.setRight(rightTop);

        welcomeScreen.getColumnConstraints().add(new ColumnConstraints(650));
        welcomeScreen.getRowConstraints().add(new RowConstraints(50));
        welcomeScreen.add(welcomeTxt1, 0, 0);
        welcomeScreen.add(welcomeTxt2, 0, 1);
        welcomeScreen.setPadding(new Insets(20, 0, 0, 0));
        welcomeScreen.setVgap(30);

        GridPane.setHalignment(welcomeTxt1, HPos.CENTER);
        GridPane.setHalignment(welcomeTxt2, HPos.CENTER);
        GridPane.setHalignment(illegalMove, HPos.CENTER);

        leftTop.setFont(Font.font("Aharoni", FontWeight.BOLD, 11));
        rightTop.setFont(Font.font("Aharoni", FontWeight.BOLD, 11));
        illegalMove.setFont(Font.font("Aharoni", FontWeight.BOLD, 15));

        welcomeTxt1.setFont(Font.font("ALGERIAN", 40));
        welcomeTxt1.setText("\n\n\n\n WELCOME! ");

        welcomeTxt2.setFont(Font.font("Aharoni", 23));
        welcomeTxt2.setWrappingWidth(610);
        welcomeTxt2.setText("\n\n\n\nI hereby invite you to test my first, self-created application, which is the game TicTacToe. " +
                            "I put a lot of work into this production and although it seems to be a very simple compilation, in fact, " +
                            "to achieve it, I had to master a lot of basics related to the javaFX environment. " +
                            "I had a lot of fun doing it and I hope you will too! " +
                            "\n\nBefore the game starts, I will only ask you for the number of rounds you would like to play:");

        slider = createSlider();
        label = new Label("Rounds number: 1");
        label.setFont(Font.font("Aharoni", FontWeight.BOLD, 20));

        welcomeScreen.add(slider, 0, 2, 1, 1);
        welcomeScreen.add(label, 0, 3, 1, 1);
        welcomeScreen.add(buttonStart, 0, 4, 1, 1);

        GridPane.setHalignment(slider, HPos.CENTER);
        GridPane.setHalignment(label, HPos.CENTER);
        GridPane.setHalignment(buttonStart, HPos.CENTER);

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                roundNumber = (int) t1.doubleValue();
                label.setText("Rounds number: " + roundNumber);
            }
        });

        buttonStart.setOnAction(actionEvent -> {
            root.getChildren().removeAll(welcomeScreen, leftTop, rightTop);
            root.setBottom(grid);
            label.setText("Round " + roundCounter + " of " + roundNumber);
            grid.add(label, 0, 2);

            if(roundNumber > 1) {
                grid.add(buttonNextRound, 0, 3);
            }

            VBox vBox = new VBox(gameBoard);
            vBox.setAlignment(Pos.CENTER);
            root.setCenter(vBox);
        });

        GridPane.setHalignment(buttonNextRound, HPos.CENTER);
        GridPane.setHalignment(winCounter, HPos.CENTER);

        grid.add(text, 0, 0);
        grid.add(winCounter, 0, 1);
        grid.add(illegalMove, 0, 5);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        winCounter.setFont(Font.font("Aharoni", FontWeight.BOLD, 20));
        winCounter.setText("SCORE: (player) X:  " + xWin + " vs " + oWin + "  :O (AI)");
        text.setFont(Font.font("Aharoni", 40));


        //MAIN SCENE STARTS
            gameBoard = new Board(event -> {

                Board.Cell cell = (Board.Cell) event.getSource();
                if(event.getButton() == MouseButton.PRIMARY && cell.getValue() == 0 && playable) {
                    gameBoard.placeMark(new Mark(1, true), new Text("X"), cell.x, cell.y);
                    enemyTurn = true;
                    if(cell.getValue() == 1 && !gameBoard.checkForCombo(gameBoard)) {
                        enemyMove();
                    }
                    if(gameBoard.checkForCombo(gameBoard) && gameBoard.combosPairs.get(0).getValue() == 1) {
                        xWin++;
                        playable = false;
                        gameBoard.addLineForBoard();
                        text.setText(" ROUND ENDS - WINS: X ");
                        checkRoundStatus();
                    } else if(gameBoard.checkForCombo(gameBoard) && gameBoard.combosPairs.get(0).getValue() == 2) {
                        oWin++;
                        playable = false;
                        gameBoard.addLineForBoard();
                        text.setText(" ROUND ENDS - WINS: O ");
                        checkRoundStatus();
                    } else if(gameBoard.allCellsList.size() == 9) {
                        text.setText("         DRAW!           ");
                        checkRoundStatus();
                    }
                }
                winCounter.setText("SCORE: (player) X:  " + xWin + " vs " + oWin + "  :O (AI)");

                enemyTurn = false;
            });

            buttonNextRound.setOnAction(event -> {
                nextRound();
            });

            gameBoard.addCombos();

            setFirstMove();

            return root;
        }
    //MAIN SCENE ENDS


    //METHODS
    private Slider createSlider() {
        Slider slider = new Slider(1, 10, 1);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);

        slider.setPrefWidth(500);
        slider.setMinWidth(500);
        slider.setMaxWidth(500);

        return slider;
    }

    private void setFirstMove() {
        int x = random.nextInt(3);
        int y = random.nextInt(3);

        Board.Cell cell = gameBoard.getCell(x, y);
        gameBoard.placeMark(new Mark(1, true), new Text("O"), cell.x, cell.y);
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

    private void checkRoundStatus() {
        playable = false;

        if(roundCounter == roundNumber && gameBoard.checkForCombo(gameBoard) ||
           roundNumber == 1 && gameBoard.checkForCombo(gameBoard)) {
            grid.getChildren().removeAll(text, label);
            grid.add(label, 0, 0);
            grid.addRow(8, emptyRow);
            label.setTextFill(Color.DARKRED);
            label.setFont(Font.font(40));

            if(roundNumber == 1) {
                grid.add(buttonNextRound, 0, 3);
                addExitOption();
            }
            if(xWin > oWin) {
                label.setText(" GAME OVER - WINS: X ");
                addExitOption();
            } else if(oWin > xWin){
                label.setText(" GAME OVER - WINS: O ");
                addExitOption();
            } else {
                label.setText(" GAME OVER - DRAW! ");
                addExitOption();
            }
        }
    }

    private void nextRound() {
        if(gameBoard.checkForCombo(gameBoard) || gameBoard.allCellsList.size() == 9) {
            roundCounter++;
            label.setText("Round " + roundCounter + " of " + roundNumber);

            illegalMove.setText("");

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
            text.setText(" GAME IN PROGRESS ");
            gameBoard.removeLineInBoard();
            setFirstMove();
        } else {
            illegalMove.setStroke(Color.DARKRED);
            illegalMove.setText("COMPLETE THE CURRENT ROUND FIRST!");
        }
    }

    private void addExitOption() {
        grid.getChildren().remove(illegalMove);
        buttonNextRound.setText(" EXIT GAME ");
        exitGame(primaryStage);
    }

    private void exitGame(Stage stage) {
        buttonNextRound.setOnAction(event -> {
            stage.close();
        });
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

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
