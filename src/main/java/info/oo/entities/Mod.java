package info.oo.entities;

import java.net.URL;

public class Mod {

    private int id;
    private String name;
    private URL url;
    private String mineVersion;
    private String summary;
    private ModModule modModule;
    private ModOrigin modOrigin;

    public Mod(int id, String name, URL url, String mineVersion, String summary, ModModule modModule, ModOrigin modOrigin) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.mineVersion = mineVersion;
        this.summary = summary;
        this.modModule = modModule;
        this.modOrigin = modOrigin;
    }


}