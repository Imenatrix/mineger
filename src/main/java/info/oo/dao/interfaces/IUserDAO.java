package info.oo.dao.interfaces;

import info.oo.entities.User;

public interface IUserDAO {
    
    public User getById(int id);
    public User getByLoginAndPassword(String login, String password);
    public User insert(User user);

}
