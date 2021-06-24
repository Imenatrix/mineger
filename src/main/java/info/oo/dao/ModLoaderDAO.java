package info.oo.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import info.oo.dao.interfaces.IModLoaderDAO;
import info.oo.entities.Mod;
import info.oo.entities.ModLoader;
import info.oo.entities.ModModule;
import info.oo.utils.clarice.Clarice;

public class ModLoaderDAO implements IModLoaderDAO {

    public ModLoader getById(int id) {
        String query = "select * from mod_loader where id = ?";
        return Clarice.executeQueryOr(
            query,
            stmt -> stmt.setInt(1, id),
            result -> resultToModLoader(result),
            null
        );
    }
    
    public ArrayList<ModLoader> getAll() {
        String query = "select * from mod_loader;";
        return Clarice.executeQueryOr(
            query,
            stmt -> {},
            result -> resultToModLoaderArrayList(result),
            new ArrayList<ModLoader>()
        );
    }

    public ArrayList<ModLoader> getAllByModModules(ArrayList<ModModule> modModules) {
        String wildcards = modModules.stream()
            .map(item -> item.getModLoader().getId())
            .map(item -> "?")
            .collect(Collectors.joining(","));
        Stream<Integer> ids = modModules.stream().map(item -> item.getModLoader().getId());
        return getAllByIds(ids, wildcards);
    }

    public ArrayList<ModLoader> getAllByMods(ArrayList<Mod> mods) {
        String wildcards = mods.stream()
            .map(item -> item.getModLoader().getId())
            .map(item -> "?")
            .collect(Collectors.joining(","));
        Stream<Integer> ids = mods.stream().map(item -> item.getModLoader().getId());
        return getAllByIds(ids, wildcards);
    }

    private ArrayList<ModLoader> getAllByIds(Stream<Integer> ids, String wildcards) {
        String query = "select * from mod_loader where id in (" + wildcards + ")";
        return Clarice.executeQueryOr(
            query,
            stmt -> Clarice.prepareIntegerIterator(stmt, ids.iterator()),
            result -> resultToModLoaderArrayList(result),
            new ArrayList<ModLoader>()
        );
    }

    private ArrayList<ModLoader> resultToModLoaderArrayList(ResultSet result) throws SQLException {
        ArrayList<ModLoader> modLoaders = new ArrayList<ModLoader>();
        while(result.next()) {
            modLoaders.add(parseModLoaderFromResult(result));
        }
        return modLoaders;
    }

    private ModLoader resultToModLoader(ResultSet result) throws SQLException{
        result.next();
        return parseModLoaderFromResult(result);
    }

    private ModLoader parseModLoaderFromResult(ResultSet result) throws SQLException{
        try {
            return new ModLoader(
                result.getInt("id"),
                result.getString("name"),
                new URL(result.getString("url"))
            );
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
