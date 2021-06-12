package info.oo.dao.interfaces;

import java.util.ArrayList;
import info.oo.entities.ModOrigin;

public interface IModOriginDAO {

    public ModOrigin getById(int id);
    public ArrayList<ModOrigin> getAll();
    
}
