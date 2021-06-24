package info.oo.dao.interfaces;

import java.util.ArrayList;

import info.oo.entities.FileModule;

public interface IFileModuleDAO {

    public ArrayList<FileModule> getAllByModModuleId(int id);

}
