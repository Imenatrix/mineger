package info.oo.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import info.oo.dao.interfaces.IModOriginDAO;
import info.oo.database.ConnectionFactory;
import info.oo.entities.ModOrigin;

public class ModOriginDAO implements IModOriginDAO {

    public ArrayList<ModOrigin> getAll() {

        String query = "select * from mod_origin;";
        ArrayList<ModOrigin> modOrigins = new ArrayList<ModOrigin>();

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
        ) {
            while(result.next()) {
                ModOrigin modOrigin = new ModOrigin(
                    result.getInt("id"),
                    result.getString("name"),
                    new URL(result.getString("url")),
                    null
                );
                modOrigins.add(modOrigin);
            }
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return modOrigins;
    }
    
}
