package info.oo.entities;

import java.util.ArrayList;

public class ModOrigin {

    private int id;
    private String name;
    private java.net.URL url;
    private ArrayList<Mod> mods; 

    public ModOrigin(int id, String name, java.net.URL url, ArrayList<Mod> mods) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.mods = mods;
    }

    
}
