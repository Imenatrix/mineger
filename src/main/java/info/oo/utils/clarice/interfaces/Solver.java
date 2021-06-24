package info.oo.utils.clarice.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

/* Representa um callback que resolve (serializa) o retorno de um ResultSet.
 */

public interface Solver<T> {
    public T call(ResultSet result) throws SQLException;
}
