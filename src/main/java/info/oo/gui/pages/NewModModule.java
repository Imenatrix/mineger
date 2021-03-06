package info.oo.gui.pages;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import info.oo.entities.ModLoader;
import info.oo.entities.ModModule;
import info.oo.entities.User;
import info.oo.repositories.interfaces.IUserRepository;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class NewModModule extends GridPane {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtName;

    @FXML
    private ComboBox<String> cbVersion;
    
    @FXML
    private ComboBox<ModLoader> cbModLoader;

    private User user;
    private ObservableList<ModModule> modModules;
    private ObservableList<String> versions;
    private ObservableList<ModLoader> modLoaders;
    private IUserRepository userRepository;

    public NewModModule(
        User user,
        ObservableList<ModModule> modModules,
        ObservableList<String> versions,
        ObservableList<ModLoader> modLoaders,
        IUserRepository userRepository
    ) {
        super();
        this.user = user;
        this.modModules = modModules;
        this.versions = versions;
        this.modLoaders = modLoaders;
        this.userRepository = userRepository;
        loadFXML();
    }

    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewModModule.fxml"));
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
    private void initialize() {
        cbVersion.setItems(versions);
        cbModLoader.setItems(modLoaders);
        cbModLoader.setCellFactory(combo -> new ListCell<ModLoader>() {
            @Override
            protected void updateItem(ModLoader item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText("");
                }
                else {
                    setText(item.getName());
                }
            }
        });
        cbModLoader.setButtonCell(new ListCell<ModLoader>() {
            @Override
            protected void updateItem(ModLoader item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText("");
                }
                else {
                    setText(item.getName());
                }
            }
        });
    }

    @FXML
    private void onBtnCancelAction(ActionEvent event) {
        close();
    }

    @FXML
    private void onBtnSaveAction(ActionEvent event) {
        ModModule modModule = new ModModule(
            txtName.getText(),
            cbVersion.getSelectionModel().getSelectedItem(),
            cbModLoader.getSelectionModel().getSelectedItem()
        );
        modModules.add(modModule);
        user.getModModules().add(modModule);
        userRepository.save(user);
        close();
    }

}
