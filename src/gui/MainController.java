package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.entities.Player;
import socket.Server;
import util.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TextField txtPlayer;

    private Player player;

    private static Server server;

    public MainController(){
        player = new Player("Player");
    }

    @FXML
    private void onBtnConnectClick(ActionEvent event) {
    }

    @FXML
    private void onBtnHostGameClick(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        createDialogForm("/gui/HostDialog.fxml", parentStage);
    }


    private void createDialogForm(String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();
            HostDialogController controller = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(parentStage);
            dialogStage.showAndWait();

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
}