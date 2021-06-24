package info.oo.repositories.interfaces;

import info.oo.entities.User;

public interface IUserRepository {
    
    public User getById(int id);
    public void save(User user);
}
