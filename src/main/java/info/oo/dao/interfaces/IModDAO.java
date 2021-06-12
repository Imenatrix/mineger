package info.oo.dao.interfaces;

import java.util.ArrayList;
import info.oo.entities.Mod;

public interface IModDAO {

    public Mod getById(int id);
    public ArrayList<Mod> getAll();

}
