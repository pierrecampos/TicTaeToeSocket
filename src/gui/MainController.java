package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private Pane mainPane;

    private boolean wait;

    private Stage window;

    @FXML
    private void onBtnConnectClick()  {
        System.out.println("OK");
    }

    @FXML
    private void onBtnHostGameClick(){
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("HostDialog.fxml"));
            Pane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Conectar");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}