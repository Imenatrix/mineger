package info.oo.entities;

import java.util.ArrayList;

public class ModOrigin {

    private String name;
    private java.net.URL url;
    private ArrayList<Mod> mods = new ArrayList<Mod>(); 

    public ModOrigin (String name, java.net.URL url, Mod mods){
        this.name = name;
        this.url = url;
        this.mods.add(mods);
    }

    
}
