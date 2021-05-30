package info.oo.entities;

//import java.util.ArrayList;

public class User {

    private String nameU;
    private String login;
    private String password;
    private ModModule modModules;

    public User (String nameU, String login, String password, ModModule modModules){
        this.nameU = nameU;
        this.login = login;
        this.password = password;
    }

    public String getNameU(){
        return nameU;
    }

    public void setNameU(String nameU){
        this.nameU = nameU;
    }

    public String getLogin(){
        return login;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }



}