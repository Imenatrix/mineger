package info.oo.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import info.oo.dao.interfaces.IModFileDAO;
import info.oo.entities.Mod;
import info.oo.entities.ModFile;
import info.oo.utils.clarice.Clarice;

public class ModFileDAO implements IModFileDAO {

    private ArrayList<ModFile> cache;

    public ModFileDAO() {
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

    public int getTotalPages(int limit, Integer modLoaderId, Integer modOriginId, String minecraftVersion, String search) {
        String query =
            "select ceil(count(*) / ?) as 'totalPages' from " +
                "mod_file f join " +
                "`mod` m " +
            "where " +
                "m.id = f.mod_id " +
                (modLoaderId != null ? "and m.mod_loader_id = ? " : "") +
                (modOriginId != null ? "and m.mod_origin_id = ? " : "") +
                (minecraftVersion != null ? "and f.minecraft_version = ? " : "") +
                (search != null ? "and match(m.name, m.summary) against(? in boolean mode) " : "") +
                ";";

        return Clarice.executeQueryOr(
            query,
            stmt -> {
                int counter = 1;
                stmt.setInt(counter++, limit);
                if (modLoaderId != null) {
                    stmt.setInt(counter++, modLoaderId);
                };
                if (modOriginId != null) {
                    stmt.setInt(counter++, modOriginId);
                };
                if (minecraftVersion != null) {
                    stmt.setString(counter++, minecraftVersion);
                };
                if (search != null) {
                    stmt.setString(counter++, search + "*");
                };
            },
            result -> {
                result.next();
                return result.getInt("totalPages");
            },
            0
        );
    }

    public ArrayList<ModFile> getPaginated(int limit, int page, Integer modLoaderId, Integer modOriginId, String minecraftVersion, String search) {
        String query =
            "select f.* from " +
                "mod_file f join " +
                "`mod` m " +
            "where " +
                "m.id = f.mod_id " +
                (modLoaderId != null ? "and m.mod_loader_id = ? " : "") +
                (modOriginId != null ? "and m.mod_origin_id = ? " : "") +
                (minecraftVersion != null ? "and f.minecraft_version = ? " : "") +
                (search != null ? "and match(m.name, m.summary) against(? in boolean mode) " : "") +
            "order by f.id " +
            "limit ? " +
            "offset ?;";

        return Clarice.executeQueryOr(
            query,
            stmt -> {
                int counter = 1;
                if (modLoaderId != null) {
                    stmt.setInt(counter++, modLoaderId);
                };
                if (modOriginId != null) {
                    stmt.setInt(counter++, modOriginId);
                };
                if (minecraftVersion != null) {
                    stmt.setString(counter++, minecraftVersion);
                };
                if (search != null) {
                    stmt.setString(counter++, search + "*");
                };
                stmt.setInt(counter++, limit);
                stmt.setInt(counter++, page * limit);
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
                new Mod(result.getInt("mod_id"))
            );
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        return null;
    }

}
