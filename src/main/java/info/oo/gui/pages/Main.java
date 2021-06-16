package info.oo.gui.pages;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import info.oo.dao.interfaces.IModFileDAO;
import info.oo.dao.interfaces.IModModuleDAO;
import info.oo.entities.ModFile;
import info.oo.entities.ModLoader;
import info.oo.entities.ModModule;
import info.oo.entities.ModOrigin;
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
    private ObservableList<ModLoader> modLoaders;
    private ObservableList<String> minecraftVersions;
    private ObservableList<ModOrigin> modOrigins;
    private IModFileDAO modFileDAO;
    private IModModuleDAO modModuleDAO;
    private IModModuleInstaller installer;


    public Main(
        User user,
        ObservableList<ModModule> modModules,
        ObservableList<ModLoader> modLoaders,
        ObservableList<String> minecraftVersions,
        ObservableList<ModOrigin> modOrigins,
        IModFileDAO modFileDAO,
        IModModuleDAO modModuleDAO,
        IModModuleInstaller installer
    ) {
        this.page = 0;
        this.totalPages = 1;
        this.count = 0;
        this.user = user;
        this.modModules = modModules;
        this.minecraftVersions = minecraftVersions;
        this.modLoaders = modLoaders;
        this.modOrigins = modOrigins;
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
                totalPages = modFileDAO.getTotalPages(
                    20,
                    newValue.getModLoader().getId(),
                    newValue.getMinecraftVersion()
                );
                page = 0;
                updateLblPaginator();
                updateListModFiles(newValue);
            }
        });
        setCellFactories();
        updateLblPaginator();
    }

    private void updateLblPaginator() {
        lblPaginator.setText((page + 1) + " de " + totalPages);
    }

    private void updateListModFiles(ModModule modModule) {
        ArrayList<ModFile> modFiles = modFileDAO.getPaginated(
            20,
            page,
            modModule.getModLoader().getId(),
            modModule.getMinecraftVersion()
        );
        setListModFilesCellFactory();
        listModFiles.setItems(FXCollections.observableArrayList(modFiles));
    }

    private void updateListModFilesWithSearch(ModModule modModule) {
        totalPages = modFileDAO.getTotalPages(
            20,
            modModule.getModLoader().getId(),
            modModule.getMinecraftVersion(),
            txtBusca.getText()
        );
        ArrayList<ModFile> modFiles = modFileDAO.getPaginated(
            20,
            page,
            modModule.getModLoader().getId(),
            modModule.getMinecraftVersion(),
            txtBusca.getText()
        );
        updateLblPaginator();
        setListModFilesCellFactory();
        listModFiles.setItems(FXCollections.observableArrayList(modFiles));
    }

    private void setCellFactories() {
        setListModModulesCellFactory();
        setListModFilesCellFactory();
    }

    private void setListModModulesCellFactory() {
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
    }

    private void setListModFilesCellFactory() {
        listModFiles.setCellFactory(list -> new ModPod(
            getSelectedModModule(),
            modModuleDAO
        ));
    }

    @FXML
    private void onBtnPreviousAction(ActionEvent event) {
        event.consume();
        if (page > 0) {
            page--;
            updateLblPaginator();
            updateListModFiles(getSelectedModModule());
        }
    }

    @FXML
    private void onBtnNextAction(ActionEvent event) {
        event.consume();
        if (page < (totalPages - 1)) {
            page++;
            updateLblPaginator();
            updateListModFiles(getSelectedModModule());
        }
    }

    @FXML
    void onBtnNewAction(ActionEvent event) {
        Stage popup = new Stage();
        NewModModule newModModule = new NewModModule(user, modModules, minecraftVersions, modLoaders, modModuleDAO);
        Scene scene = new Scene(newModModule);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show();
    }

    @FXML
    private void onBtnDeleteAction(ActionEvent event) {
        event.consume();
        ModModule modModule = getSelectedModModule();
        if (modModule != null) {
            modModules.remove(modModule);
            user.getModModules().remove(modModule);
            modModuleDAO.delete(modModule);
        }
    }

    private ModModule getSelectedModModule() {
        return listModModules.getSelectionModel().getSelectedItem();
    }

    private void onInstallerFetchOne(Warning warning) {
        Platform.runLater(() -> {
            warning.setLblMessageText(count + " / " + getSelectedModModule().getModFiles().size());
        });
        count++;
    }

    private void onInstallerFinish(Warning warning) {
        Platform.runLater(() -> {
            warning.setLblMessageText(getSelectedModModule().getName() + " instalado!");
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
        warning.setLblMessageText("0 / " + getSelectedModModule().getModFiles().size());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show();
        count = 0;
        Thread thread = new Thread(() -> installer.install(
            getSelectedModModule(),
            modFile -> onInstallerFetchOne(warning),
            modFile -> onInstallerFinish(warning)
        ));
        thread.start();
        
    }

    @FXML
    void onBtnSearchAction(ActionEvent event) {
        event.consume();
        updateListModFilesWithSearch(getSelectedModModule());
    }

    @FXML
    void onBtnFilterAction(ActionEvent event) {
        event.consume();
        Stage popup = new Stage();
        Filter filter = new Filter(minecraftVersions, modLoaders, modOrigins);
        Scene scene = new Scene(filter);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show();
        count = 0;
    }

}