package info.oo.dao.interfaces;

import java.util.ArrayList;

import info.oo.entities.ModFile;
import info.oo.entities.ModModule;
import info.oo.entities.User;

public interface IModModuleDAO {
    
    public ArrayList<ModModule> getAll();
    public ArrayList<ModModule> getAllByUserId(int id);
    public ModModule insert(ModModule modModule, User user);
    public boolean addModFile(ModModule modModule, ModFile modFile);
    public boolean delete(int id);

}
