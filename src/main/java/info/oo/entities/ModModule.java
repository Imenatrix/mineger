package info.oo.entities;

import java.util.ArrayList;

public class ModModule {
    
    private int id;
    private String name;
    private String minecraftVersion;
    private ArrayList<ModFile> modFiles;
    private ModLoader modLoader;
    private User user;

    public ModModule(int id, String name, String minecraftVersion, ArrayList<ModFile> modFiles, ModLoader modLoader, User user) {
        this.id = id;
        this.name = name;
        this.minecraftVersion = minecraftVersion;
        this.modFiles = modFiles;
        this.modLoader = modLoader;
        this.user = user;
    }

    public ModModule(int id, String name, String minecraftVersion, ModLoader modLoader, User user) {
        this.id = id;
        this.name = name;
        this.minecraftVersion = minecraftVersion;
        this.modFiles = new ArrayList<ModFile>();
        this.modLoader = modLoader;
        this.user = user;
    }

    public ModModule(String name, String minecraftVersion, ModLoader modLoader) {
        this.name = name;
        this.minecraftVersion = minecraftVersion;
        this.modFiles = new ArrayList<ModFile>();
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

    public User getUser() {
        return this.user;
    }

}
