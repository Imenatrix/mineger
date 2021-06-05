package info.oo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import info.oo.dao.interfaces.IUserDAO;
import info.oo.database.ConnectionFactory;
import info.oo.entities.User;

public class UserDAO implements IUserDAO {
    
    public ArrayList<User> getAll() {

        String query = "select * from user;";
        ArrayList<User> users = new ArrayList<User>();

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet result = stmt.executeQuery();
        ) {
            while(result.next()) {
                User user = new User(
                    result.getInt("id"),
                    result.getString("name"),
                    result.getString("login"),
                    result.getString("password"),
                    null
                );
                users.add(user);
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return users;
    }

}
