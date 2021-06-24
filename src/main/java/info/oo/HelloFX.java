package info.oo;

import java.io.IOException;
import java.util.ArrayList;

import info.oo.dao.FileModuleDAO;
import info.oo.dao.MinecraftVersionDAO;
import info.oo.dao.ModDAO;
import info.oo.dao.ModFileDAO;
import info.oo.dao.ModLoaderDAO;
import info.oo.dao.ModModuleDAO;
import info.oo.dao.ModOriginDAO;
import info.oo.dao.UserDAO;
import info.oo.entities.ModLoader;
import info.oo.entities.ModModule;
import info.oo.entities.ModOrigin;
import info.oo.entities.User;
import info.oo.factories.ModFilesFactory;
import info.oo.factories.ModModulesFactory;
import info.oo.factories.ModsFactory;
import info.oo.factories.UserFactory;
import info.oo.gui.pages.Login;
import info.oo.gui.pages.Main;
import info.oo.repositories.ModFilePageRepository;
import info.oo.repositories.UserRepository;
import info.oo.services.ModModuleInstaller;
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

        ModDAO modDAO = new ModDAO();
        UserDAO userDAO = new UserDAO();
        ModFileDAO modFileDAO = new ModFileDAO();
        ModLoaderDAO modLoaderDAO = new ModLoaderDAO();
        ModOriginDAO modOriginDAO = new ModOriginDAO();
        ModModuleDAO modModuleDAO = new ModModuleDAO();
        FileModuleDAO fileModuleDAO = new FileModuleDAO();
        ModModuleInstaller installer = new ModModuleInstaller();
        MinecraftVersionDAO minecraftVersionDAO = new MinecraftVersionDAO();

        ModsFactory modsFactory = new ModsFactory();
        ModFilesFactory modFilesFactory = new ModFilesFactory(modsFactory);
        ModModulesFactory modModulesFactory = new ModModulesFactory(modFilesFactory);
        UserFactory userFactory = new UserFactory(modModulesFactory);

        UserRepository userRepository = new UserRepository(
            userDAO,
            modModuleDAO,
            modLoaderDAO,
            modFileDAO,
            fileModuleDAO,
            modDAO,
            modOriginDAO,
            userFactory
        );

        ModFilePageRepository modFilePageRepository = new ModFilePageRepository(modFileDAO, modDAO, modLoaderDAO, modOriginDAO, modFilesFactory);

        ArrayList<ModLoader> modLoaders = modLoaderDAO.getAll();
        ArrayList<String> minecraftVersions = minecraftVersionDAO.getAll();
        ArrayList<ModOrigin> modOrigins = modOriginDAO.getAll();

        Font.loadFont(getClass().getResourceAsStream("fonts/MaterialIcons-Regular.ttf"), 10);
        
        Login login = new Login(
            userDAO,
            userRepository,
            user -> loadMainScene(
                stage,
                user,
                modLoaders,
                minecraftVersions,
                modOrigins,
                modFilePageRepository,
                userRepository,
                installer
            )
        );
        Scene scene = new Scene(login);
        stage.setScene(scene);
        stage.show();
    }

    private void loadMainScene(
        Stage stage,
        User user,
        ArrayList<ModLoader> modLoaders,
        ArrayList<String> minecraftVersions,
        ArrayList<ModOrigin> modOrigins,
        ModFilePageRepository modFilePageRepository,
        UserRepository userRepository,
        ModModuleInstaller installer
    ) {
        try {
            Scene main = new Scene(loadMainPage(
                user,
                user.getModModules(),
                modLoaders,
                minecraftVersions,
                modOrigins,
                modFilePageRepository,
                userRepository,
                installer
            ));
            stage.setScene(main);
            stage.centerOnScreen();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    private Parent loadMainPage(
        User user,
        ArrayList<ModModule> modModules,
        ArrayList<ModLoader> modLoaders,
        ArrayList<String> minecraftVersions,
        ArrayList<ModOrigin> modOrigins,
        ModFilePageRepository modFilePageRepository,
        UserRepository userRepository,
        ModModuleInstaller installer
    ) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("gui/pages/Main.fxml"));
        loader.setControllerFactory(aClass -> new Main(
            user,
            observable(modModules),
            observable(modLoaders),
            observable(minecraftVersions),
            observable(modOrigins),
            modFilePageRepository,
            userRepository,
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