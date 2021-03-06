package info.oo.gui.pages;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import info.oo.entities.ModFile;
import info.oo.entities.ModLoader;
import info.oo.entities.ModModule;
import info.oo.entities.ModOrigin;
import info.oo.entities.User;
import info.oo.gui.components.ModPod;
import info.oo.repositories.interfaces.IModFilePageRepository;
import info.oo.repositories.interfaces.IUserRepository;
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
    private ListView<ModModule> listModModules;

    @FXML
    private ListView<ModFile> listModFiles;

    @FXML
    private Label lblPaginator;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnFilter;

    private static final int limit = 20;

    private int page;
    private int totalPages;
    private int count;
    private User user;
    private ModModule modModule;
    private Integer modLoaderId;
    private Integer modOriginId;
    private String minecraftVersion;
    private String search;
    private Boolean nonAdded;
    private ObservableList<ModModule> modModules;
    private ObservableList<ModLoader> modLoaders;
    private ObservableList<String> minecraftVersions;
    private ObservableList<ModOrigin> modOrigins;
    private IModFilePageRepository modFilePageRepository;
    private IUserRepository userRepository;
    private IModModuleInstaller installer;
    

    public Main(
        User user,
        ObservableList<ModModule> modModules,
        ObservableList<ModLoader> modLoaders,
        ObservableList<String> minecraftVersions,
        ObservableList<ModOrigin> modOrigins,
        IModFilePageRepository modFilePageRepository,
        IUserRepository userRepository,
        IModModuleInstaller installer
    ) {
        this.page = 0;
        this.totalPages = 1;
        this.count = 0;
        this.nonAdded = true;
        this.user = user;
        this.modModules = modModules;
        this.minecraftVersions = minecraftVersions;
        this.modLoaders = modLoaders;
        this.modOrigins = modOrigins;
        this.modFilePageRepository = modFilePageRepository;
        this.userRepository = userRepository;
        this.installer = installer;
    }

    @FXML
    public void initialize() {
        listModModules.setItems(modModules);
        addListModModulesListeners();
        setCellFactories();
        updateLblPaginator(totalPages);
    }

    private void addListModModulesListeners() {
        listModModules.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ModModule>() {
            @Override
            public void changed(ObservableValue<? extends ModModule> observable, ModModule oldValue, ModModule newValue) {
                if (txtBusca.getText().equals("")) {
                    search = null;
                }
                modModule = newValue;
                modLoaderId = modModule.getModLoader().getId();
                minecraftVersion = modModule.getMinecraftVersion();
                page = 0;
                enableFilterButtons();
                updateListModFiles();
            }
        });
    }

    private void updateLblPaginator(int totalPages) {
        lblPaginator.setText((page + 1) + " de " + totalPages);
    }

    private void updateTotalPages() {
        totalPages = getTotalPages();
    }

    private int getTotalPages() {
        return modFilePageRepository.getTotalPages(limit, modLoaderId, modOriginId, minecraftVersion, search);
    }
    
    private void updateListModFiles() {
        ArrayList<ModFile> modFiles = getPage();
        updateTotalPages();
        updateLblPaginator(totalPages);
        setListModFilesCellFactory();
        listModFiles.setItems(FXCollections.observableArrayList(modFiles));
    }

    private ArrayList<ModFile> getPage() {
        return modFilePageRepository.getPage(limit, page, modLoaderId, modOriginId, minecraftVersion, search);
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
            user,
            modModule,
            userRepository
        ));
    }

    @FXML
    private void onBtnPreviousAction(ActionEvent event) {
        event.consume();
        if (page > 0) {
            page--;
            if (nonAdded) {
                updateListModFiles();
            }
            else {
                filterOnlyAdded();
            }
        }
    }

    @FXML
    private void onBtnNextAction(ActionEvent event) {
        event.consume();
        if (page < (totalPages - 1)) {
            page++;
            if (nonAdded) {
                updateListModFiles();
            }
            else {
                filterOnlyAdded();
            }
        }
    }

    @FXML
    private void onBtnNewAction(ActionEvent event) {
        Stage popup = new Stage();
        NewModModule newModModule = new NewModModule(user, modModules, minecraftVersions, modLoaders, userRepository);
        Scene scene = new Scene(newModModule);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show();
    }

    @FXML
    private void onBtnDeleteAction(ActionEvent event) {
        event.consume();
        ModModule modModule = this.modModule;
        if (modModule != null) {
            modModules.remove(modModule);
            user.getModModules().remove(modModule);
            userRepository.save(user);
        }
    }

    private void onInstallerFetchOne(Warning warning) {
        Platform.runLater(() -> {
            warning.setLblMessageText(count + " / " + modModule.getModFiles().size());
        });
        count++;
    }

    private void onInstallerFinish(Warning warning) {
        Platform.runLater(() -> {
            warning.setLblMessageText(modModule.getName() + " instalado!");
            warning.setBtnDimissDisable(false);
        });
    }

    @FXML
    private void onBtnInstallAction(ActionEvent event) {
        event.consume();
        Stage popup = new Stage();
        Warning warning = new Warning();
        Scene scene = new Scene(warning);
        warning.setBtnDimissDisable(true);
        warning.setLblMessageText("0 / " + modModule.getModFiles().size());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show();
        count = 0;
        Thread thread = new Thread(() -> installer.install(
            modModule,
            modFile -> onInstallerFetchOne(warning),
            modModule -> onInstallerFinish(warning)
        ));
        thread.start();
        
    }

    private void enableFilterButtons() {
        btnFilter.setDisable(false);
        btnSearch.setDisable(false);
    }

    @FXML
    private void onBtnSearchAction(ActionEvent event) {
        event.consume();
        if (txtBusca.getText().equals("")) {
            search = null;
        }
        else {
            search = txtBusca.getText();
        }
        updateListModFiles();
    }

    @FXML
    private void onBtnFilterAction(ActionEvent event) {
        event.consume();
        if (txtBusca.getText().equals("")) {
            search = null;
        }
        Stage popup = new Stage();
        Filter filter = new Filter(
            modLoaders,
            modOrigins,
            minecraftVersions,
            modLoaderId,
            modOriginId,
            minecraftVersion,
            nonAdded,
            (
                modLoader,
                modOrigin,
                minecraftVersion,
                nonAdded
            ) -> onFilterApply(
                modLoader,
                modOrigin,
                minecraftVersion,
                nonAdded
            )
        );
        Scene scene = new Scene(filter);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setScene(scene);
        popup.show();
        count = 0;
    }

    private void onFilterApply(ModLoader modLoader, ModOrigin modOrigin, String minecraftVersion, Boolean nonAdded) {
        this.page = 0;
        this.nonAdded = nonAdded;
        this.modLoaderId = modLoader == null ? null : modLoader.getId();
        this.modOriginId = modOrigin == null ? null : modOrigin.getId();
        this.minecraftVersion = minecraftVersion;
        if (nonAdded) {
            updateListModFiles();
        }
        else {
            filterOnlyAdded();
        }
    }

    private void filterOnlyAdded() {
        Stream<ModFile> modFileStream = modModule.getModFiles().stream();
        if (modLoaderId != null) {
            modFileStream = modFileStream.filter(
                item -> item.getMod().getModLoader().getId() == modLoaderId
            );
        }
        if (modOriginId != null) {
            modFileStream = modFileStream.filter(
                item -> item.getMod().getModOrigin().getId() == modOriginId
            );
        }
        if (minecraftVersion != null) {
            modFileStream = modFileStream.filter(item -> item.getMinecraftVersion().equals(minecraftVersion));
        }
        ArrayList<ModFile> modFiles = new ArrayList<ModFile>(modFileStream.collect(Collectors.toList()));
        totalPages = (int) Math.ceil(modFiles.size() / (float) limit);
        updateLblPaginator(totalPages);
        setListModFilesCellFactory();
        listModFiles.setItems(FXCollections.observableArrayList(
            modFiles.subList(page * limit, Math.min(modFiles.size(), (page + 1) * limit))
        ));
    }

}