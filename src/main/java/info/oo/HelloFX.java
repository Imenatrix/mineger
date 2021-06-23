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
import info.oo.entities.ModOrigin;
import info.oo.entities.User;
import info.oo.gui.pages.Login;
import info.oo.gui.pages.Main;
import info.oo.services.ModModuleInstaller;
import info.oo.services.interfaces.IModModuleInstaller;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class HelloFX extends Application {

    public void start(Stage stage) throws IOException {

        IModDAO modDAO = new ModDAO();
        IUserDAO userDAO = new UserDAO();
        IModFileDAO modFileDAO = new ModFileDAO();
        IModLoaderDAO modLoaderDAO = new ModLoaderDAO();
        IModOriginDAO modOriginDAO = new ModOriginDAO();
        IModModuleDAO modModuleDAO = new ModModuleDAO();
        IModModuleInstaller installer = new ModModuleInstaller();
        IMinecraftVersionDAO minecraftVersionDAO = new MinecraftVersionDAO();

        ArrayList<ModLoader> modLoaders = modLoaderDAO.getAll();
        ArrayList<String> minecraftVersions = minecraftVersionDAO.getAll();
        ArrayList<ModOrigin> modOrigins = modOriginDAO.getAll();

        Font.loadFont(getClass().getResourceAsStream("fonts/MaterialIcons-Regular.ttf"), 10);
        
        Scene login = new Scene(new Login(userDAO, user -> {
            try {
                Scene main = new Scene(loadMainPage(
                    user,
                    user.getModModules(),
                    modLoaders,
                    minecraftVersions,
                    modOrigins,
                    modFileDAO,
                    modModuleDAO,
                    installer
                ));
                stage.setScene(main);
                stage.centerOnScreen();
            }
            catch (IOException e) {
                System.out.println(e);
            }
        }));
        stage.setScene(login);
        stage.show();
    }

    private Parent loadMainPage(
        User user,
        ArrayList<ModModule> modModules,
        ArrayList<ModLoader> modLoaders,
        ArrayList<String> minecraftVersions,
        ArrayList<ModOrigin> modOrigins,
        IModFileDAO modFileDAO,
        IModModuleDAO modModuleDAO,
        IModModuleInstaller installer
    ) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("gui/pages/Main.fxml"));
        loader.setControllerFactory(aClass -> new Main(
            user,
            observable(modModules),
            observable(modLoaders),
            observable(minecraftVersions),
            observable(modOrigins),
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