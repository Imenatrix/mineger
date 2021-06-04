package info.oo.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import info.oo.dao.interfaces.IModLoaderDAO;
import info.oo.database.ConnectionFactory;
import info.oo.entities.ModLoader;

public class ModLoaderDAO implements IModLoaderDAO {
    
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
