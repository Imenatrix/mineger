package info.oo.gui.components;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import info.oo.dao.interfaces.IModModuleDAO;
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
    private IModModuleDAO modModuleDAO;

    public ModPod(ModModule modModule, IModModuleDAO modModuleDAO) {
        super();
        loadFXML();
        this.modModule = modModule;
        this.modModuleDAO = modModuleDAO;
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

    private void updateBtnInstallText() {
        if (isInstalled()) {
            btnInstall.setText("Remover");
        }
        else {
            btnInstall.setText("Adicionar");
        }
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
            updateBtnInstallText();
        }
    }

    @FXML
    void onBtnInstallAction(ActionEvent event) {
        if (isInstalled()) {
            modModule.getModFiles().remove(
                modModule.getModFiles()
                    .stream()
                    .filter(item -> item.getId() == modFile.getId())
                    .findFirst()
                    .get()
            );
            modModuleDAO.removeModFile(modModule, modFile);
        }
        else {
            modModule.getModFiles().add(modFile);
            modModuleDAO.addModFile(modModule, modFile);
        }
        updateBtnInstallText();
    }
}