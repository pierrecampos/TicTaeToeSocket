package com.example.demo;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerHome implements Initializable {
    @FXML
    private Pane mainPane;
    private boolean wait;
    private Stage window;

    @FXML
    private void handleButton1() throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader(ControllerHome.class.getResource("ConnectDialog.fxml"));
            Pane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Conectar");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}