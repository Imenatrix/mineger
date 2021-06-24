package info.oo.repositories.unitsOfWork;

import java.util.ArrayList;

import info.oo.dao.interfaces.IModModuleDAO;
import info.oo.entities.ModFile;
import info.oo.entities.ModModule;

public class ModModuleUnitOfWork {

    private ModModule modModule;
    private ArrayList<ModFile> insertedModFiles;
    private ArrayList<ModFile> deletedModFiles;
    private IModModuleDAO modModuleDAO;
    
    public ModModuleUnitOfWork(ModModule modModule, ModModule oldModModule, IModModuleDAO modModuleDAO) {

        this.modModule = modModule;
        this.modModuleDAO = modModuleDAO;

        ArrayList<ModFile> modFiles = modModule.getModFiles();
        ArrayList<ModFile> oldModFiles = oldModModule.getModFiles();

        insertedModFiles = new ArrayList<ModFile>(modFiles);
        insertedModFiles.removeIf(item -> 
            oldModFiles.stream()
                .map(item2 -> item2.getId())
                .anyMatch(item2 -> item2 == item.getId())
        );

        deletedModFiles = new ArrayList<ModFile>(oldModFiles);
        deletedModFiles.removeIf(item -> 
            modFiles.stream()
                .map(item2 -> item2.getId())
                .anyMatch(item2 -> item2 == item.getId())
        );
    }

    public void commit() {
        for (ModFile modFile : insertedModFiles) {
            modModuleDAO.addModFile(modModule, modFile);
        }
        for (ModFile modFile : deletedModFiles) {
            modModuleDAO.removeModFile(modModule, modFile);
        }
    }

}
