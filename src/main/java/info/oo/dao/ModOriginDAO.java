package info.oo.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import info.oo.dao.interfaces.IModOriginDAO;
import info.oo.entities.ModOrigin;
import info.oo.utils.Clarice;

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

        String query = "select * from mod_origin where id = ?";
        return Clarice.executeQueryOr(
            query,
            stmt -> stmt.setInt(1, id),
            result -> {
                ModOrigin modOrigin = resultToModOrigin(result);
                cache.add(modOrigin);
                return modOrigin;
            },
            null
        );

    }

    public ArrayList<ModOrigin> getAll() {
        String query = "select * from mod_origin;";
        return Clarice.executeQueryOr(
            query,
            stmt -> {},
            result -> resultToModOriginArrayList(result),
            new ArrayList<ModOrigin>()
        );
    }

    private ArrayList<ModOrigin> resultToModOriginArrayList(ResultSet result) throws SQLException {
        ArrayList<ModOrigin> modOrigins = new ArrayList<ModOrigin>();
        while(result.next()) {
            modOrigins.add(parseModOrigin(result));
        }
        return modOrigins;
    }

    private ModOrigin resultToModOrigin(ResultSet result) throws SQLException {
        result.next();
        return parseModOrigin(result);
    }

    private ModOrigin parseModOrigin(ResultSet result) throws SQLException {
        try {
            return new ModOrigin(
                result.getInt("id"),
                result.getString("name"),
                new URL(result.getString("url")),
                null
            );
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        return null;
    }
    
}
