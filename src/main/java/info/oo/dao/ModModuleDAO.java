package info.oo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import info.oo.dao.interfaces.IModFileDAO;
import info.oo.dao.interfaces.IModLoaderDAO;
import info.oo.dao.interfaces.IModModuleDAO;
import info.oo.database.ConnectionFactory;
import info.oo.entities.ModFile;
import info.oo.entities.ModModule;
import info.oo.entities.User;
import info.oo.utils.Preparer;
import info.oo.utils.Solver;
import info.oo.utils.UpdateSolver;

public class ModModuleDAO implements IModModuleDAO {

    private IModFileDAO modFileDAO;
    private IModLoaderDAO modLoaderDAO;

    public ModModuleDAO(IModFileDAO modFileDAO, IModLoaderDAO modLoaderDAO) {
        this.modFileDAO = modFileDAO;
        this.modLoaderDAO = modLoaderDAO;
    }
    
    public ArrayList<ModModule> getAll() {
        String query = "select * from mod_module;";
        return executeQueryOr(
            query,
            stmt -> {},
            result -> resultToModModuleArrayList(result),
            new ArrayList<ModModule>()
        );
    }

    public ArrayList<ModModule> getAllByUserId(int id) {
        String query = "select * from mod_module where user_id = ?;";
        return executeQueryOr(
            query,
            stmt -> stmt.setInt(1, id),
            result -> resultToModModuleArrayList(result),
            new ArrayList<ModModule>()
        );
    }

    public ModModule insert(ModModule modModule, User user) {
        String query = "insert into mod_module(name, user_id, mod_loader_id, minecraft_version) values (?, ?, ?, ?)";
        return executeUpdateOr(
            query,
            stmt -> {
                stmt.setString(1, modModule.getName());
                stmt.setInt(2, user.getId());
                stmt.setInt(3, modModule.getModLoader().getId());
                stmt.setString(4, modModule.getMinecraftVersion());
            },
            (updated, result) -> {
                return updated == 1
                    ? resultToNewModModule(modModule, result)
                    : null;
            },
            (ModModule) null
        );
    }

    public boolean addModFile(ModModule modModule, ModFile modFile) {
        String query = "insert into file_module(mod_module_id, mod_file_id) values (?, ?);";
        return 1 == executeUpdateOr(
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
        return 1 == executeUpdateOr(
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
        return 1 <= executeUpdateOr(
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
        return 1 == executeUpdateOr(
            query,
            stmt -> {
                stmt.setInt(1, modModule.getId());
            },
            (updated, result) -> updated,
            0
        );
    }

    private <T> T executeQueryOr(String query, Preparer preparer, Solver<T> solver, T or) {
        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            preparer.call(stmt);
            return solveResult(stmt, solver);
        }
        catch (SQLException e) {
            System.out.println(e);
            return or;
        }
    }

    private <T> T executeUpdateOr(String query, Preparer preparer, UpdateSolver<T> solver, T or) {
        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ) {
            preparer.call(stmt);
            int updated = stmt.executeUpdate();
            return solveUpdateResult(updated, stmt, solver);
        }
        catch (SQLException e) {
            System.out.println(e);
            return or;
        }
    }

    private <T> T solveResult(PreparedStatement stmt, Solver<T> solver) throws SQLException {
        try (        
            ResultSet result = stmt.executeQuery();
        ) {
            return solver.call(result);
        }
    }

    private <T> T solveUpdateResult(int updated, PreparedStatement stmt, UpdateSolver<T> solver) throws SQLException {
        try (        
            ResultSet result = stmt.getGeneratedKeys();
        ) {
            return solver.call(updated, result);
        }
    }

    private ArrayList<ModModule> resultToModModuleArrayList(ResultSet result) throws SQLException {
        ArrayList<ModModule> modModules = new ArrayList<ModModule>();
        while (result.next()) {
            modModules.add(resultToModModule(result));
        }
        return modModules;
    }

    private ModModule resultToModModule(ResultSet result) throws SQLException {
        return new ModModule(
            result.getInt("id"),
            result.getString("name"),
            result.getString("minecraft_version"),
            modFileDAO.getAllByModModuleId(result.getInt("id")),
            modLoaderDAO.getById(result.getInt("mod_loader_id"))
        );
    }

    private ModModule resultToNewModModule(ModModule modModule, ResultSet result) throws SQLException {
        result.next();
        int id = result.getInt(1);
        return new ModModule(
            id,
            modModule.getName(),
            modModule.getMinecraftVersion(),
            new ArrayList<ModFile>(),
            modModule.getModLoader()
        );
    }

}
