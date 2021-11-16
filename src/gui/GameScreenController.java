package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.entities.Player;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class GameScreenController implements Initializable {
    private static final long serialVersionUID = 1L;
    private Player player;
    private OutputStream oS;
    private Writer oSW;
    private BufferedWriter bW;


    @FXML
    private void onBtn1Click(ActionEvent event) {
        sendMessage(player.getName() + "- Teste");
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

    public void listenMessages() {
        try {
            InputStream iS = player.getPlayerService().getCon().getInputStream();
            InputStreamReader iSR = new InputStreamReader(iS);
            BufferedReader bR = new BufferedReader(iSR);
            String msg;
            System.out.println("Ouvindo");
            while (true) {
                if (bR.ready()) {
                    msg = bR.readLine();
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
