import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class TicTacToeGame extends Application {

    private Parent drawContent() {
        Pane root = new Pane();
        root.setPrefSize(600, 600);

        return root;
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        mainStage.setScene(new Scene(drawContent()));
        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
