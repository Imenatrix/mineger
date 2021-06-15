package info.oo.gui.pages;

import java.io.IOException;

import info.oo.dao.interfaces.IUserDAO;
import info.oo.entities.User;
import info.oo.utils.VoidCallback;
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

    private IUserDAO userDAO;
    private VoidCallback<User> onSuccess;

    public Login(IUserDAO userDAO,  VoidCallback<User> onSuccess) {
        super();
        loadFXML();
        this.userDAO = userDAO;
        this.onSuccess = onSuccess;
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
        User user = userDAO.getByLoginAndPassword(login, password);
        if (user == null) {
            lblError.setText("Login ou senha incorretos.");
        }
        else {
            onSuccess.call(user);
        }
    }

    @FXML
    void onBtnRegisterAction(ActionEvent event) {

    }

}
