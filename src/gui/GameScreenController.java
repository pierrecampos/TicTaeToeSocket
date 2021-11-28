package gui;

import animation.Flip;
import animation.Hinge;
import animation.Jello;
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
import model.constants.GameConstants;
import model.entities.Player;
import model.entities.TicTacToe;
import util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class GameScreenController extends Thread implements Initializable {

    private static final long serialVersionUID = 1L;
    private final TicTacToe game;
    private final Player player;
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
        listenMessages();
        rematch(player.getResult());
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
                    player.setResult(GameConstants.WINNER);
                    continueGame.set(false);
                    continue;
                } else if (index == -2) {
                    player.setResult(GameConstants.DRAW);
                    continueGame.set(false);
                    continue;
                }

                boolean adversaryToken = !player.getToken().value;
                fields = Utils.transformIndex(index);
                game.play(fields[0], fields[1], adversaryToken);
                Boolean[][] winningMatrix = game.isWinner(adversaryToken);

                draw(index, adversaryToken);

                if (hasWinner(winningMatrix, GameConstants.LOSER)) {
                    player.setResult(GameConstants.LOSER);
                    sendMessage(-1);
                    continueGame.set(false);
                } else {
                    if (game.getRounds() == 9) {
                        player.setResult(GameConstants.DRAW);
                        sendMessage(-2);
                        continueGame.set(false);
                        drawWinner(new ArrayList(), GameConstants.DRAW);
                    }
                }

                myTurn = true;
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
            if (!hasWinner(winningMatrix, GameConstants.WINNER)) {
                if (game.getRounds() == 9) {
                    player.setResult(GameConstants.DRAW);
                    drawWinner(new ArrayList(), GameConstants.DRAW);
                }
            }
            sendMessage(indexButton);
            myTurn = false;

        }
    }

    private boolean validPLay(int index) {
        fields = Utils.transformIndex(index);
        return game.validPlay(fields[0], fields[1]);
    }

    private boolean hasWinner(Boolean[][] winningMatrix, GameConstants result) {
        if (winningMatrix.length == 0) {
            return false;
        }

        myTurn = false;
        List<Integer> indexButtons = Utils.transformPosition(winningMatrix);
        drawWinner(indexButtons, result);
        return true;
    }

    private void rematch(GameConstants result) {
        Platform.runLater(() -> {
            Stage parentStage = (Stage) pane.getScene().getWindow();
            RematchController rematchController = new RematchController(parentStage, player, oIS, oS, this::createRematch, result);
            rematchController.start();
        });
    }

    private void createRematch(Boolean create) {
        if (create) {
            createWindow("GameScreen.fxml", "Jogo da Velha - " + player.getName(), new GameScreenController(player));
        } else {
            try {
                player.getPlayerSocketService().closeService();
            } catch (IOException e) {
                e.printStackTrace();
            }
            createWindow("Main.fxml", "Jogo da Velha", new MainController(player));
        }
    }

    private void draw(int index, boolean token) {
        Platform.runLater(() -> {
            Button button = (Button) buttons.get(index);
            String tokenString = token ? "X" : "O";
            button.setText(tokenString);
        });
    }

    private void drawWinner(List<Integer> indexButtons, GameConstants result) {
        for (Node button : buttons) {
            Button btn = (Button) button;
            if (indexButtons.contains(buttons.indexOf(btn))) {
                setResult(result, btn);
            } else if (result.equals(GameConstants.DRAW)) {
                setResult(result, btn);
            }

        }
    }

    private void setResult(GameConstants result, Button btn) {
        String color = "-fx-background-color: ";
        switch (result) {
            case WINNER:
                color += "#60D394;";
                new Flip(btn).play();
                break;
            case LOSER:
                color += "#EE6055;";
                new Hinge(btn).play();
                break;
            case DRAW:
                color += "transparent";
                new Jello(btn).play();
                break;
        }
        btn.setStyle(color);
    }


    private <T> void createWindow(String absoluteName, String title, T controller) {
        Stage parentStage = (Stage) pane.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));

        loader.setController(controller);
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene gameScene = new Scene(pane);
        parentStage.setResizable(false);
        parentStage.setScene(gameScene);
        parentStage.setTitle(title);

        if (controller instanceof GameScreenController) {
            GameScreenController gameScreenController = (GameScreenController) controller;
            Thread t = new Thread(gameScreenController);
            t.start();
        } else {
            parentStage.setScene(gameScene);
        }
    }


}
