package info.oo.dao.interfaces;

import java.util.ArrayList;
import info.oo.entities.ModLoader;

public interface IModLoaderDAO {
    
    public ModLoader getById(int id);
    public ArrayList<ModLoader> getAll();

}
