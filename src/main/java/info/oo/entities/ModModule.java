package info.oo.entities;

import java.util.ArrayList;

public class ModModule {
    
    private int id;
    private String name;
    private String minecraftVersion;
    private ArrayList<ModFile> modFiles;
    private ModLoader modLoader;

    public ModModule(int id, String name, String minecraftVersion, ArrayList<ModFile> modFiles, ModLoader modLoader) {
        this.id = id;
        this.name = name;
        this.minecraftVersion = minecraftVersion;
        this.modFiles= modFiles;
        this.modLoader = modLoader;
    }

    public ModModule(String name, String minecraftVersion, ModLoader modLoader) {
        this.name = name;
        this.minecraftVersion = minecraftVersion;
        this.modLoader = modLoader;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getMinecraftVersion() {
        return this.minecraftVersion;
    }

    public ArrayList<ModFile> getModFiles() {
        return this.modFiles;
    }

    public ModLoader getModLoader() {
        return this.modLoader;
    }

}
