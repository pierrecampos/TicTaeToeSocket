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

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
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
                        fields = transformIndex(index);
                        game.play(fields[0], fields[1], !player.getToken().value);
                        Boolean[][] winningMatrix = game.isWinner(!player.getToken().value);
                        draw(index, !player.getToken().value);
                        if (hasWinner(winningMatrix, false)) {
                            System.out.println("Você Perdeu");
                            myTurn = false;
                        }
                    });
                    myTurn = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean hasWinner(Boolean[][] winningMatrix, boolean token) {
        if (winningMatrix.length == 0) {
            return false;
        }

        List<Integer> indexButtons = transformPosition(winningMatrix);
        drawWinner(indexButtons, token);

        return true;
    }

    private List<Integer> transformPosition(Boolean[][] matrix) {
        int row, column = 0, position = 0;
        List<Integer> indexButtons = new ArrayList<>();
        for (row = 0; row < 3; row++) {
            for (column = 0; column < 3; column++, position++) {
                if (matrix[row][column] != null) {
                    indexButtons.add(position);
                }
            }
        }
        return indexButtons;
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
            if (hasWinner(winningMatrix, true)) {
                System.out.println("Você Ganhou");
                myTurn = false;
            }
        }
    }

    private boolean validPLay(int index) {
        fields = transformIndex(index);
        return game.validPlay(fields[0], fields[1]);
    }

    private int[] transformIndex(int index) {
        int row, column = 0, count = 0;
        for (row = 0; row < 3; row++, count++) {
            for (column = 0; column < 2; column++, count++) {
                if (count == index) {
                    break;
                }
            }
            if (count == index) {
                break;
            }
        }
        int[] fields = new int[2];
        fields[0] = row;
        fields[1] = column;

        return fields;
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
