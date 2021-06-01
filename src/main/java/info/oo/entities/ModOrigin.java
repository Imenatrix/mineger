package info.oo.entities;

import java.util.ArrayList;

public class ModOrigin {

    private String name;
    private java.net.URL link;
    private ArrayList<Mod> mods = new ArrayList<Mod>(); 

    public ModOrigin (String name, java.net.URL link, Mod mods){
        this.name = name;
        this.link = link;
        this.mods.add(mods);
    }

    
}
