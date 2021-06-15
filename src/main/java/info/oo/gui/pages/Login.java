package info.oo.gui.pages;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Login extends VBox {

    @FXML
    private Label lblError;

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtPassword;

    public Login() {
        super();
        loadFXML();
    }

    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    public void initialize() {
        lblError.setText("");
        lblError.setManaged(false);
    }

    @FXML
    void onBtnLoginAction(ActionEvent event) {

    }

    @FXML
    void onBtnRegisterAction(ActionEvent event) {

    }

}
