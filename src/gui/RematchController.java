package gui;

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
import model.entities.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class RematchController extends Thread implements Initializable {
    private final Stage parentStage;
    private final ObjectInputStream oIS;
    private final ObjectOutputStream oS;
    private final Consumer<Boolean> callback;
    private Boolean rematchGame;
    private boolean winner;
    @FXML
    private Button btnYes;

    @FXML
    private Button btnNo;

    @FXML
    private Label winLabel;


    public RematchController(Stage parentStage, Player player, ObjectInputStream oIS, ObjectOutputStream oS, Consumer<Boolean> callback, boolean winner) {
        this.parentStage = parentStage;
        this.oIS = oIS;
        this.oS = oS;
        this.callback = callback;
        this.winner = winner;
    }

    @Override
    public void run() {
        Platform.runLater(this::listenMessage);

    }

    private void listenMessage() {
        try {
            show();
            Boolean adversary = (Boolean) oIS.readObject();
            callback.accept(adversary && rematchGame);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

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
        disableButtons();
        rematchGame = rematch;
        sendMessage(rematch);
        close();
    }

    private void sendMessage(boolean rematch) {
        try {
            oS.flush();
            oS.writeObject(rematch);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        Stage currentStage = (Stage) btnYes.getScene().getWindow();
        currentStage.close();
    }

    private void disableButtons() {
        btnYes.setDisable(true);
        btnNo.setDisable(true);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(winner){
            winLabel.setText("Jogo Vencedor");
            winLabel.setStyle( "-fx-text-fill: #2b9348;");
        }else{
            winLabel.setText("Jogo Perdedor");
            winLabel.setStyle( "-fx-text-fill: #e5383b;");
        }
    }
}
