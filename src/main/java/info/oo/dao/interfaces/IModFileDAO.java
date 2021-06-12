package info.oo.dao.interfaces;

import java.util.ArrayList;
import info.oo.entities.ModFile;

public interface IModFileDAO {
    
    public ModFile getById(int id);
    public ArrayList<ModFile> getAll();
    public ArrayList<ModFile> getAllByModModuleId(int id);

}
