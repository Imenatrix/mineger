package info.oo.utils.clarice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import info.oo.database.ConnectionFactory;
import info.oo.utils.clarice.interfaces.Preparer;
import info.oo.utils.clarice.interfaces.Solver;
import info.oo.utils.clarice.interfaces.UpdateSolver;

public class Clarice {
    public static <T> T executeQueryOr(String query, Preparer preparer, Solver<T> solver, T or) {
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

    public static <T> T executeUpdateOr(String query, Preparer preparer, UpdateSolver<T> solver, T or) {
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

    public static <T> T solveResult(PreparedStatement stmt, Solver<T> solver) throws SQLException {
        try (        
            ResultSet result = stmt.executeQuery();
        ) {
            return solver.call(result);
        }
    }

    public static <T> T solveUpdateResult(int updated, PreparedStatement stmt, UpdateSolver<T> solver) throws SQLException {
        try (        
            ResultSet result = stmt.getGeneratedKeys();
        ) {
            return solver.call(updated, result);
        }
    }
}
