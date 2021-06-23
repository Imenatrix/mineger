package info.oo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import info.oo.dao.interfaces.IModModuleDAO;
import info.oo.entities.ModFile;
import info.oo.entities.ModModule;
import info.oo.entities.User;
import info.oo.utils.clarice.Clarice;

public class ModModuleDAO implements IModModuleDAO {
    
    public ArrayList<ModModule> getAll() {
        String query = "select * from mod_module;";
        return Clarice.executeQueryOr(
            query,
            stmt -> {},
            result -> resultToModModuleArrayList(result),
            new ArrayList<ModModule>()
        );
    }

    public ArrayList<ModModule> getAllByUserId(int id) {
        String query = "select * from mod_module where user_id = ?;";
        return Clarice.executeQueryOr(
            query,
            stmt -> stmt.setInt(1, id),
            result -> resultToModModuleArrayList(result),
            new ArrayList<ModModule>()
        );
    }

    public ModModule insert(ModModule modModule, User user) {
        String query = "insert into mod_module(name, user_id, mod_loader_id, minecraft_version) values (?, ?, ?, ?)";
        return Clarice.executeUpdateOr(
            query,
            stmt -> {
                stmt.setString(1, modModule.getName());
                stmt.setInt(2, user.getId());
                stmt.setInt(3, modModule.getModLoader().getId());
                stmt.setString(4, modModule.getMinecraftVersion());
            },
            (updated, result) -> (
                updated == 1
                    ? indexModModule(modModule, result)
                    : null
            ),
            null
        );
    }

    public boolean addModFile(ModModule modModule, ModFile modFile) {
        String query = "insert into file_module(mod_module_id, mod_file_id) values (?, ?);";
        return 1 == Clarice.executeUpdateOr(
            query,
            stmt -> {
                stmt.setInt(1, modModule.getId());
                stmt.setInt(2, modFile.getId());
            },
            (updated, result) -> updated,
            0
        );
    }

    public boolean removeModFile(ModModule modModule, ModFile modFile) {
        String query = "delete from file_module where mod_module_id = ? and mod_file_id = ?;";
        return 1 == Clarice.executeUpdateOr(
            query,
            stmt -> {
                stmt.setInt(1, modModule.getId());
                stmt.setInt(2, modFile.getId());
            },
            (updated, result) -> updated,
            0
        );
    }

    private boolean removeFileModuleRelationships(ModModule modModule) {
        String query = "delete from file_module where mod_module_id = ?;";
        return 1 <= Clarice.executeUpdateOr(
            query,
            stmt -> {
                stmt.setInt(1, modModule.getId());
            },
            (updated, result) -> updated,
            0
        );
    }

    public boolean delete(ModModule modModule) {

        removeFileModuleRelationships(modModule);

        String query = "delete from mod_module where id = ?";
        return 1 == Clarice.executeUpdateOr(
            query,
            stmt -> {
                stmt.setInt(1, modModule.getId());
            },
            (updated, result) -> updated,
            0
        );
    }

    private ArrayList<ModModule> resultToModModuleArrayList(ResultSet result) throws SQLException {
        ArrayList<ModModule> modModules = new ArrayList<ModModule>();
        while (result.next()) {
            modModules.add(parseModModuleFromResult(result));
        }
        return modModules;
    }

    private ModModule indexModModule(ModModule modModule, ResultSet result) throws SQLException {
        result.next();
        int id = result.getInt(1);
        return new ModModule(
            id,
            modModule.getName(),
            modModule.getMinecraftVersion(),
            modModule.getModLoader()
        );
    }

    private ModModule parseModModuleFromResult(ResultSet result) throws SQLException {
        return new ModModule(
            result.getInt("id"),
            result.getString("name"),
            result.getString("minecraft_version")
        );
    }

}
