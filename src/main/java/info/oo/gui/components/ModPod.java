package info.oo.gui.components;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import info.oo.entities.ModFile;
import info.oo.entities.ModModule;
import info.oo.entities.User;
import info.oo.repositories.interfaces.IUserRepository;
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

    private User user;
    private ModModule modModule;
    private ModFile modFile;
    private IUserRepository userRepository;

    public ModPod(User user, ModModule modModule, IUserRepository userRepository) {
        super();
        loadFXML();
        this.user = user;
        this.modModule = modModule;
        this.userRepository = userRepository;
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
            btnInstall.setText("\ue872");
            btnInstall.setStyle("-fx-background-color: #C84B31");
        }
        else {
            btnInstall.setText("\ue2c4");
            btnInstall.setStyle("-fx-background-color: #24914D");
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
            user.getModModules().stream()
                .filter(item -> item.getId() == modModule.getId())
                .findFirst()
                .get()
                .getModFiles()
                .removeIf(item -> item.getId() == modFile.getId());
            userRepository.save(user);
        }
        else {
            user.getModModules().stream()
                .filter(item -> item.getId() == modModule.getId())
                .findFirst()
                .get()
                .getModFiles()
                .add(modFile);
            userRepository.save(user);
        }
        updateBtnInstallText();
    }
    
}