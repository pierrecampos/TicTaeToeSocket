package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import model.entities.Player;
import model.entities.TicTacToe;
import util.Utils;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameScreenController extends Thread implements Initializable {

    private static final long serialVersionUID = 1L;
    private final TicTacToe game;
    private int[] fields;
    @FXML
    private TilePane tilePane;
    private List<Node> nodes;
    private Player player;
    private boolean myTurn;
    private OutputStream oS;
    private Writer oSW;
    private BufferedWriter bW;

    public GameScreenController() {
        game = new TicTacToe();
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

    public void setPlayer(Player player) {
        this.player = player;
    }

    private void sendMessage(String message) {
        try {
            bW.write(message + "\r\n");
            bW.flush();
            myTurn = false;
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
        myTurn = player.getIsHost();
        try {
            InputStream iS = player.getPlayerService().getCon().getInputStream();
            InputStreamReader iSR = new InputStreamReader(iS);
            BufferedReader bR = new BufferedReader(iSR);
            String msg;
            while (true) {
                if (bR.ready()) {
                    msg = bR.readLine();
                    String finalMsg = msg;
                    Platform.runLater(() -> {
                        int index = Integer.parseInt(finalMsg);
                        fields = Utils.transformIndex(index);
                        game.play(fields[0], fields[1], !player.getToken().value);
                        draw(index, !player.getToken().value);

                        Boolean[][] winningMatrix = game.isWinner(!player.getToken().value);
                        hasWinner(winningMatrix, false);
                    });
                    myTurn = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onButtonBoardClick(ActionEvent event) {
        if (!myTurn) {
            return;
        }
        Button clickedButton = (Button) event.getSource();
        int indexButton = nodes.indexOf(clickedButton);
        if (validPLay(indexButton)) {
            sendMessage(String.valueOf(indexButton));
            game.play(fields[0], fields[1], player.getToken().value);
            draw(indexButton, player.getToken().value);

            Boolean[][] winningMatrix = game.isWinner(player.getToken().value);
            hasWinner(winningMatrix, true);
        }
    }

    private boolean validPLay(int index) {
        fields = Utils.transformIndex(index);
        return game.validPlay(fields[0], fields[1]);
    }

    private boolean hasWinner(Boolean[][] winningMatrix, boolean winner) {
        if (winningMatrix.length == 0) {
            return false;
        }

        List<Integer> indexButtons = Utils.transformPosition(winningMatrix);
        drawWinner(indexButtons, winner);
        myTurn = false;
        rematch(winner);

        return true;
    }

    private void rematch(boolean winner) {
        String answer = "Você " + (winner ? "Ganhou" : "Perdeu");
        System.out.println(answer);
    }


    private void draw(int index, boolean token) {
        Button button = (Button) nodes.get(index);
        String tokenString = token ? "X" : "O";
        button.setText(tokenString);
    }

    private void drawWinner(List<Integer> indexButtons, boolean winner) {
        String color = "-fx-background-color: " + (winner ? "#60D394;" : "#EE6055;");
        for (Node button : nodes) {
            Button btn = (Button) button;
            if (indexButtons.contains(nodes.indexOf(btn))) {
                btn.setStyle(color);
            }
        }
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
