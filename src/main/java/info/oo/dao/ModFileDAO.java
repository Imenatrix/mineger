package info.oo.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import info.oo.dao.interfaces.IModDAO;
import info.oo.dao.interfaces.IModFileDAO;
import info.oo.database.ConnectionFactory;
import info.oo.entities.ModFile;

public class ModFileDAO implements IModFileDAO {

    private IModDAO modDAO;
    private ArrayList<ModFile> cache;

    public ModFileDAO(IModDAO modDAO) {
        this.modDAO = modDAO;
        this.cache = new ArrayList<ModFile>();
    }

    public ModFile getById(int id) {
        
        Optional<ModFile> optionalModFile = cache.stream().filter(item -> item.getId() == id).findFirst();

        if (optionalModFile.isPresent()) {
            return optionalModFile.get();
        }

        ModFile modFile = null;
        String query = "select * from mod_file where id = ?";

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, id);

            try (ResultSet result = stmt.executeQuery()) {
                result.next();
                modFile = new ModFile(
                    result.getInt("id"),
                    result.getString("file_name"),
                    new URL(result.getString("url")),
                    result.getString("minecraft_version"),
                    modDAO.getById(result.getInt("mod_id"))
                );
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        } 

        cache.add(modFile);
        return modFile;

    }
    
    public ArrayList<ModFile> getAll() {
        String query = "select * from mod_file;";
        ArrayList<ModFile> modFiles = new ArrayList<ModFile>();

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
        ) {
            while(result.next()) {
                ModFile modFile = new ModFile(
                    result.getInt("id"),
                    result.getString("file_name"),
                    new URL(result.getString("url")),
                    result.getString("minecraft_version"),
                    modDAO.getById(result.getInt("mod_id"))
                );
                modFiles.add(modFile);
            }
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return modFiles;
    }

    public ArrayList<ModFile> getAllByModModuleId(int id) {

        String query = "select * from file_module join mod_file where mod_module_id = ? and id = mod_file_id;";
        ArrayList<ModFile> modFiles = new ArrayList<ModFile>();

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, id);
            try (
                ResultSet result = stmt.executeQuery();
            ) {
                while(result.next()) {
                    ModFile modFile = new ModFile(
                        result.getInt("id"),
                        result.getString("file_name"),
                        new URL(result.getString("url")),
                        result.getString("minecraft_version"),
                        modDAO.getById(result.getInt("mod_id"))
                    );
                    modFiles.add(modFile);
                }
            }
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return modFiles;

    }

    public int getTotalPagesByModLoaderIdAndMinecraftVersion(int limit, int modLoaderId, String minecraftVersion) {

        String query =
            "select ceil(count(*) / ?) as 'totalPages' from " +
                "mod_file f join " +
                "`mod` m " +
            "where " +
                "m.id = f.mod_id " +
                "and f.minecraft_version = ? " +
                "and m.mod_loader_id = ?;";

        int totalPages = 0;

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, limit);
            stmt.setString(2, minecraftVersion);
            stmt.setInt(3, modLoaderId);

            try (
                ResultSet result = stmt.executeQuery();
            ) {
                result.next();
                totalPages = result.getInt("totalPages");
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return totalPages;
    }

    public ArrayList<ModFile> getPaginatedByModLoaderIdAndMinecraftVersion(int limit, int page, int modLoaderId, String minecraftVersion) {

        String query =
            "select f.* from " +
                "mod_file f join " +
                "`mod` m " +
            "where " +
                "m.id = f.mod_id " +
                "and f.minecraft_version = ? " +
                "and m.mod_loader_id = ? " +
            "order by f.id " +
            "limit ? " +
            "offset ?;";
        
        ArrayList<ModFile> modFiles = new ArrayList<ModFile>();
        
        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setString(1, minecraftVersion);
            stmt.setInt(2, modLoaderId);
            stmt.setInt(3, limit);
            stmt.setInt(4, page);

            try (
                ResultSet result = stmt.executeQuery();
            ) {
                while (result.next()) {
                    ModFile modFile = new ModFile(
                        result.getInt("id"),
                        result.getString("file_name"),
                        new URL(result.getString("url")),
                        result.getString("minecraft_version"),
                        modDAO.getById(result.getInt("mod_id"))
                    );
                    modFiles.add(modFile);
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }

        return modFiles;
    }

}
