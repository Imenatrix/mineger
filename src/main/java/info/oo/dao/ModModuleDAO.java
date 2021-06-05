package info.oo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import info.oo.dao.interfaces.IModModuleDAO;
import info.oo.database.ConnectionFactory;
import info.oo.entities.ModModule;

public class ModModuleDAO implements IModModuleDAO {
    
    public ArrayList<ModModule> getAll() {

        String query = "select * from mod_module;";
        ArrayList<ModModule> modModules = new ArrayList<ModModule>();

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
        ) {
            while(result.next()) {
                ModModule modModule = new ModModule(
                    result.getInt("id"),
                    result.getString("name"),
                    result.getString("minecraft_version"),
                    null,
                    null,
                    null
                );
                modModules.add(modModule);
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return modModules;
    }

}
