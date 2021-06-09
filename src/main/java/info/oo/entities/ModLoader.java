package info.oo.entities;

import java.net.URL;

public class ModLoader {
    
    private int id;
    private String name;
    private URL url;

    public ModLoader(int id, String name, URL url) {
        this.id = id;
        this.name = name;
        this.url = url;
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
    
}
