package info.oo.gui.components;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import info.oo.entities.ModFile;
import info.oo.entities.ModModule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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

    @FXML
    private Button btnInstall;

    private ModModule modModule;
    private ModFile modFile;

    public ModPod(ModModule modModule) {
        super();
        loadFXML();
        this.modModule = modModule;
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

    private boolean isInstalled() {
        return modModule
            .getModFiles()
            .stream()
            .map(item -> item.getId())
            .anyMatch(id -> id == modFile.getId());
    }

    @Override
    protected void updateItem(ModFile item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            container.setVisible(false);
        }
        else {
            container.setVisible(true);
            modFile = item;
            lblName.setText(item.getMod().getName());
            lblSummary.setText(item.getMod().getSummary());
            if (isInstalled()) {
                btnInstall.setText("Remover");
            }
            else {
                btnInstall.setText("Adicionar");
            }
        }
    }
}