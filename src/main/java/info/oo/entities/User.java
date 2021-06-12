package info.oo.entities;

import java.util.ArrayList;

public class User {

    private int id;
    private String name;
    private String login;
    private String password;
    private ArrayList<ModModule> modModules;

    public User(int id, String name, String login, String password, ArrayList<ModModule> modModules) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.modModules = modModules;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
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
    
    
    @Override
    public String toString(){
        return "User { name: " 
            + name + " login: " 
            + login + " password: " 
            + password + " modModules: " 
            + modModules + " }";
    }
}