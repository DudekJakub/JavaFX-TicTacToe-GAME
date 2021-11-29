import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

public class Board extends Parent {

    private final VBox rows = new VBox();
    private Text text = new Text();
    private final boolean wasMarked = false;
    private int cellID = 0;
    public Cell[][] comboBoard = new Cell[3][3];
    public List<Cell> combos = new ArrayList<>();
    public List<Cell> combosPairs = new ArrayList<>();

    public boolean isWasMarked() {
        return wasMarked;
    }

    public Board(EventHandler<? super MouseEvent> handler) {

        for (int y = 0; y < 3; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 3; x++) {
                Cell c = new Cell(x, y, this, 200, 200, 0, 0);
                c.setTranslateX(x);
                c.setTranslateY(y);
                c.setCellID(cellID++);
                c.setOnMouseClicked(handler);
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
        public boolean wasMarked = isWasMarked();
        public int value;
        public int cellID;

        private Rectangle border;
        private int width;
        private int height;
        private Board board;

        public Cell(int x, int y, Board board, int width, int height, int value, int cellID) {
            this.x = x;
            this.y = y;
            this.board = board;
            this.width = width;
            this.height = height;
            this.value = value;
            this.cellID = cellID;

            border = new Rectangle(width, height);

            border.setFill(Color.LIGHTGRAY);
            border.setStroke(Color.BLACK);

            getChildren().add(border);
        }

        public int getValue() {
            return value;
        }

        public int getCenterX() {
            return (int) (getTranslateX() + 100);
        }

        public int getCenterY() {
            return (int) (getTranslateY() + 100);
        }

        public int getCellID() {
            return cellID;
        }

        public void setCellID(int cellID) {
            this.cellID = cellID;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public boolean markCell() {
            wasMarked = true;
            Cell cell = this;

            cell.border.setStroke(Color.DARKRED);

            return wasMarked;
        }
    }

    public Cell getCell(int x, int y) {
        return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 3 && y >= 0 && y < 3;
    }

    public boolean placeMark(Mark mark, Text text, int x, int y) {
        this.text = text;

        if (canPlaceMark(mark, x, y)) {
            int checkEmptiness = mark.isFull;

            if (mark.horizontal)
                for (int i = x; i < x + checkEmptiness; i++) {
                    Cell cell = getCell(i, y);
                    cell.mark = mark;
                    text.setFont(Font.font(72));
                    cell.getChildren().add(text);
                    cell.markCell();
                    if (text.getText().equals("X")) {
                        cell.setValue(1);
                    } else cell.setValue(2);
                }
            return true;
        }
        return false;
    }

    public boolean canPlaceMark(Mark mark, int x, int y) {
        int checkEmptiness = mark.isFull;

        if (mark.horizontal)
            for (int i = x; i < x + checkEmptiness; i++) {
                if (!isValidPoint(i, y))
                    return false;

                Cell cell = getCell(i, y);
                if (cell.mark != null)
                    return false;
            }
        return true;
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

    public int comboProcesor(Board board, int index) {
        return board.getCombos().get(index).getValue();
    }

    public boolean checkForCombo(Board board) {
                if(board.comboProcesor(board, 0) == 1 && board.comboProcesor(board, 1) == 1 && board.comboProcesor(board, 2) == 1 ||
                   board.comboProcesor(board, 0) == 2 && board.comboProcesor(board, 1) == 2 && board.comboProcesor(board, 2) == 2) {
                    combosPairs.add(board.getCombos().get(0));
                    return true;
                } else if(board.comboProcesor(board, 3) == 1 && board.comboProcesor(board, 4) == 1 && board.comboProcesor(board, 5) == 1 ||
                          board.comboProcesor(board, 3) == 1 && board.comboProcesor(board, 4) == 2 && board.comboProcesor(board, 5) == 2) {
                    combosPairs.add(board.getCombos().get(3));
                    return true;
                } else if(board.comboProcesor(board, 6) == 1 && board.comboProcesor(board, 7) == 1 && board.comboProcesor(board, 8) == 1 ||
                          board.comboProcesor(board, 6) == 2 && board.comboProcesor(board, 7) == 2 && board.comboProcesor(board, 8) == 2) {
                    combosPairs.add(board.getCombos().get(6));
                    return true;
                } else if(board.comboProcesor(board, 0) == 1 && board.comboProcesor(board, 3) == 1 && board.comboProcesor(board, 6) == 1 ||
                          board.comboProcesor(board, 0) == 2 && board.comboProcesor(board, 3) == 2 && board.comboProcesor(board, 6) == 2) {
                    combosPairs.add(board.getCombos().get(0));
                    return true;
                } else if(board.comboProcesor(board, 1) == 1 && board.comboProcesor(board, 4) == 1 && board.comboProcesor(board, 7) == 1 ||
                        board.comboProcesor(board, 1) == 2 && board.comboProcesor(board, 4) == 2 && board.comboProcesor(board, 7) == 2) {
                    combosPairs.add(board.getCombos().get(1));
                    return true;
                } else if(board.comboProcesor(board, 2) == 1 && board.comboProcesor(board, 5) == 1 && board.comboProcesor(board, 8) == 1 ||
                          board.comboProcesor(board, 2) == 2 && board.comboProcesor(board, 5) == 2 && board.comboProcesor(board, 8) == 2) {
                    combosPairs.add(board.getCombos().get(2));
                    return true;
                } else if(board.comboProcesor(board, 0) == 1 && board.comboProcesor(board, 4) == 1 && board.comboProcesor(board, 8) == 1 ||
                        board.comboProcesor(board, 0) == 2 && board.comboProcesor(board, 4) == 2 && board.comboProcesor(board, 8) == 2) {
                    combosPairs.add(board.getCombos().get(0));
                    return true;
                } else if(board.comboProcesor(board, 2) == 1 && board.comboProcesor(board, 4) == 1 && board.comboProcesor(board, 6) == 1 ||
                          board.comboProcesor(board, 2) == 2 && board.comboProcesor(board, 4) == 2 && board.comboProcesor(board, 6) == 2) {
                    combosPairs.add(board.getCombos().get(2));
                    return true;
                }
                return false;
    }
}

