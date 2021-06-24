package info.oo.dao.interfaces;

import java.util.ArrayList;

import info.oo.entities.Mod;
import info.oo.entities.ModFile;

public interface IModDAO {
    public Mod getById(int id);
    public ArrayList<Mod> getAllByModFiles(ArrayList<ModFile> modFiles);
}
