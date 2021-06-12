package info.oo.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import info.oo.dao.interfaces.IModOriginDAO;
import info.oo.database.ConnectionFactory;
import info.oo.entities.ModOrigin;

public class ModOriginDAO implements IModOriginDAO {

    private ArrayList<ModOrigin> cache;

    public ModOriginDAO() {
        cache = new ArrayList<ModOrigin>();
    }

    public ModOrigin getById(int id) {
        
        Optional<ModOrigin> optionalModOrigin = cache.stream().filter(item -> item.getId() == id).findFirst();

        if (optionalModOrigin.isPresent()) {
            return optionalModOrigin.get();
        }

        ModOrigin modOrigin = null;
        String query = "select * from mod_origin where id = ?";

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            stmt.setInt(1, id);

            try (ResultSet result = stmt.executeQuery()) {
                result.next();
                modOrigin = new ModOrigin(
                    result.getInt("id"),
                    result.getString("name"),
                    new URL(result.getString("url")),
                    null
                );
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        } 

        cache.add(modOrigin);
        return modOrigin;

    }

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
