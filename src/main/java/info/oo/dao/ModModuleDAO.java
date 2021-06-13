package info.oo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import info.oo.dao.interfaces.IModFileDAO;
import info.oo.dao.interfaces.IModLoaderDAO;
import info.oo.dao.interfaces.IModModuleDAO;
import info.oo.database.ConnectionFactory;
import info.oo.entities.ModFile;
import info.oo.entities.ModModule;
import info.oo.entities.User;

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

    public ModModule insert(ModModule modModule, User user) {

        String query = "insert into mod_module(name, user_id, mod_loader_id, minecraft_version) values (?, ?, ?, ?)";

        try (
            Connection conn = ConnectionFactory.getConnection();

            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setString(1, modModule.getName());
            stmt.setInt(2, user.getId());
            stmt.setInt(3, modModule.getModLoader().getId());
            stmt.setString(4, modModule.getMinecraftVersion());

            if (stmt.executeUpdate() == 1) {
                try (
                    ResultSet result = stmt.getGeneratedKeys();
                ) {
                    result.next();
                    int id = result.getInt(1);
                    return new ModModule(
                        id,
                        modModule.getName(),
                        modModule.getMinecraftVersion(),
                        new ArrayList<ModFile>(),
                        modModule.getModLoader()
                    );
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return null;

    }

    public boolean addModFile(ModModule modModule, ModFile modFile) {

        String query = "insert into file_module(mod_module_id, mod_file_id) values (?, ?);";

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, modModule.getId());
            stmt.setInt(2, modFile.getId());
            return stmt.executeUpdate() == 1;
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return false;

    }

    public boolean removeModFile(ModModule modModule, ModFile modFile) {

        String query = "delete from file_module where mod_module_id = ? and mod_file_id = ?;";

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, modModule.getId());
            stmt.setInt(2, modFile.getId());
            return stmt.executeUpdate() == 1;
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return false;

    }

    public boolean delete(int id) {

        String query = "delete from mod_module where id = ?";

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() == 1;
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

}
