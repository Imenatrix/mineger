package info.oo.gui.pages;

import java.io.IOException;

import info.oo.dao.interfaces.IUserDAO;
import info.oo.entities.User;
import info.oo.repositories.interfaces.IUserRepository;
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

    private IUserDAO userDAO;
    private IUserRepository userRepository;
    private VoidCallback<User> onSuccess;

    public Login(IUserDAO userDAO, IUserRepository userRepository,  VoidCallback<User> onSuccess) {
        super();
        loadFXML();
        this.userDAO = userDAO;
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
    void onBtnLoginAction(ActionEvent event) {
        String login = txtLogin.getText();
        String password = txtPassword.getText();
        User loginUser = userDAO.getByLoginAndPassword(login, password);
        if (loginUser == null) {
            lblError.setText("Login ou senha incorretos.");
        }
        else {
            User user = userRepository.getById(loginUser.getId());
            onSuccess.call(user);
        }
    }

    @FXML
    void onBtnRegisterAction(ActionEvent event) {
        Stage stage = (Stage) getScene().getWindow();
        stage.setScene(
            new Scene(
                new Register(userDAO, getScene())
            )
        );
        stage.centerOnScreen();
    }

}
