package info.oo.entities;

import java.net.URL;

public class ModFile {
    
    private int id;
    private String fileName;
    private URL url;
    private String minecraftVersion;
    private Mod mod;

    public ModFile(int id, String fileName, URL url, String minecraftVersion, Mod mod) {
        this.id = id;
        this.fileName = fileName;
        this.url = url;
        this.minecraftVersion = minecraftVersion;
        this.mod = mod;
    }

    public int getId() {
        return this.id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Mod getMod() {
        return this.mod;
    }

}
