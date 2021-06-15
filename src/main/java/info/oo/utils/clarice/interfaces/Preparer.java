package info.oo.utils.clarice.interfaces;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface Preparer {
    public void call(PreparedStatement stmt) throws SQLException;
}
