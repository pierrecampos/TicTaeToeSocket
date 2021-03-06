package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.entities.Player;
import model.constants.Token;
import util.Utils;

import java.io.IOException;

public class ConnectDialogController {

    private final Player player;
    @FXML
    private TextField txtIp;
    @FXML
    private TextField txtPort;

    public ConnectDialogController(Player player) {
        this.player = player;
    }

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
        String ip = txtIp.getText();
        int port = Integer.parseInt(txtPort.getText());

        try {
            boolean hasConnected = player.getPlayerSocketService().startSocket(ip, port);
            if (hasConnected) {
                player.setIsReady(true);
                player.setToken(Token.CIRCLE);
                gameReady();
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    private void close(Stage currentStage) {
        currentStage.close();
    }


}
