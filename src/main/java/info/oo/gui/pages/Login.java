package info.oo.gui.pages;

import java.io.IOException;

import info.oo.dao.interfaces.IUserDAO;
import info.oo.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    private Parent page;
    private IUserDAO userDAO;

    public Login(Parent page, IUserDAO userDAO) {
        super();
        loadFXML();
        this.page = page;
        this.userDAO = userDAO;
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
            Scene scene = getScene();
            scene.setRoot(page);
            scene.getWindow().sizeToScene();
            scene.getWindow().centerOnScreen();
        }
    }

    @FXML
    void onBtnRegisterAction(ActionEvent event) {

    }

}
