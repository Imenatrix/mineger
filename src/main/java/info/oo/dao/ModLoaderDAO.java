package info.oo.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import info.oo.dao.interfaces.IModLoaderDAO;
import info.oo.entities.ModLoader;
import info.oo.utils.clarice.Clarice;

public class ModLoaderDAO implements IModLoaderDAO {

    private ArrayList<ModLoader> cache;

    public ModLoaderDAO() {
        cache = new ArrayList<ModLoader>();
    }

    public ModLoader getById(int id) {
        
        Optional<ModLoader> optionalModLoader = cache.stream().filter(item -> item.getId() == id).findFirst();

        if (optionalModLoader.isPresent()) {
            return optionalModLoader.get();
        }

        String query = "select * from mod_loader where id = ?";
        return Clarice.executeQueryOr(
            query,
            stmt -> stmt.setInt(1, id),
            result -> {
                ModLoader modLoader = resultToModLoader(result);
                cache.add(modLoader);
                return modLoader;
            },
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
            System.out.println(e);
        }
        return null;
    }

}
