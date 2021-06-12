package info.oo.gui.pages;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import info.oo.dao.interfaces.IModFileDAO;
import info.oo.entities.ModFile;
import info.oo.entities.ModLoader;
import info.oo.entities.ModModule;
import info.oo.gui.components.ModPod;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    @FXML
    private Label lblPaginator;

    private int page;
    private int totalPages;
    private IModFileDAO modFileDAO;
    private ObservableList<ModModule> modModules;
    private ModModule selectedModModule;
    private ObservableList<String> versions;
    private ObservableList<ModLoader> modLoaders;

    public Main(IModFileDAO modFileDAO, ObservableList<String> versions, ObservableList<ModModule> modModules, ObservableList<ModLoader> modLoaders) {
        this.page = 0;
        this.totalPages = 1;
        this.modFileDAO = modFileDAO;
        this.modModules = modModules;
        this.versions = versions;
        this.modLoaders = modLoaders;
    }

    @FXML
    public void initialize() {
        listModModules.setItems(modModules);
        listModModules.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ModModule>() {
            @Override
            public void changed(ObservableValue<? extends ModModule> observable, ModModule oldValue, ModModule newValue) {
                selectedModModule = newValue;
                totalPages = modFileDAO.getTotalPagesByModLoaderIdAndMinecraftVersion(
                    20,
                    selectedModModule.getModLoader().getId(),
                    selectedModModule.getMinecraftVersion()
                );
                page = 0;
                updateLblPaginator();
                updateListModFiles();
            }
        });
        configureCellFactories();
        updateLblPaginator();
    }

    private void updateLblPaginator() {
        lblPaginator.setText((page + 1) + " de " + totalPages);
    }

    private void updateListModFiles() {
        ArrayList<ModFile> modFiles = modFileDAO.getPaginatedByModLoaderIdAndMinecraftVersion(
            20,
            page,
            selectedModModule.getModLoader().getId(),
            selectedModModule.getMinecraftVersion()
        );
        listModFiles.setItems(FXCollections.observableArrayList(modFiles));
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

    @FXML
    private void onBtnPreviousAction(ActionEvent event) {
        event.consume();
        if (page > 0) {
            page--;
            updateLblPaginator();
            updateListModFiles();
        }
    }

    @FXML
    private void onBtnNextAction(ActionEvent event) {
        event.consume();
        if (page < (totalPages - 1)) {
            page++;
            updateLblPaginator();
            updateListModFiles();
        }
    }

    @FXML
    void onBtnNewAction(ActionEvent event) {
        Stage popup = new Stage();
        NewModModule newModModule = new NewModModule(versions, modLoaders);
        Scene scene = new Scene(newModModule);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show();
    }

}