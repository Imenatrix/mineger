package info.oo.dao.interfaces;

import java.util.ArrayList;

import info.oo.entities.User;

public interface IUserDAO {
    
    public User getById(int id);
    public ArrayList<User> getAll();
    public User getByLoginAndPassword(String login, String password);
    public User insert(User user);

}
