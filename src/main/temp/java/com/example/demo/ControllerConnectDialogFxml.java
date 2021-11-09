package com.example.demo;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ControllerConnectDialogFxml {

    private boolean wait = true;
    private Stage window;

    @FXML
    private void backButton(Stage homeStage){
        window = homeStage;
        if (wait){
            window.close();
        }
    }
}
