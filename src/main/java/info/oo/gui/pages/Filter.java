package info.oo.gui.pages;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import info.oo.entities.ModLoader;
import info.oo.entities.ModOrigin;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Filter extends GridPane {

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

    @FXML
    private ComboBox<ModOrigin> cbOrigin;

    private ObservableList<String> versions;
    private ObservableList<ModLoader> modLoaders;
    private ObservableList<ModOrigin> modOrigins;

    public Filter(ObservableList<String> versions, ObservableList<ModLoader> modLoaders, ObservableList<ModOrigin> modOrigins) {
        super();
        this.versions = versions;
        this.modLoaders = modLoaders;
        this.modOrigins = modOrigins;
        loadFXML();
    }

    private void loadFXML() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Filter.fxml"));
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
    void initialize() {
        cbVersion.setItems(versions);
        cbModLoader.setItems(modLoaders);
        cbOrigin.setItems(modOrigins);
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
        cbOrigin.setButtonCell(new ListCell<ModOrigin>() {
            @Override
            protected void updateItem(ModOrigin item, boolean empty) {
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
        close();
    }

    @FXML
    void onBtnApplyAction(ActionEvent event) {
        
    }

}
