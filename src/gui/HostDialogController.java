package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.entities.Player;
import model.listeners.GameReadyListener;
import socket.Server;
import util.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class HostDialogController implements Initializable, GameReadyListener {

    @FXML
    private Button btnBack;

    @FXML
    private TextField txtPort;

    @FXML
    private TextField txtIp;

    private Player player;

    private static Server server;


    @FXML
    private void onBtnHostClick(ActionEvent event) {
        int port = Integer.parseInt(txtPort.getText());
        boolean hostStarted = startHost(port);
        if (hostStarted) {
            String ip = txtIp.getText();
            boolean hasConnected = player.getPlayerService().connect(ip, port);
            player.setReady(hasConnected);
        }
    }

    @FXML
    private void onBtnCloseClick(ActionEvent event) {
        player.getPlayerService().closeSocket();
        Stage currentStage = Utils.currentStage(event);
        close(currentStage);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private boolean startHost(int port) {
        server = new Server(port);
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
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                close(stage);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}
