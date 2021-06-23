package info.oo.entities;

import java.net.URL;

public class ModFile {
    
    private int id;
    private String fileName;
    private URL url;
    private String minecraftVersion;
    private Mod mod;

    public ModFile(int id, String fileName, URL url, String minecraftVersion) {
        this.id = id;
        this.fileName = fileName;
        this.url = url;
        this.minecraftVersion = minecraftVersion;
    }

    public int getId() {
        return this.id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public URL getURL() {
        return this.url;
    }

    public String getMinecraftVersion() {
        return this.minecraftVersion;
    }

    public Mod getMod() {
        return this.mod;
    }

}
