module com.tictactoesocket {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tictactoesocket to javafx.fxml;
    exports com.tictactoesocket;
    exports controller;
    opens controller to javafx.fxml;
}