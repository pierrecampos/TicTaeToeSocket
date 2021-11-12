package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.entities.Player;
import util.Utils;

import java.io.IOException;
import java.net.Socket;

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

    private void connectToServer(){
        int port = Integer.parseInt(txtPort.getText());
        String ip = txtIp.getText();

        try {
            Socket con  = new Socket(ip, port);
            System.out.println(con == null);
            player.setSocket(con);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setPlayer(Player player){
        this.player = player;
    }


    private void close(Stage currentStage) {
        currentStage.close();
    }




}
