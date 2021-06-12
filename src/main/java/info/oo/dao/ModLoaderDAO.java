package info.oo.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import info.oo.dao.interfaces.IModLoaderDAO;
import info.oo.database.ConnectionFactory;
import info.oo.entities.ModLoader;

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

        ModLoader modLoader = null;
        String query = "select * from mod_loader where id = ?";

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, id);

            try (ResultSet result = stmt.executeQuery()) {
                result.next();
                modLoader = new ModLoader(
                    result.getInt("id"),
                    result.getString("name"),
                    new URL(result.getString("url"))
                );
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        } 

        cache.add(modLoader);
        return modLoader;

    }
    
    public ArrayList<ModLoader> getAll() {
        String query = "select * from mod_loader;";
        ArrayList<ModLoader> modLoaders = new ArrayList<ModLoader>();

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
        ) {
            while(result.next()) {
                ModLoader modLoader = new ModLoader(
                    result.getInt("id"),
                    result.getString("name"),
                    new URL(result.getString("url"))
                );
                modLoaders.add(modLoader);
            }
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return modLoaders;
    }

}
