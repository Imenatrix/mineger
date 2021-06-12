package info.oo.gui.pages;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import info.oo.entities.ModLoader;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

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

    private ObservableList<ModLoader> modLoaders;
    private ObservableList<String> versions;

    public NewModModule(ObservableList<String> versions, ObservableList<ModLoader> modLoaders) {
        super();
        this.versions = versions;
        this.modLoaders = modLoaders;
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

    @FXML
    void initialize() {
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
    void onBtnCancelAction(ActionEvent event) {

    }

    @FXML
    void onBtnSaveAction(ActionEvent event) {
        
    }

}
