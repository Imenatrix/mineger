package info.oo.dao.interfaces;

import java.util.ArrayList;

import info.oo.entities.FileModule;
import info.oo.entities.ModModule;

public interface IFileModuleDAO {
    public ArrayList<FileModule> getAllByModModules(ArrayList<ModModule> modModules);
}
