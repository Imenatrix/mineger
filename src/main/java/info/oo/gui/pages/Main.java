package info.oo.gui.pages;

import java.net.URL;
import java.util.ResourceBundle;

import info.oo.entities.ModFile;
import info.oo.entities.ModModule;
import info.oo.gui.components.ModPod;
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
    private ObservableList<ModFile> modFiles;

    public Main(ObservableList<ModModule> modModules) {
        this.modModules = modModules;
        this.modFiles = FXCollections.observableArrayList();
        for (ModModule modModule : modModules) {
            modFiles.addAll(modModule.getModFiles());
        }
    }

    @FXML
    public void initialize() {
        listModModules.setItems(modModules);
        listModFiles.setItems(FXCollections.observableArrayList(modFiles));
        configureCellFactories();
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