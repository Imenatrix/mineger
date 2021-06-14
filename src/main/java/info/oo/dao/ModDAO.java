package info.oo.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import info.oo.dao.interfaces.IModDAO;
import info.oo.dao.interfaces.IModLoaderDAO;
import info.oo.dao.interfaces.IModOriginDAO;
import info.oo.entities.Mod;
import info.oo.utils.Clarice;

public class ModDAO implements IModDAO {

    private IModLoaderDAO modLoaderDAO;
    private IModOriginDAO modOriginDAO;
    private ArrayList<Mod> cache;

    public ModDAO(IModLoaderDAO modLoaderDAO, IModOriginDAO modOriginDAO) {
        this.modLoaderDAO = modLoaderDAO;
        this.modOriginDAO = modOriginDAO;
        this.cache = new ArrayList<Mod>();
    }

    public Mod getById(int id) {
        
        Optional<Mod> optionalMod = cache.stream().filter(item -> item.getId() == id).findFirst();

        if (optionalMod.isPresent()) {
            return optionalMod.get();
        }

        String query = "select * from `mod` where id = ?";
        return Clarice.executeQueryOr(
            query,
            stmt -> {
                stmt.setInt(1, id);
            },
            result -> {
                result.next();
                Mod mod = resultToMod(result);
                cache.add(mod);
                return mod;
            },
            null
        );
    }
    
    public ArrayList<Mod> getAll() {
        String query = "select * from `mod`;";
        return Clarice.executeQueryOr(
            query,
            stmt -> {},
            result -> resultToModArrayList(result),
            new ArrayList<Mod>()
        );
    }

    private ArrayList<Mod> resultToModArrayList(ResultSet result) throws SQLException{
        ArrayList<Mod> mods = new ArrayList<Mod>();
        while(result.next()) {
            mods.add(resultToMod(result));
        }
        return mods;
    }

    private Mod resultToMod(ResultSet result) throws SQLException {
        try {
            return new Mod(
                result.getInt("id"),
                result.getString("name"),
                new URL(result.getString("url")),
                result.getString("summary"),
                modLoaderDAO.getById(result.getInt("mod_loader_id")),
                modOriginDAO.getById(result.getInt("mod_origin_id"))
            );
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        return null;
    }

}
