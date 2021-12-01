import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class Board extends Parent {

    private final VBox rows = new VBox();
    private Text text = new Text();
    public final boolean wasMarked = false;
    public Cell[][] comboBoard = new Cell[3][3];
    public List<Cell> combos = new ArrayList<>();
    public List<Cell> combosPairs = new ArrayList<>();
    public List<Cell> allCellsList = new ArrayList<>();
    public List<Text> textList = new ArrayList<>();
    public Line line = new Line();

    public Board(EventHandler<? super MouseEvent> handler) {

        for (int y = 0; y<3; y++) {
            HBox row = new HBox();
            for (int x = 0; x<3; x++) {
                Cell c = new Cell(x, y, this, 200, 200, 0);
                c.setOnMouseClicked(handler);
                c.border.setStroke(Color.DARKRED);
                c.border.setStrokeWidth(2);
                c.setTranslateX(x*1.5);
                c.setTranslateY(y*1.5);
                row.getChildren().add(c);
                comboBoard[x][y] = c;
            }
            rows.getChildren().add(row);
        }
        getChildren().add(rows);
    }

    public class Cell extends StackPane {

        public int x, y;
        public Mark mark = null;
        public boolean wasMarked = ifWasMarked();
        public int value;
        private final Rectangle border;
        private final int width;
        private final int height;
        private Board board;

        public Cell(int x, int y, Board board, int width, int height, int value) {
            this.x = x;
            this.y = y;
            this.board = board;
            this.width = width;
            this.height = height;
            this.value = value;

            border = new Rectangle(width, height);
            border.setFill(Color.LIGHTGRAY);
            border.setStroke(Color.BLACK);

            getChildren().add(border);
        }

        public int getValue() {
            return value;
        }

        public int setLineXPos() {
            return (this.width / 2) + 1;
        }

        public int setLineYPos() {
            return (this.height / 2) + 7;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public void markCell() {
            wasMarked = true;
        }
    }

    public Cell getCell(int x, int y) {
        return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
    }

    public List<Text> getTextList() {
        return textList;
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 3 && y >= 0 && y < 3;
    }

    public boolean ifWasMarked() {
        return wasMarked;
    }

    public void placeMark(Mark mark, Text text, int x, int y) {
        this.text = text;

        if(canPlaceMark(mark, x, y)) {
            int checkEmptiness = mark.isFull;

            if(mark.horizontal)
                for (int i = x; i < x + checkEmptiness; i++) {
                    Cell cell = getCell(i, y);
                    cell.mark = mark;
                    cell.getChildren().add(text);
                    cell.markCell();
                    text.setFont(Font.font(72));
                    textList.add(text);
                    allCellsList.add(cell);
                    if (text.getText().equals("X")) {
                        cell.setValue(1);
                    } else cell.setValue(2);
                }
            }
        }

    public boolean canPlaceMark(Mark mark, int x, int y) {
        int checkEmptiness = mark.isFull;

        if(mark.horizontal)
            for (int i = x; i < x + checkEmptiness; i++) {
                if (!isValidPoint(i, y))
                    return false;

                Cell cell = getCell(i, y);
                if (cell.mark != null)
                    return false;
            }
        return true;
    }

    public void addLineForBoard() {
        this.getChildren().add(line);
    }

    public void removeLineInBoard() {
        this.getChildren().remove(line);
    }

    public void addCombos() {

        //horyzontalnie
        for (int y = 0; y < 3; y++) {
            combos.add(comboBoard[0][y]);
            combos.add(comboBoard[1][y]);
            combos.add(comboBoard[2][y]);
        }

        //wertykalnie
        for (int x = 0; x < 3; x++) {
            combos.add(comboBoard[x][0]);
            combos.add(comboBoard[x][1]);
            combos.add(comboBoard[x][2]);
        }
    }

    public List<Cell> getCombos() {
        return combos;
    }

    public int comboProcessor(Board board, int index) {
        return board.getCombos().get(index).getValue();
    }

    public void playWinAnimation(Board board, int xPos1, int yPos1, int xPos2, int yPos2) {
        line.setStrokeWidth(2);
        line.setStartX(board.getCombos().get(0).setLineXPos() + xPos1);
        line.setEndX(board.getCombos().get(0).setLineXPos() + xPos1);
        line.setStartY(board.getCombos().get(0).setLineYPos() + yPos1);
        line.setEndY(board.getCombos().get(0).setLineYPos() + yPos1);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2), new KeyValue(line.endXProperty(), board.getCombos().get(0).setLineXPos() + xPos1 +xPos2),
                                    new KeyValue(line.endYProperty(), board.getCombos().get(0).setLineYPos() + yPos1 + yPos2)));
        timeline.play();
    }

    public boolean checkForCombo(Board board) {
                if(board.comboProcessor(board, 0) == 1 && board.comboProcessor(board, 1) == 1 && board.comboProcessor(board, 2) == 1 ||
                   board.comboProcessor(board, 0) == 2 && board.comboProcessor(board, 1) == 2 && board.comboProcessor(board, 2) == 2) {

                    combosPairs.add(board.getCombos().get(0));
                    playWinAnimation(board, 0, 0, 400, 0);
                    return true;
                } else if(board.comboProcessor(board, 3) == 1 && board.comboProcessor(board, 4) == 1 && board.comboProcessor(board, 5) == 1 ||
                          board.comboProcessor(board, 3) == 2 && board.comboProcessor(board, 4) == 2 && board.comboProcessor(board, 5) == 2) {

                    combosPairs.add(board.getCombos().get(3));
                    playWinAnimation(board, 0, 200, 400, 0);
                    return true;
                } else if(board.comboProcessor(board, 6) == 1 && board.comboProcessor(board, 7) == 1 && board.comboProcessor(board, 8) == 1 ||
                          board.comboProcessor(board, 6) == 2 && board.comboProcessor(board, 7) == 2 && board.comboProcessor(board, 8) == 2) {

                    combosPairs.add(board.getCombos().get(6));
                    playWinAnimation(board, 0, 402, 400, 0);
                    return true;
                } else if(board.comboProcessor(board, 0) == 1 && board.comboProcessor(board, 3) == 1 && board.comboProcessor(board, 6) == 1 ||
                          board.comboProcessor(board, 0) == 2 && board.comboProcessor(board, 3) == 2 && board.comboProcessor(board, 6) == 2) {

                    combosPairs.add(board.getCombos().get(0));
                    playWinAnimation(board, 2, 0, 0, 400);
                    return true;
                } else if(board.comboProcessor(board, 1) == 1 && board.comboProcessor(board, 4) == 1 && board.comboProcessor(board, 7) == 1 ||
                          board.comboProcessor(board, 1) == 2 && board.comboProcessor(board, 4) == 2 && board.comboProcessor(board, 7) == 2) {

                    combosPairs.add(board.getCombos().get(1));
                    playWinAnimation(board, 202, 0, 0, 400);
                    return true;
                } else if(board.comboProcessor(board, 2) == 1 && board.comboProcessor(board, 5) == 1 && board.comboProcessor(board, 8) == 1 ||
                          board.comboProcessor(board, 2) == 2 && board.comboProcessor(board, 5) == 2 && board.comboProcessor(board, 8) == 2) {

                    combosPairs.add(board.getCombos().get(2));
                    playWinAnimation(board, 403, 0, 0, 400);
                    return true;
                } else if(board.comboProcessor(board, 0) == 1 && board.comboProcessor(board, 4) == 1 && board.comboProcessor(board, 8) == 1 ||
                          board.comboProcessor(board, 0) == 2 && board.comboProcessor(board, 4) == 2 && board.comboProcessor(board, 8) == 2) {

                    combosPairs.add(board.getCombos().get(0));
                    playWinAnimation(board, 0, 0, 400, 400);
                    return true;
                } else if(board.comboProcessor(board, 2) == 1 && board.comboProcessor(board, 4) == 1 && board.comboProcessor(board, 6) == 1 ||
                          board.comboProcessor(board, 2) == 2 && board.comboProcessor(board, 4) == 2 && board.comboProcessor(board, 6) == 2) {

                    combosPairs.add(board.getCombos().get(2));
                    playWinAnimation(board, 403, 0, -396, 400);
                    return true;
                }
                return false;
    }
}

