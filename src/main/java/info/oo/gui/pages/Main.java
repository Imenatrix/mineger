package info.oo.gui.pages;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import info.oo.dao.interfaces.IModFileDAO;
import info.oo.dao.interfaces.IModModuleDAO;
import info.oo.entities.ModFile;
import info.oo.entities.ModLoader;
import info.oo.entities.ModModule;
import info.oo.entities.User;
import info.oo.gui.components.ModPod;
import info.oo.services.interfaces.IModModuleInstaller;
import javafx.application.Platform;
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
    private int count;
    private User user;
    private ObservableList<ModModule> modModules;
    private ObservableList<String> versions;
    private ObservableList<ModLoader> modLoaders;
    private IModFileDAO modFileDAO;
    private IModModuleDAO modModuleDAO;
    private IModModuleInstaller installer;


    public Main(User user, ObservableList<ModModule> modModules, ObservableList<String> versions, ObservableList<ModLoader> modLoaders, IModFileDAO modFileDAO, IModModuleDAO modModuleDAO, IModModuleInstaller installer) {
        this.page = 0;
        this.totalPages = 1;
        this.count = 0;
        this.user = user;
        this.modModules = modModules;
        this.versions = versions;
        this.modLoaders = modLoaders;
        this.modFileDAO = modFileDAO;
        this.modModuleDAO = modModuleDAO;
        this.installer = installer;
    }

    @FXML
    public void initialize() {
        listModModules.setItems(modModules);
        listModModules.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ModModule>() {
            @Override
            public void changed(ObservableValue<? extends ModModule> observable, ModModule oldValue, ModModule newValue) {
                totalPages = modFileDAO.getTotalPagesByModLoaderIdAndMinecraftVersion(
                    20,
                    newValue.getModLoader().getId(),
                    newValue.getMinecraftVersion()
                );
                page = 0;
                updateLblPaginator();
                updateListModFiles(newValue);
            }
        });
        configureCellFactories();
        updateLblPaginator();
    }

    private void updateLblPaginator() {
        lblPaginator.setText((page + 1) + " de " + totalPages);
    }

    private void updateListModFiles(ModModule modModule) {
        ArrayList<ModFile> modFiles = modFileDAO.getPaginatedByModLoaderIdAndMinecraftVersion(
            20,
            page,
            modModule.getModLoader().getId(),
            modModule.getMinecraftVersion()
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

        listModFiles.setCellFactory(list -> new ModPod(
            listModModules.getSelectionModel().getSelectedItem(),
            modModuleDAO
        ));
    }

    @FXML
    private void onBtnPreviousAction(ActionEvent event) {
        event.consume();
        if (page > 0) {
            page--;
            updateLblPaginator();
            updateListModFiles(listModModules.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void onBtnNextAction(ActionEvent event) {
        event.consume();
        if (page < (totalPages - 1)) {
            page++;
            updateLblPaginator();
            updateListModFiles(listModModules.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    void onBtnNewAction(ActionEvent event) {
        Stage popup = new Stage();
        NewModModule newModModule = new NewModModule(user, modModules, versions, modLoaders, modModuleDAO);
        Scene scene = new Scene(newModModule);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show();
    }

    @FXML
    private void onBtnDeleteAction(ActionEvent event) {
        event.consume();
        ModModule modModule = listModModules.getSelectionModel().getSelectedItem();
        if (modModule != null) {
            modModules.remove(modModule);
            user.getModModules().remove(modModule);
            modModuleDAO.delete(modModule);
        }
    }

    private void onInstallerFetchOne(Warning warning) {
        Platform.runLater(() -> {
            warning.setLblMessageText(count + " / " + listModModules.getSelectionModel().getSelectedItem().getModFiles().size());
        });
        count++;
    }

    private void onInstallerFinish(Warning warning) {
        Platform.runLater(() -> {
            warning.setLblMessageText(listModModules.getSelectionModel().getSelectedItem().getName() + " instalado!");
            warning.setBtnDimissDisable(false);
        });
    }

    @FXML
    void onBtnInstallAction(ActionEvent event) {
        event.consume();
        Stage popup = new Stage();
        Warning warning = new Warning();
        Scene scene = new Scene(warning);
        warning.setBtnDimissDisable(true);
        warning.setLblMessageText("0 / " + listModModules.getSelectionModel().getSelectedItem().getModFiles().size());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show();
        count = 0;
        Thread thread = new Thread(() -> installer.install(
            listModModules.getSelectionModel().getSelectedItem(),
            modFile -> onInstallerFetchOne(warning),
            modFile -> onInstallerFinish(warning)
        ));
        thread.start();
        
    }

}