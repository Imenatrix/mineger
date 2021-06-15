package info.oo.utils.clarice.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Solver<T> {

    public T call(ResultSet result) throws SQLException;

}
