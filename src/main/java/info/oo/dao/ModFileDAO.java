package info.oo.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import info.oo.dao.interfaces.IModDAO;
import info.oo.dao.interfaces.IModFileDAO;
import info.oo.database.ConnectionFactory;
import info.oo.entities.ModFile;

public class ModFileDAO implements IModFileDAO {

    private IModDAO modDAO;

    public ModFileDAO(IModDAO modDAO) {
        this.modDAO = modDAO;
    }
    
    public ArrayList<ModFile> getAll() {
        String query = "select * from mod_file;";
        ArrayList<ModFile> modFiles = new ArrayList<ModFile>();

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
        ) {
            while(result.next()) {
                ModFile modFile = new ModFile(
                    result.getInt("id"),
                    result.getString("file_name"),
                    new URL(result.getString("url")),
                    result.getString("minecraft_version"),
                    modDAO.getById(result.getInt("mod_id"))
                );
                modFiles.add(modFile);
            }
        }
        catch (MalformedURLException e) {
            System.out.println(e);
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return modFiles;
    }

}
