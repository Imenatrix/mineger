package info.oo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import info.oo.dao.interfaces.IUserDAO;
import info.oo.entities.User;
import info.oo.utils.clarice.Clarice;

public class UserDAO implements IUserDAO {

    public User getById(int id) {
        String query = "select * from user where id = ?";
        return Clarice.executeQueryOr(
            query,
            stmt -> stmt.setInt(1, id),
            result -> resultToUser(result),
            null
        );
    }

    public User getByLoginAndPassword(String login, String password) {
        String query = "select id, name from user where login = ? and password = ?";
        return Clarice.executeQueryOr(
            query,
            stmt -> {
                stmt.setString(1, login);
                stmt.setString(2, password);
            },
            result -> resultToUser(result),
            null
        );
    }

    public User insert(User user) {
        String query = "insert into user(name, login, password) values(?, ?, ?);";
        return Clarice.executeUpdateOr(
            query,
            stmt -> {
                stmt.setString(1, user.getName());
                stmt.setString(2, user.getLogin());
                stmt.setString(3, user.getPassword());
            },
            (updated, result) -> (
                updated == 1
                    ? indexUser(user, result)
                    : null
            ),
            null
        );
    }

    private User indexUser(User user, ResultSet result) throws SQLException {
        result.next();
        int id = result.getInt(1);
        return new User(
            id,
            user.getName()
        );
    }

    private User resultToUser(ResultSet result) throws SQLException {
        result.next();
        return parseUserFromResult(result);
    }

    private User parseUserFromResult(ResultSet result) throws SQLException {
        return new User(
            result.getInt("id"),
            result.getString("name")
        );
    }

}
