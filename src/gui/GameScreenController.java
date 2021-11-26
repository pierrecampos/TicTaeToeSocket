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
import model.services.PlayerSocketService;
import socket.Server;
import util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class GameScreenController extends Thread implements Initializable {

    private static final long serialVersionUID = 1L;
    private final TicTacToe game;
    private Player player;
    @FXML
    private Pane pane;
    @FXML
    private Label player1Name;
    @FXML
    private Label player2Name;
    @FXML
    private Label player1Token;
    @FXML
    private Label player2Token;
    private int[] fields;
    private List<Node> buttons;
    private boolean myTurn;
    private ObjectOutputStream oS;
    private InputStream is;
    private ObjectInputStream oIS;

    public GameScreenController(Player player) {
        this.player = player;
        game = new TicTacToe();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = pane.getChildren().stream().filter(x -> x instanceof Button).collect(Collectors.toList());
        System.out.println(player.getName());
        initEvents();
        initWriter();
    }

    private void initWriter() {
        try {
            Socket socket = player.getPlayerSocketService().getSocket();
            oS = new ObjectOutputStream(socket.getOutputStream());
            oS.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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


    private void sendMessage(Object object) {
        try {
            oS.writeObject(object);
            oS.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        myTurn = player.getIsHost();
        System.out.println(myTurn);
        listenMessages();
        rematch(player.getIsWinner());
    }

    public void listenMessages() {
        Socket socket = player.getPlayerSocketService().getSocket();
        try {
            is = socket.getInputStream();
            oIS = new ObjectInputStream(is);
            setOpponentName(oIS);
            AtomicBoolean continueGame = new AtomicBoolean(true);
            while (continueGame.get()) {
                Integer index = (Integer) oIS.readObject();
                if (index == -1) {
                    continueGame.set(false);
                    player.setWinner(player.getIsWinner());
                    sendMessage(-1);
                    continue;
                }
                Platform.runLater(() -> {
                    boolean adversaryToken = !player.getToken().value;
                    fields = Utils.transformIndex(index);
                    game.play(fields[0], fields[1], adversaryToken);
                    draw(index, adversaryToken);

                    myTurn = true;
                    Boolean[][] winningMatrix = game.isWinner(adversaryToken);
                    continueGame.set(!hasWinner(winningMatrix, false));
                });
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void setOpponentName(ObjectInputStream oIS) {
        sendMessage(player.getName());
        try {
            String opponentName = (String) oIS.readObject();

            Platform.runLater(() -> {
                player1Name.setText(player.getName());
                player2Name.setText(opponentName);
                player1Token.setText(player.getIsHost() ? "X" : "O");
                player2Token.setText(player.getIsHost() ? "O" : "X");
            });
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void onButtonBoardClick(ActionEvent event) {
        if (!myTurn) {
            return;
        }
        Button clickedButton = (Button) event.getSource();
        int indexButton = buttons.indexOf(clickedButton);
        if (validPLay(indexButton)) {

            game.play(fields[0], fields[1], player.getToken().value);
            draw(indexButton, player.getToken().value);
            Boolean[][] winningMatrix = game.isWinner(player.getToken().value);
            boolean hasWinner = hasWinner(winningMatrix, true);
            sendMessage(indexButton);
            myTurn = false;
            if (hasWinner) {
                player.setWinner(true);
                sendMessage(-1);
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

        myTurn = false;
        List<Integer> indexButtons = Utils.transformPosition(winningMatrix);
        drawWinner(indexButtons, winner);
        return true;
    }

    private void closeServer() {
//        player.getPlayerService().closeSocket();

//        Server server = player.getServer();
//        if (server != null) {
//            server.closeServer();
//            server.interrupt();
//        }
    }


    private void rematch(boolean winner) {
        String msg = "Você " + (winner ? "Ganhou" : "Perdeu");
        Platform.runLater(() -> {
            Utils.showConfirmation("Fim de Jogo", msg);


//            //Apenas para debug - REFATORAR
            try {
                oIS.close();
                is.close();
                oS.close();
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
}
