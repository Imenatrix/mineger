package info.oo.entities;

import java.util.ArrayList;

public class User {

    private String name;
    private String login;
    private String password;
    private ArrayList<ModModule> modModules = new ArrayList<ModModule>();

    public User (String name, String login, String password, ModModule modModules){
        this.name = name;
        this.login = login;
        this.password = password;
        this.modModules.add(modModules);
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