package info.oo.entities;

import java.net.URL;

public class Mod {

    private int id;
    private String name;
    private URL url;
    private String summary;
    private ModLoader modLoader;
    private ModOrigin modOrigin;

    public Mod(int id) {
        this.id = id;
    }

    public Mod(int id, String name, URL url, String summary, ModLoader modLoader, ModOrigin modOrigin) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.summary = summary;
        this.modLoader = modLoader;
        this.modOrigin = modOrigin;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public URL getURL() {
        return this.url;
    }

    public String getSummary() {
        return this.summary;
    }

    public ModLoader modLoader() {
        return this.modLoader;
    }

    public ModOrigin getModOrigin() {
        return this.modOrigin;
    }

}