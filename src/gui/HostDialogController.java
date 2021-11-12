package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socket.Server;
import util.Utils;

public class HostDialogController {

    @FXML
    private Button btnBack;

    @FXML
    private TextField txtPort;

    @FXML
    private TextField txtIp;

    private Server server;


    @FXML
    private void onBtnHostClick(ActionEvent event) {
        int port = Integer.parseInt(txtPort.getText());
        server = new Server(port);
        new Thread(server).start();
    }

    @FXML
    private void onBtnCloseClick(ActionEvent event) {
        Stage currentStage = Utils.currentStage(event);
        close(currentStage);
    }

    private void close(Stage currentStage) {
        currentStage.close();
    }

}
