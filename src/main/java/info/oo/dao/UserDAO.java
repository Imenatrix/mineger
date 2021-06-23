package info.oo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import info.oo.dao.interfaces.IUserDAO;
import info.oo.entities.User;
import info.oo.utils.clarice.Clarice;

public class UserDAO implements IUserDAO {

    private ArrayList<User> cache;

    public UserDAO() {
        this.cache = new ArrayList<User>();
    }

    public User getById(int id) {
        
        Optional<User> optinalUser = cache.stream().filter(item -> item.getId() == id).findFirst();

        if (optinalUser.isPresent()) {
            return optinalUser.get();
        }

        String query = "select * from user where id = ?";
        return Clarice.executeQueryOr(
            query,
            stmt -> stmt.setInt(1, id),
            result -> {
                User user = resultToUser(result);
                cache.add(user);
                return user;
            },
            null
        );
    }
    
    public ArrayList<User> getAll() {
        String query = "select * from user;";
        return Clarice.executeQueryOr(
            query,
            stmt -> {},
            result -> resultToUserArrayList(result),
            new ArrayList<User>()
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
            result -> resultToLoginUser(result),
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

    private ArrayList<User> resultToUserArrayList(ResultSet result) throws SQLException {
        ArrayList<User> users = new ArrayList<User>();
        while(result.next()) {
            User user = parseUserFromResult(result);
            users.add(user);
        }
        return users;
    }

    private User resultToUser(ResultSet result) throws SQLException {
        result.next();
        return parseUserFromResult(result);
    }

    private User parseUserFromResult(ResultSet result) throws SQLException {
        User user = new User(
            result.getInt("id"),
            result.getString("name"),
            result.getString("login"),
            result.getString("password")
        );
        return user;
    }

    private User resultToLoginUser(ResultSet result) throws SQLException {
        result.next();
        return parseLoginUserFromResult(result);
    }

    private User parseLoginUserFromResult(ResultSet result) throws SQLException {
        return new User(
            result.getInt("id"),
            result.getString("name")
        );
    }

}
