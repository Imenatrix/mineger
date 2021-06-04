package info.oo.entities;

import java.net.URL;
import java.util.ArrayList;

public class ModOrigin {

    private int id;
    private String name;
    private URL url;
    private ArrayList<Mod> mods; 

    public ModOrigin(int id, String name, URL url, ArrayList<Mod> mods) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.mods = mods;
    }

    
}
