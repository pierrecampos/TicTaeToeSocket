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
import util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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

    @FXML
    private Label player1Token;

    @FXML
    private Label player2Token;

    private int[] fields;
    private List<Node> buttons;
    private Player player;
    private boolean myTurn;
    private ObjectOutputStream oS;

    public GameScreenController() {
        game = new TicTacToe();
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

    private void sendMessage(Object object) {
        try {
            oS.writeObject(object);
            oS.flush();
//            myTurn = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        listenMessages();
    }

    public void listenMessages() {
        try {
            Socket socket = player.getPlayerSocketService().getSocket();
            InputStream is = socket.getInputStream();
            ObjectInputStream oIS = new ObjectInputStream(is);
            setOpponentName(oIS);
            while (true) {
                System.out.println(oIS.readObject());
            }


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void setOpponentName(ObjectInputStream oIS) throws IOException, ClassNotFoundException {
        sendMessage(player.getName());
        String opponentName = (String) oIS.readObject();

        Platform.runLater(() -> {
            player1Name.setText(player.getName());
            player2Name.setText(opponentName);
            player1Token.setText(player.getIsHost() ? "X" : "O");
            player2Token.setText(player.getIsHost() ? "O" : "X");
        });
    }

    private void onButtonBoardClick(ActionEvent event) {
//        if (!myTurn) {
//            return;
//        }
        Button clickedButton = (Button) event.getSource();
        int indexButton = buttons.indexOf(clickedButton);
        if (validPLay(indexButton)) {
            sendMessage(String.valueOf(indexButton));
//            game.play(fields[0], fields[1], player.getToken().value);
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
//        myTurn = false;
//        closeServer();
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
        String answer = "Você " + (winner ? "Ganhou" : "Perdeu");
        Platform.runLater(() -> {
            Utils.showConfirmation("Fim de Jogo", answer);

            //Apenas para debug - REFATORAR
            try {
                Stage parentStage = (Stage) pane.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GameScreen.fxml"));
                Pane pane = loader.load();
                GameScreenController controller = loader.getController();
                controller.setPlayer(player);
                controller.setAttributes();
                Scene gameScene = new Scene(pane);
                parentStage.setScene(gameScene);
                parentStage.setTitle("Jogo da velha! - " + player.getName());
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
//        try {
//            oS = player.getPlayerService().getCon().getOutputStream();
//            oSW = new OutputStreamWriter(oS);
//            bW = new BufferedWriter(oSW);
        initWriter();
        start();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
