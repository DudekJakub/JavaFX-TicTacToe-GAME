import javafx.scene.Parent;

public class Mark extends Parent {
    public int isFull; //null - is not full, 1 - is full
    public boolean horizontal;

    public Mark(int isFull, boolean horizontal) {
        this.isFull = isFull;
        this.horizontal = horizontal;

    }
}
