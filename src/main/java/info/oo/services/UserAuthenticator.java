package info.oo.services;

import info.oo.dao.interfaces.IUserDAO;
import info.oo.entities.User;
import info.oo.services.interfaces.IUserAuthenticator;

public class UserAuthenticator implements IUserAuthenticator {

    private IUserDAO userDAO;
    
    public UserAuthenticator(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Integer authenticate(String login, String password) {
        User user = userDAO.getByLoginAndPassword(login, password);
        if (user == null) {
            return null;
        }
        return user.getId();
    }

}
