package info.oo.dao.interfaces;

import java.util.ArrayList;
import info.oo.entities.ModFile;

public interface IModFileDAO {
    
    public ModFile getById(int id);
    public ArrayList<ModFile> getAll();
    public ArrayList<ModFile> getAllByModModuleId(int id);
    public int getTotalPages(int limit, Integer modLoaderId, Integer modOriginId, String minecraftVersion, String search);
    public ArrayList<ModFile> getPaginated(int limit, int page, Integer modLoaderId, Integer modOriginId, String minecraftVersion, String search);
}
