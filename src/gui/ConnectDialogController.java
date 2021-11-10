package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import util.Utils;

public class ConnectDialogController {

    @FXML
    private Button btnBack;

    @FXML
    private TextField txtPort;

    @FXML
    private TextField txtIp;

    @FXML
    private void onBtnCloseClick(ActionEvent event) {
        Stage currentStage = Utils.currentStage(event);
        currentStage.close();
    }

}
