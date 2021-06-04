package info.oo.entities;

import java.net.URL;

public class ModFile {
    
    private int id;
    private String fileName;
    private URL url;
    private String mineVersion;
    private Mod mod;

    public ModFile(int id, String fileName, URL url, String mineVersion, Mod mod) {
        this.id = id;
        this.fileName = fileName;
        this.url = url;
        this.mineVersion = mineVersion;
        this.mod = mod;
    }

}
