package info.oo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import info.oo.dao.interfaces.IFileModuleDAO;
import info.oo.entities.FileModule;
import info.oo.entities.ModModule;
import info.oo.utils.clarice.Clarice;

public class FileModuleDAO implements IFileModuleDAO {
    
    public ArrayList<FileModule> getAllByModModules(ArrayList<ModModule> modModules) {

        String wildcards = modModules.stream()
            .map(item -> "?")
            .collect(Collectors.joining(","));
        String query = "select f.* from file_module f join mod_module m where f.mod_module_id = m.id and m.id in (" + wildcards + ")";
        Stream<Integer> ids = modModules.stream().map(item -> item.getId());
        return Clarice.executeQueryOr(
            query,
            stmt -> Clarice.prepareIntegerIterator(stmt, ids.iterator()),
            result -> resultToFileModuleArrayList(result),
            new ArrayList<FileModule>()
        );
    }

    public boolean insert(FileModule fileModule) {
        String query = "insert into file_module(mod_module_id, mod_file_id) values (?, ?);";
        return 1 == Clarice.executeUpdateOr(
            query,
            stmt -> {
                stmt.setInt(1, fileModule.getModModuleId());
                stmt.setInt(2, fileModule.getModFileId());
            },
            (updated, result) -> updated,
            0
        );
    }

    public boolean delete(FileModule fileModule) {
        String query = "delete from file_module where mod_module_id = ? and mod_file_id = ?;";
        return 1 == Clarice.executeUpdateOr(
            query,
            stmt -> {
                stmt.setInt(1, fileModule.getModModuleId());
                stmt.setInt(2, fileModule.getModFileId());
            },
            (updated, result) -> updated,
            0
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
