package info.oo;

import java.io.IOException;
import java.util.ArrayList;

import info.oo.dao.MinecraftVersionDAO;
import info.oo.dao.ModDAO;
import info.oo.dao.ModFileDAO;
import info.oo.dao.ModLoaderDAO;
import info.oo.dao.ModModuleDAO;
import info.oo.dao.ModOriginDAO;
import info.oo.dao.UserDAO;
import info.oo.dao.interfaces.IMinecraftVersionDAO;
import info.oo.dao.interfaces.IModDAO;
import info.oo.dao.interfaces.IModFileDAO;
import info.oo.dao.interfaces.IModLoaderDAO;
import info.oo.dao.interfaces.IModModuleDAO;
import info.oo.dao.interfaces.IModOriginDAO;
import info.oo.dao.interfaces.IUserDAO;
import info.oo.entities.ModLoader;
import info.oo.entities.ModModule;
import info.oo.entities.User;
import info.oo.gui.pages.Main;
import info.oo.utils.ModModuleInstaller;
import info.oo.utils.interfaces.IModModuleInstaller;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class HelloFX extends Application {

    public void start(Stage stage) throws IOException {

        IModLoaderDAO modLoaderDAO = new ModLoaderDAO();
        IModOriginDAO modOriginDAO = new ModOriginDAO();
        IMinecraftVersionDAO minecraftVersionDAO = new MinecraftVersionDAO();

        IModDAO modDAO = new ModDAO(modLoaderDAO, modOriginDAO);
        
        IModFileDAO modFileDAO = new ModFileDAO(modDAO);

        IModModuleDAO modModuleDAO = new ModModuleDAO(modFileDAO, modLoaderDAO);

        IUserDAO userDAO = new UserDAO(modModuleDAO);
        
        IModModuleInstaller installer = new ModModuleInstaller();

        User user = userDAO.getById(1);
        ArrayList<ModModule> modModules = user.getModModules();
        ArrayList<ModLoader> modLoaders = modLoaderDAO.getAll();
        ArrayList<String> minecraftVersions = minecraftVersionDAO.getAll();
        
        stage.setScene(
            new Scene(
                loadMainPage(
                    user,
                    modModules,
                    minecraftVersions,
                    modLoaders,
                    modFileDAO,
                    modModuleDAO,
                    installer
                )
            )
        );
        stage.show();
    }

    private Parent loadMainPage(
        User user,
        ArrayList<ModModule> modModules,
        ArrayList<String> minecraftVersions,
        ArrayList<ModLoader> modLoaders,
        IModFileDAO modFileDAO,
        IModModuleDAO modModuleDAO,
        IModModuleInstaller installer
    ) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("gui/pages/Main.fxml"));
        loader.setControllerFactory(aClass -> new Main(
            user,
            observable(modModules),
            observable(minecraftVersions),
            observable(modLoaders),
            modFileDAO,
            modModuleDAO,
            installer
        ));
        return loader.load();
    }

    private <T> ObservableList<T> observable(ArrayList<T> list) {
        return FXCollections.observableArrayList(list);
    }

    public static void main(String[] args) {
        launch(args);
    }

}