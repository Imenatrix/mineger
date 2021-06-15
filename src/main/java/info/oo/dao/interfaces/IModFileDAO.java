package info.oo.dao.interfaces;

import java.util.ArrayList;
import info.oo.entities.ModFile;

public interface IModFileDAO {
    
    public ModFile getById(int id);
    public ArrayList<ModFile> getAll();
    public ArrayList<ModFile> getAllByModModuleId(int id);
    public int getTotalPagesByModLoaderIdAndMinecraftVersion(int limit, int modLoaderId, String minecraftVersion);
    public ArrayList<ModFile> getPaginatedByModLoaderIdAndMinecraftVersion(int limit, int page, int modLoaderId, String minecraftVersion);
    public ArrayList<ModFile> getPaginatedByModLoaderIdAndMinecraftVersionAndSearch(int limit, int page, int modLoaderId, String minecraftVersion, String search);
    public int getTotalPagesByModLoaderIdAndMinecraftVersionAndSearch(int limit, int page, int modLoaderId, String minecraftVersion, String search);

}
