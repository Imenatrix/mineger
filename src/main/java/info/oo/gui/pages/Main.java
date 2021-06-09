package info.oo.gui.pages;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import info.oo.dao.interfaces.IModFileDAO;
import info.oo.entities.ModFile;
import info.oo.entities.ModModule;
import info.oo.gui.components.ModPod;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    @FXML
    private Label lblPaginator;

    private int page;
    private int totalPages;
    private IModFileDAO modFileDAO;
    private ObservableList<ModModule> modModules;

    public Main(IModFileDAO modFileDAO, ObservableList<ModModule> modModules) {
        this.page = 0;
        this.totalPages = 0;
        this.modFileDAO = modFileDAO;
        this.modModules = modModules;
    }

    @FXML
    public void initialize() {
        listModModules.setItems(modModules);
        configureCellFactories();

        listModModules.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ModModule>() {
            @Override
            public void changed(ObservableValue<? extends ModModule> observable, ModModule oldValue, ModModule newValue) {
                totalPages = modFileDAO.getTotalPagesByModLoaderIdAndMinecraftVersion(
                    20,
                    newValue.getModLoader().getId(),
                    newValue.getMinecraftVersion()
                );
                ArrayList<ModFile> modFiles = modFileDAO.getPaginatedByModLoaderIdAndMinecraftVersion(
                    20,
                    page,
                    newValue.getModLoader().getId(),
                    newValue.getMinecraftVersion()
                );
                listModFiles.setItems(FXCollections.observableArrayList(modFiles));
                updateLblPaginator();
            }
        });
    }

    private void updateLblPaginator() {
        lblPaginator.setText((page + 1) + " de " + totalPages);
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
        }
        updateLblPaginator();
    }

    @FXML
    private void onBtnNextAction(ActionEvent event) {
        event.consume();
        if (page < (totalPages - 1)) {
            page++;
        }
        updateLblPaginator();
    }

}