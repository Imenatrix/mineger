package info.oo.gui.pages;

import java.io.IOException;

import info.oo.repositories.interfaces.IUserRepository;
import info.oo.entities.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Register extends VBox {

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtConfirmPassword;

    @FXML
    private Label lblError;

    private IUserRepository userRepository;
    private Scene previous;

    public Register(IUserRepository userRepository, Scene previous) {
        super();
        loadFXML();
        this.userRepository = userRepository;
        this.previous = previous;
        Platform.runLater(() -> requestFocus());
    }

    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Register.fxml"));
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
    private void onBtnRegisterAction(ActionEvent event) {
        String name = txtName.getText();
        String login = txtLogin.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        if (password.equals(confirmPassword)) {
            User user = userRepository.insert(new User(name, login, password));
            if (user == null) {
                lblError.setText("Nome de usuário inválido");
            }
            else {
                goBack();
            }
        }
        else {
            lblError.setText("As senhas não coincidem!");
        }
    }

    @FXML
    private void onBtnCancelAction(ActionEvent event) {
        goBack();
    }

    private void goBack() {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(previous);
        stage.centerOnScreen();
    }

}
