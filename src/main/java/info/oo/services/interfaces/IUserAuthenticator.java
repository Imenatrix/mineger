package info.oo.services.interfaces;

public interface IUserAuthenticator {
    public Integer authenticate(String login, String password);
}
