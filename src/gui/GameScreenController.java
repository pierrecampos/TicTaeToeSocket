package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import model.entities.Player;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GameScreenController extends Thread implements Initializable {

    private static final long serialVersionUID = 1L;
    @FXML
    private Button btn0;
    private Player player;
    private OutputStream oS;
    private Writer oSW;
    private BufferedWriter bW;


    @FXML
    private void onBtn1Click(ActionEvent event) {
        System.out.println(player.getName() + " - Clicou no botão");
        sendMessage("0");
    }

    @FXML
    private void onBtn2Click(ActionEvent event) {

    }

    @FXML
    private void onBtn3Click(ActionEvent event) {

    }

    @FXML
    private void onBtn4Click(ActionEvent event) {

    }

    @FXML
    private void onBtn5Click(ActionEvent event) {

    }

    @FXML
    private void onBtn6Click(ActionEvent event) {

    }

    @FXML
    private void onBtn7Click(ActionEvent event) {

    }

    @FXML
    private void onBtn8Click(ActionEvent event) {

    }

    @FXML
    private void onBtn9Click(ActionEvent event) {

    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private void sendMessage(String message) {
        try {
            bW.write(message + "\r\n");
            bW.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        sendMessage("");
        listenMessages();
    }

    public void listenMessages() {
        try {
            InputStream iS = player.getPlayerService().getCon().getInputStream();
            InputStreamReader iSR = new InputStreamReader(iS);
            BufferedReader bR = new BufferedReader(iSR);
            String msg;
            while (true) {
                if (bR.ready()) {

                    msg = bR.readLine();

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            btn0.setText("X");
                        }
                    });




                    System.out.println("OUVINDO " + msg);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setAttributes() {
        try {
            oS = player.getPlayerService().getCon().getOutputStream();
            oSW = new OutputStreamWriter(oS);
            bW = new BufferedWriter(oSW);
            start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
