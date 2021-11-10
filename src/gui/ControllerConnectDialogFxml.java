package gui;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ControllerConnectDialogFxml {

    private final boolean wait = true;
    private Stage window;

    @FXML
    private void backButton(Stage homeStage) {
        window = homeStage;
        if (wait) {
            window.close();
        }
    }
}
