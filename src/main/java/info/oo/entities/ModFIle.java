package info.oo.entities;

public class ModFile {
    
    private int id;
    private String fileName;
    private java.net.URL url;
    private String mineVersion;
    private Mod mod;

    public ModFile(int id, String fileName, java.net.URl url, String mineVersion, Mod mod) {
        this.id = id;
        this.fileName = fileName;
        this.url = url;
        this.mineVersion = mineVersion;
        this.mod = mod;
    }

}
