package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Player;
import model.services.PlayerSocketService;
import util.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private final Player player;
    @FXML
    private TextField txtPlayer;

    public MainController() {
        player = new Player("Player", new PlayerSocketService());
    }

    public MainController(Player player){
        this.player = player;
    }

    @FXML
    private void onBtnConnectClick(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        createDialogForm("/gui/ConnectDialog.fxml", parentStage, new ConnectDialogController(player));
    }

    @FXML
    private void onBtnHostGameClick(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        createDialogForm("/gui/HostDialog.fxml", parentStage, new HostDialogController(player));
    }

    private <T> void createDialogForm(String absoluteName, Stage parentStage, T controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            loader.setController(controller);
            Pane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.getScene().setFill(Color.TRANSPARENT);
            dialogStage.getScene().getRoot().setEffect(new DropShadow());
            dialogStage.initOwner(parentStage);
            dialogStage.showAndWait();

            startGame(parentStage);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startGame(Stage parentStage) {
        if (!player.getIsReady()) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GameScreen.fxml"));
            GameScreenController gameScreenController = new GameScreenController(player);
            loader.setController(gameScreenController);
            Pane pane = loader.load();
            Scene gameScene = new Scene(pane);
            parentStage.setResizable(false);
            parentStage.setScene(gameScene);
            parentStage.setTitle("Jogo da velha! - " + player.getName() + (player.getIsHost() ? " - Host" : ""));

            Thread t = new Thread(gameScreenController);
            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initFields();
        initEvents();
    }


    private void initFields() {
        txtPlayer.setText(player.getName());
    }

    private void initEvents() {
        txtPlayer.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                player.setName(txtPlayer.getText());
            }
        });
    }

    @FXML
    public void debugButton(ActionEvent e) throws IOException {
        Stage parentStage = Utils.currentStage(e);
//        RematchController rematchController = new RematchController(parentStage, player);
//        rematchController.start();
    }
}