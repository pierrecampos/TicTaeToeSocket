package gui;

import animation.ZoomIn;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.constants.GameConstants;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class PlayAgainController extends Thread implements Initializable {
    private final Stage parentStage;
    private final Consumer<Boolean> callback;
    private final GameConstants result;
    @FXML
    private Button btnYes;

    @FXML
    private Button btnNo;

    @FXML
    private Label winLabel;


    public PlayAgainController(Stage parentStage, Consumer<Boolean> callback, GameConstants result) {
        this.parentStage = parentStage;
        this.callback = callback;
        this.result = result;
    }

    @Override
    public void run() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(this::show);

    }

    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RematchScreen.fxml"));
            loader.setController(this);
            Pane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.getScene().setFill(Color.TRANSPARENT);
            dialogStage.getScene().getRoot().setEffect(new DropShadow());
            dialogStage.initOwner(parentStage);

            double centerXPosition = parentStage.getX() + parentStage.getWidth() / 2d;
            double centerYPosition = parentStage.getY() + parentStage.getHeight() / 2d;

            dialogStage.setOnShown(e -> {
                dialogStage.setX(centerXPosition - dialogStage.getWidth() / 2d);
                dialogStage.setY(centerYPosition - dialogStage.getHeight() / 2d);
                new ZoomIn(dialogStage.getScene().getRoot()).play();
            });

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onRematchYesClick(ActionEvent event) {
        returnRematchPlay(true);
    }

    @FXML
    private void onRematchNoClick(ActionEvent event) {
        returnRematchPlay(false);
    }

    private void returnRematchPlay(boolean rematch) {
        callback.accept(rematch);
        close();
    }

    private void close() {
        Stage currentStage = (Stage) btnYes.getScene().getWindow();
        currentStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (GameConstants.WINNER.equals(result)) {
            winLabel.setText("Jogo Vencedor");
            winLabel.setStyle("-fx-text-fill: #2b9348;");
        } else if (GameConstants.LOSER.equals(result)) {
            winLabel.setText("Jogo Perdedor");
            winLabel.setStyle("-fx-text-fill: #e5383b;");
        } else {
            winLabel.setText("Empate");
            winLabel.setStyle("-fx-text-fill: #0077b6;");
        }
    }
}
