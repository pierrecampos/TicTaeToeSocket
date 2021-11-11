package gui;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import util.Utils;

public class ConnectDialogController {

    public void onBtnConnectClick(ActionEvent event) {

    }

    public void onBtnCloseClick(ActionEvent event) {
        Stage currentStage = Utils.currentStage(event);
        currentStage.close();
    }
}
