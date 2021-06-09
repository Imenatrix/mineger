package info.oo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import info.oo.dao.interfaces.IModFileDAO;
import info.oo.dao.interfaces.IModLoaderDAO;
import info.oo.dao.interfaces.IModModuleDAO;
import info.oo.database.ConnectionFactory;
import info.oo.entities.ModModule;

public class ModModuleDAO implements IModModuleDAO {

    private IModFileDAO modFileDAO;
    private IModLoaderDAO modLoaderDAO;

    public ModModuleDAO(IModFileDAO modFileDAO, IModLoaderDAO modLoaderDAO) {
        this.modFileDAO = modFileDAO;
        this.modLoaderDAO = modLoaderDAO;
    }
    
    public ArrayList<ModModule> getAll() {

        String query = "select * from mod_module;";
        ArrayList<ModModule> modModules = new ArrayList<ModModule>();

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
        ) {
            while(result.next()) {
                ModModule modModule = new ModModule(
                    result.getInt("id"),
                    result.getString("name"),
                    result.getString("minecraft_version"),
                    modFileDAO.getAllByModModuleId(result.getInt("id")),
                    modLoaderDAO.getById(result.getInt("mod_loader_id"))
                );
                modModules.add(modModule);
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return modModules;
    }

    public ArrayList<ModModule> getAllByUserId(int id) {
        
        String query = "select * from mod_module where user_id = ?;";
        ArrayList<ModModule> modModules = new ArrayList<ModModule>();

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, id);
            try (        
                ResultSet result = stmt.executeQuery();
            ) {
                while(result.next()) {
                    ModModule modModule = new ModModule(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("minecraft_version"),
                        modFileDAO.getAllByModModuleId(result.getInt("id")),
                        modLoaderDAO.getById(result.getInt("mod_loader_id"))
                    );
                    modModules.add(modModule);
            }
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return modModules;
    }

}
