package info.oo.gui.pages;

import java.net.URL;
import java.util.ResourceBundle;

import info.oo.entities.ModFile;
import info.oo.entities.ModModule;
import info.oo.gui.components.ModPod;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Main {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtBusca;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnFiltrar;

    @FXML
    private ListView<ModModule> listModModules;

    @FXML
    private ListView<ModFile> listModFiles;

    private ObservableList<ModModule> modModules;

    public Main(ObservableList<ModModule> modModules) {
        this.modModules = modModules;
    }

    @FXML
    public void initialize() {
        listModModules.setItems(modModules);
        configureCellFactories();

        listModModules.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ModModule>() {
            @Override
            public void changed(ObservableValue<? extends ModModule> observable, ModModule oldValue, ModModule newValue) {
                listModFiles.setItems(FXCollections.observableArrayList(newValue.getModFiles()));
            }
        });
    }

    private void configureCellFactories() {
        listModModules.setCellFactory(list -> new ListCell<ModModule>() {
            @Override
            public void updateItem(ModModule item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText("");
                }
                else {
                    setText(item.getName());
                }
            }
        });

        listModFiles.setCellFactory(list -> new ModPod());
    }

}