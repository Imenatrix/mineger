package info.oo.gui.pages;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Warning extends VBox {

    @FXML
    private Label lblMessage;

    @FXML
    private Button btnDimiss;

    public Warning() {
        super();
        loadFXML();
    }

    public void setBtnDimissDisable(boolean disable) {
        btnDimiss.setDisable(disable);
    }

    public void setLblMessageText(String text) {
        lblMessage.setText(text);
    }

    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Warning.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void close() {
        Stage stage = (Stage) getScene().getWindow();
        stage.close();
    }

    @FXML
    void onBtnDimissAction(ActionEvent event) {
        event.consume();
        close();
    }

}
