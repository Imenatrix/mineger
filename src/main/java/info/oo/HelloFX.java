package info.oo;

import java.io.IOException;

import info.oo.dao.ModDAO;
import info.oo.dao.ModFileDAO;
import info.oo.dao.ModLoaderDAO;
import info.oo.dao.ModModuleDAO;
import info.oo.dao.ModOriginDAO;
import info.oo.dao.UserDAO;
import info.oo.dao.interfaces.IModDAO;
import info.oo.dao.interfaces.IModFileDAO;
import info.oo.dao.interfaces.IModLoaderDAO;
import info.oo.dao.interfaces.IModModuleDAO;
import info.oo.dao.interfaces.IModOriginDAO;
import info.oo.dao.interfaces.IUserDAO;
import info.oo.gui.pages.Main;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class HelloFX extends Application {

    public void start(Stage stage) {
        
        try {
            IModLoaderDAO modLoaderDAO = new ModLoaderDAO();
            IModOriginDAO modOriginDAO = new ModOriginDAO();
            IModDAO modDAO = new ModDAO(modLoaderDAO, modOriginDAO);
            IModFileDAO modFileDAO = new ModFileDAO(modDAO);
            IModModuleDAO modModuleDAO = new ModModuleDAO(modFileDAO, modLoaderDAO);
            IUserDAO userDAO = new UserDAO(modModuleDAO);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("gui/pages/Main.fxml"));
            loader.setControllerFactory(aClass -> new Main(
                modFileDAO,
                FXCollections.observableArrayList(userDAO.getById(1).getModModules())
            ));
            
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}