package info.oo.dao.interfaces;

import java.util.ArrayList;

import info.oo.entities.FileModule;
import info.oo.entities.ModFile;

public interface IModFileDAO {
    
    public ModFile getById(int id);
    public ArrayList<ModFile> getAllByModModuleId(int id);
    public ArrayList<ModFile> getByFileModules(ArrayList<FileModule> fileModules);
    public int getTotalPages(int limit, Integer modLoaderId, Integer modOriginId, String minecraftVersion, String search);
    public ArrayList<ModFile> getPage(int limit, int page, Integer modLoaderId, Integer modOriginId, String minecraftVersion, String search);
}
