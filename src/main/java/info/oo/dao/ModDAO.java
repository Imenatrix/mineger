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
import info.oo.dao.interfaces.IModLoaderDAO;
import info.oo.dao.interfaces.IModOriginDAO;
import info.oo.database.ConnectionFactory;
import info.oo.entities.Mod;

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

        Mod mod = null;
        String query = "select * from `mod` where id = ?";

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, id);

            try (ResultSet result = stmt.executeQuery()) {
                result.next();
                mod = new Mod(
                    result.getInt("id"),
                    result.getString("name"),
                    new URL(result.getString("url")),
                    result.getString("summary"),
                    modLoaderDAO.getById(result.getInt("mod_loader_id")),
                    modOriginDAO.getById(result.getInt("mod_origin_id"))
                );
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        } 

        cache.add(mod);
        return mod;

    }
    
    public ArrayList<Mod> getAll() {
        String query = "select * from `mod`;";
        ArrayList<Mod> mods = new ArrayList<Mod>();

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
        ) {
            while(result.next()) {
                Mod mod = new Mod(
                    result.getInt("id"),
                    result.getString("name"),
                    new URL(result.getString("url")),
                    result.getString("summary"),
                    modLoaderDAO.getById(result.getInt("mod_loader_id")),
                    modOriginDAO.getById(result.getInt("mod_origin_id"))
                );
                mods.add(mod);
            }
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return mods;
    }

}
