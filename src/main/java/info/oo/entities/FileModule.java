package info.oo.entities;

public class FileModule {
    
    private int modFileId;
    private int modModuleId;

    public FileModule(int modFileId, int modModuleId) {
        this.modFileId = modFileId;
        this.modModuleId = modModuleId;
    }

    public int getModFileId() {
        return modFileId;
    }

    public int getModModuleId() {
        return modModuleId;
    }

}
