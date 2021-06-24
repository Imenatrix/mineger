package info.oo.utils.clarice.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

/* Representa um callback que resolve (serializa) o retorno de um ResultSet
 * de uma query de update (insert, update, delete), difere de Solver pois
 * inclui o numero de colunas afetadas pela query.
 */
public interface UpdateSolver<T> {
    public T call(int updated, ResultSet generatedKeys) throws SQLException;
}
