package info.oo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import info.oo.dao.interfaces.IFileModuleDAO;
import info.oo.entities.FileModule;
import info.oo.utils.clarice.Clarice;

public class FileModuleDAO implements IFileModuleDAO {
    
    public ArrayList<FileModule> getAllByModModuleId(int id) {
        String query = "select * from file_module where mod_module_id = ?";
        return Clarice.executeQueryOr(
            query,
            stmt -> stmt.setInt(1, id),
            result -> resultToFileModuleArrayList(result),
            new ArrayList<FileModule>()
        );
    }

    private ArrayList<FileModule> resultToFileModuleArrayList(ResultSet result) throws SQLException {
        ArrayList<FileModule> fileModules = new ArrayList<FileModule>();
        while (result.next()) {
            fileModules.add(parseFileModuleFromResult(result));
        }
        return fileModules;
    }

    private FileModule parseFileModuleFromResult(ResultSet result) throws SQLException {
        return new FileModule(
            result.getInt("mod_file_id"),
            result.getInt("mod_module_id")
        );
    }

}
