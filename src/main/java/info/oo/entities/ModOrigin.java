package info.oo.entities;

import java.util.ArrayList;

public class ModOrigin {

    private String name;
    private java.net.URL url;
    private ArrayList<Mod> mods; 

    public ModOrigin (String name, java.net.URL url, ArrayList<Mod> mods){
        this.name = name;
        this.url = url;
        this.mods = mods;
    }

    
}
