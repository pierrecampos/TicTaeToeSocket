package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.entities.Player;
import model.entities.Token;
import util.Utils;

public class ConnectDialogController {

    @FXML
    private TextField txtIp;

    @FXML
    private TextField txtPort;

    private Player player;


    @FXML
    public void onBtnConnectClick(ActionEvent event) {
        connectToServer();
    }

    @FXML
    private void onBtnCloseClick(ActionEvent event) {
        Stage currentStage = Utils.currentStage(event);
        close(currentStage);
    }

    private void connectToServer() {
        int port = Integer.parseInt(txtPort.getText());
        String ip = txtIp.getText();

        boolean hasConnected = player.getPlayerService().connect(ip, port);
        if (hasConnected) {
            player.setReady(hasConnected);
            player.setToken(Token.CIRCLE);
            gameReady();
        }
    }

    public void gameReady() {
        Stage stage = (Stage) txtIp.getScene().getWindow();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                close(stage);
            }
        });
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    private void close(Stage currentStage) {
        currentStage.close();
    }


}
