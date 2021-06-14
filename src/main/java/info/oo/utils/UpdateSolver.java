package info.oo.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UpdateSolver<T> {

    public T call(int updated, ResultSet generatedKeys) throws SQLException;

}
