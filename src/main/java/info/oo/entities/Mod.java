package info.oo.entities;

public class Mod {

    private String name;
    private java.net.URL url;
    private String mineVersion;
    private String summary;
    private ModModule modModule;
    private ModOrigin modOrigin;

    public Mod(String name, java.net.URL url, String mineVersion, String summary, ModModule modModule, ModOrigin modOrigin) {
        this.name = name;
        this.url = url;
        this.mineVersion = mineVersion;
        this.summary = summary;
        this.modModule = modModule;
        this.modOrigin = modOrigin;
    }


}