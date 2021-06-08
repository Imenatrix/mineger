package info.oo.gui.components;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import info.oo.entities.ModFile;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class ModPod extends ListCell<ModFile> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label lblName;

    @FXML
    private Label lblSummary;

    @FXML HBox container;

    public ModPod() {
        super();
        loadFXML();
    }

    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ModPod.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    protected void updateItem(ModFile item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            this.container.setVisible(false);
        }
        else {
            this.container.setVisible(true);
            lblName.setText(item.getMod().getName());
        }
    }
}