package info.oo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import info.oo.dao.interfaces.IMinecraftVersionDAO;
import info.oo.database.ConnectionFactory;

public class MinecraftVersionDAO implements IMinecraftVersionDAO {
    
    public ArrayList<String> getAll() {

        String query = "select * from minecraft_version;";

        ArrayList<String> versions = new ArrayList<String>();

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery()
        ) {
            while(result.next()) {
                versions.add(result.getString("version"));
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return versions;
    }

}
