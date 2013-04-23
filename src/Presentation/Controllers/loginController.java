package Presentation.Controllers;

import Domain.DataObjects.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;



public class loginController {
    @FXML public GridPane window;
    @FXML public TextField address;
    @FXML public TextField port;
    @FXML public TextField name;
    @FXML public TextField password;

    @FXML public void login(ActionEvent event)
    {
        Stage stage = (Stage) window.getScene().getWindow();
        stage.close();

    }

    public Database getConnectionParameters()
    {
        return new Database(address.getText(),address.getText(),port.getText(),name.getText(),password.getText());
    }
}
