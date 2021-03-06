package info.oo.gui.pages;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import info.oo.entities.ModLoader;
import info.oo.entities.ModOrigin;
import info.oo.utils.QuadriCallback;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
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

    @FXML
    private CheckBox chkNonAdded;

    private ObservableList<String> versions;
    private ObservableList<ModLoader> modLoaders;
    private ObservableList<ModOrigin> modOrigins;
    
    private Integer modLoaderId;
    private Integer modOriginId;
    private String version;
    private Boolean nonAdded;
    
    private QuadriCallback<ModLoader, ModOrigin, String, Boolean> onApply;

    public Filter(
        ObservableList<ModLoader> modLoaders,
        ObservableList<ModOrigin> modOrigins,
        ObservableList<String> versions,
        Integer modLoaderId,
        Integer modOriginId,
        String version,
        boolean nonAdded,
        QuadriCallback<ModLoader, ModOrigin, String, Boolean> onApply
    ) {
        super();
        this.modLoaders = modLoaders;
        this.modOrigins = modOrigins;
        this.versions = versions;
        
        this.modLoaderId = modLoaderId;
        this.modOriginId = modOriginId;
        this.version = version;
        this.nonAdded = nonAdded;

        this.onApply = onApply;

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
    private void initialize() {
        cbVersion.setItems(versions);
        cbModLoader.setItems(modLoaders);
        cbOrigin.setItems(modOrigins);
        trySetDefaultValues();
        setCellFactories();
    }

    private void trySetDefaultValues() {
        try {
            setDefaultValues();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setDefaultValues() throws NullPointerException {
        cbVersion.getSelectionModel().select(
            versions.stream()
                .filter(item -> item.equals(version))
                .findFirst()
                .orElseGet(null)
        );
        cbModLoader.getSelectionModel().select(
            modLoaders.stream()
                .filter(item -> item.getId() == modLoaderId)
                .findFirst()
                .orElseGet(null)
        );
        cbOrigin.getSelectionModel().select(
            modOrigins.stream()
                .filter(item -> item.getId() == modOriginId)
                .findFirst()
                .orElseGet(null)
        );
        chkNonAdded.setSelected(nonAdded);
    }

    private void setCellFactories() {
        setCbOriginCellFactories();
        setCbModLoaderCellFactories();
    }

    private void setCbOriginCellFactories() {
        cbOrigin.setCellFactory(combo -> new ListCell<ModOrigin>() {
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

    private void setCbModLoaderCellFactories() {
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
        event.consume();
        close();
    }

    @FXML
    private void onBtnApplyAction(ActionEvent event) {
        event.consume();
        onApply.call(
            cbModLoader.getSelectionModel().getSelectedItem(),
            cbOrigin.getSelectionModel().getSelectedItem(),
            cbVersion.getSelectionModel().getSelectedItem(),
            chkNonAdded.isSelected()
        );
        close();
    }

}
