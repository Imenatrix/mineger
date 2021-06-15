package info.oo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import info.oo.dao.interfaces.IMinecraftVersionDAO;
import info.oo.utils.clarice.Clarice;

public class MinecraftVersionDAO implements IMinecraftVersionDAO {
    
    public ArrayList<String> getAll() {
        String query = "select * from minecraft_version;";
        return Clarice.executeQueryOr(
            query,
            stmt -> {},
            result -> resultToVersionArrayList(result),
            new ArrayList<String>()
        );
    }

    private ArrayList<String> resultToVersionArrayList(ResultSet result) throws SQLException {
        ArrayList<String> versions = new ArrayList<String>();
        while (result.next()) {
            versions.add(result.getString("version"));
        }
        return versions;
    }

}
