package info.oo.entities;

import java.util.ArrayList;

public class ModModule {
    
    private String name;
    private String mineVersion;
    private User user;
    private ArrayList<Mod> mods = new ArrayList<Mod>();
    private ModLoader modLoader;

    public ModModule (String name, String mineVersion, User user, Mod mods, ModLoader modLoader){
        this.name = name;
        this.mineVersion = mineVersion;
        this.user = user;
        this.mods.add(mods);
        this.modLoader = modLoader;
    }



}
