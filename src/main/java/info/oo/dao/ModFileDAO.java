package info.oo.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import info.oo.dao.interfaces.IModDAO;
import info.oo.dao.interfaces.IModFileDAO;
import info.oo.entities.ModFile;
import info.oo.utils.clarice.Clarice;

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

        String query = "select * from mod_file where id = ?";
        return Clarice.executeQueryOr(
            query,
            stmt -> stmt.setInt(1, id),
            result -> {
                ModFile modFile = resultToModFile(result);
                cache.add(modFile);
                return modFile;
            },
            null
        );
    }
    
    public ArrayList<ModFile> getAll() {
        String query = "select * from mod_file;";
        return Clarice.executeQueryOr(
            query,
            stmt -> {},
            result -> resultToModFileArrayList(result),
            new ArrayList<ModFile>()
        );
    }

    public ArrayList<ModFile> getAllByModModuleId(int id) {
        String query = "select * from file_module join mod_file where mod_module_id = ? and id = mod_file_id;";
        return Clarice.executeQueryOr(
            query,
            stmt -> stmt.setInt(1, id),
            result -> resultToModFileArrayList(result),
            new ArrayList<ModFile>()
        );
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

        return Clarice.executeQueryOr(
            query,
            stmt -> {
                stmt.setInt(1, limit);
                stmt.setString(2, minecraftVersion);
                stmt.setInt(3, modLoaderId);
            },
            result -> {
                result.next();
                return result.getInt("totalPages");
            },
        0);
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

        return Clarice.executeQueryOr(
            query,
            stmt -> {
                stmt.setString(1, minecraftVersion);
                stmt.setInt(2, modLoaderId);
                stmt.setInt(3, limit);
                stmt.setInt(4, page * limit);
            },
            result -> resultToModFileArrayList(result),
            new ArrayList<ModFile>()
        );
    }

    private ArrayList<ModFile> resultToModFileArrayList(ResultSet result) throws SQLException {
        ArrayList<ModFile> modFiles = new ArrayList<ModFile>();
        while (result.next()) {
            modFiles.add(parseModFileFromResult(result));
        }
        return modFiles;
    }

    private ModFile resultToModFile(ResultSet result) throws SQLException {
        result.next();
        return parseModFileFromResult(result);
    }

    private ModFile parseModFileFromResult(ResultSet result) throws SQLException {
        try {
            return new ModFile(
                result.getInt("id"),
                result.getString("file_name"),
                new URL(result.getString("url")),
                result.getString("minecraft_version"),
                modDAO.getById(result.getInt("mod_id"))
            );
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        return null;
    }

}
