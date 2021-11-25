package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.entities.Player;
import model.entities.Token;
import model.listeners.GameReadyListener;
import socket.Server;
import util.Utils;

import java.net.URL;
import java.util.ResourceBundle;


public class HostDialogController implements Initializable, GameReadyListener {

    @FXML
    private TextField txtPort;
    @FXML
    private TextField txtIp;
    @FXML
    private Label waiting;
    private Player player;


    @FXML
    private void onBtnHostClick() {
        int port = Integer.parseInt(txtPort.getText());
        boolean hostStarted = startHost(port);

        if (hostStarted) {
            player.setToken(Token.CROSS);
            player.setReady(true);
            player.setIsHost(true);
            waiting.setVisible(true);
        }
    }

    @FXML
    private void onBtnCloseClick(ActionEvent event) {
        player.setReady(false);
        player.getPlayerSocketService().closeServer();
        Stage currentStage = Utils.currentStage(event);
        close(currentStage);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private boolean startHost(int port) {
        Server server = player.getPlayerSocketService().startServer(port);
        server.subscribeGameReadyListener(this);
        Thread t = new Thread(server);
        t.start();
        return t.isAlive();
    }

    private void close(Stage currentStage) {
        currentStage.close();
    }

    @Override
    public void onGameReady() {
        Stage stage = (Stage) txtIp.getScene().getWindow();
        Platform.runLater(() -> {
            close(stage);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}
