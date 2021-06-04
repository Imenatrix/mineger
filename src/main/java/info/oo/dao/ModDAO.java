package info.oo.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import info.oo.dao.interfaces.IModDAO;
import info.oo.database.ConnectionFactory;
import info.oo.entities.Mod;

public class ModDAO implements IModDAO {
    
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
                    null,
                    null
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
