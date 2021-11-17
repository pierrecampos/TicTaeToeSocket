package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import model.entities.Player;
import model.entities.Token;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameScreenController extends Thread implements Initializable {

    private static final long serialVersionUID = 1L;

    @FXML
    private TilePane tilePane;

    private List<Node> nodes;
    private Player player;
    private OutputStream oS;
    private Writer oSW;
    private BufferedWriter bW;
    private String playerToken;


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
                    String finalMsg = msg;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            int index = Integer.parseInt(finalMsg);
                            draw(index);
                        }
                    });
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nodes = tilePane.getChildren().stream().filter(x -> x instanceof Button).collect(Collectors.toList());
        initEvents();
    }

    private void initEvents() {
        initBoardButtonsEvent();
    }

    private void initBoardButtonsEvent() {
        for (Node node : nodes) {
            Button button = (Button) node;
            button.setOnAction(this::onButtonBoardClick);
        }
    }

    private void onButtonBoardClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        int indexButton = nodes.indexOf(clickedButton);
        sendMessage(String.valueOf(indexButton));
    }

    private void draw(int index) {
        Button button = (Button) nodes.get(index);
        button.setText(playerToken);
    }

    public void setAttributes() {
        try {
            oS = player.getPlayerService().getCon().getOutputStream();
            oSW = new OutputStreamWriter(oS);
            bW = new BufferedWriter(oSW);
            playerToken = player.getToken().equals(Token.CIRCLE) ? "O" : "X";
            start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
