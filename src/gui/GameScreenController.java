package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.entities.Player;
import model.entities.TicTacToe;
import socket.Server;
import util.Utils;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class GameScreenController extends Thread implements Initializable {

    private static final long serialVersionUID = 1L;
    private final TicTacToe game;
    @FXML
    private Pane pane;
    @FXML
    private Label player1Name;
    @FXML
    private Label player2Name;

    private int[] fields;
    private List<Node> buttons;
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
        buttons = pane.getChildren().stream().filter(x -> x instanceof Button).collect(Collectors.toList());
        initEvents();
    }

    private void initEvents() {
        initBoardButtonsEvent();
    }

    private void initBoardButtonsEvent() {
        for (Node currentBtn : buttons) {
            Button button = (Button) currentBtn;
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
            AtomicBoolean continueGame = new AtomicBoolean(true);
            setOpponentName(bR);
            while (continueGame.get()) {
                if (bR.ready()) {
                    msg = bR.readLine();
                    String finalMsg = msg;
                    Platform.runLater(() -> {
                        int index = Integer.parseInt(finalMsg);
                        fields = Utils.transformIndex(index);
                        game.play(fields[0], fields[1], !player.getToken().value);
                        draw(index, !player.getToken().value);

                        Boolean[][] winningMatrix = game.isWinner(!player.getToken().value);
                        continueGame.set(!hasWinner(winningMatrix, false));
                    });
                    myTurn = true;
                }
            }
            rematch(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setOpponentName(BufferedReader bR) throws IOException {
        sendMessage(player.getName());
        String opponentName = bR.readLine();

        Platform.runLater(() -> {
            if (player.getIsHost()) {
                player1Name.setText(player.getName());
                player2Name.setText(opponentName);
            } else {
                player1Name.setText(opponentName);
                player2Name.setText(player.getName());
            }
            myTurn = player.getIsHost();
        });
    }

    private void onButtonBoardClick(ActionEvent event) {
        if (!myTurn) {
            return;
        }
        Button clickedButton = (Button) event.getSource();
        int indexButton = buttons.indexOf(clickedButton);
        if (validPLay(indexButton)) {
            sendMessage(String.valueOf(indexButton));
            game.play(fields[0], fields[1], player.getToken().value);
            draw(indexButton, player.getToken().value);

            Boolean[][] winningMatrix = game.isWinner(player.getToken().value);
            if (hasWinner(winningMatrix, true)) {
                rematch(true);
            }
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
        player.getPlayerService().closeSocket();
        closeServer();
        return true;
    }

    private void closeServer() {
        Server server = player.getServer();
        if (server != null) {
            server.closeServer();
            server.interrupt();
        }
    }


    private void rematch(boolean winner) {
        String answer = "Você " + (winner ? "Ganhou" : "Perdeu");
        Platform.runLater(() -> {
            Utils.showConfirmation("Fim de Jogo", answer);

            //Apenas para debug - REFATORAR
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                Pane p = loader.load();
                MainController controller = loader.getController();
                Scene gameScene = new Scene(p);
                Stage stage = (Stage) pane.getScene().getWindow();
                stage.setScene(gameScene);
                stage.setTitle("Jogo da velha! - " + player.getName() + (player.getIsHost() ? " - Host" : ""));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void draw(int index, boolean token) {
        Button button = (Button) buttons.get(index);
        String tokenString = token ? "X" : "O";
        button.setText(tokenString);
    }

    private void drawWinner(List<Integer> indexButtons, boolean winner) {
        String color = "-fx-background-color: " + (winner ? "#60D394;" : "#EE6055;");
        for (Node button : buttons) {
            Button btn = (Button) button;
            if (indexButtons.contains(buttons.indexOf(btn))) {
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
