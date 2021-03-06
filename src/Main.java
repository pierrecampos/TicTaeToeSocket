import gui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("gui/Main.fxml"));
        fxmlLoader.setController(new MainController());
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Jogo da velha!");
        stage.setScene(scene);
        stage.show();
    }
}
