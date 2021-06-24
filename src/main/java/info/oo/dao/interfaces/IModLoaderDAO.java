package info.oo.dao.interfaces;

import java.util.ArrayList;

import info.oo.entities.Mod;
import info.oo.entities.ModLoader;
import info.oo.entities.ModModule;

public interface IModLoaderDAO {
    public ModLoader getById(int id);
    public ArrayList<ModLoader> getAll();
    public ArrayList<ModLoader> getAllByModModules(ArrayList<ModModule> modModules);
    public ArrayList<ModLoader> getAllByMods(ArrayList<Mod> mods);
}
