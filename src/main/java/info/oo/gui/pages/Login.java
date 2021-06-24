package info.oo.gui.pages;

import java.io.IOException;

import info.oo.entities.User;
import info.oo.repositories.interfaces.IUserRepository;
import info.oo.services.interfaces.IUserAuthenticator;
import info.oo.utils.VoidCallback;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Login extends VBox {

    @FXML
    private Label lblError;

    @FXML
    private TextField txtLogin;

    @FXML
    private TextField txtPassword;

    private IUserAuthenticator authenticator;
    private IUserRepository userRepository;
    private VoidCallback<User> onSuccess;

    public Login(IUserAuthenticator authenticator, IUserRepository userRepository,  VoidCallback<User> onSuccess) {
        super();
        loadFXML();
        this.authenticator = authenticator;
        this.userRepository = userRepository;
        this.onSuccess = onSuccess;
        Platform.runLater(() -> requestFocus());
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
    private void onBtnLoginAction(ActionEvent event) {
        String login = txtLogin.getText();
        String password = txtPassword.getText();
        Integer userId = authenticator.authenticate(login, password);
        if (userId == null) {
            lblError.setText("Login ou senha incorretos.");
        }
        else {
            User user = userRepository.getById(userId);
            onSuccess.call(user);
        }
    }

    @FXML
    private void onBtnRegisterAction(ActionEvent event) {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(
            new Scene(
                new Register(userRepository, getScene())
            )
        );
        stage.centerOnScreen();
    }

}
