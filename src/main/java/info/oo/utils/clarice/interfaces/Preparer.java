package info.oo.utils.clarice.interfaces;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/* Representa um callback que insere os valores correspondentes aos wildcards
 * De uma query no PreparedStatement.
 */

public interface Preparer {
    public void call(PreparedStatement stmt) throws SQLException;
}
