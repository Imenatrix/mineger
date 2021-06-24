package info.oo.utils.clarice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            DBConfig.getURL(),
            DBConfig.getUser(),
            DBConfig.getPassword()
        );
    }

}
