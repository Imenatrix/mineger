package info.oo.repositories.unitsOfWork;

import java.util.ArrayList;

import info.oo.dao.interfaces.IFileModuleDAO;
import info.oo.entities.FileModule;
import info.oo.entities.ModFile;
import info.oo.entities.ModModule;

public class FileModuleUnitOfWork {

    private ArrayList<FileModule> inserted;
    private ArrayList<FileModule> deleted;
    private IFileModuleDAO fileModuleDAO;
    
    public FileModuleUnitOfWork(ArrayList<ModModule> modModules, ArrayList<ModModule> oldModModules, IFileModuleDAO fileModuleDAO) {

        this.fileModuleDAO = fileModuleDAO;
        inserted = new ArrayList<FileModule>();
        deleted = new ArrayList<FileModule>();

        for (ModModule modModule : modModules) {

            ModModule oldModModule = oldModModules.stream()
                .filter(item -> item.getId() == modModule.getId())
                .findFirst()
                .get();

            ArrayList<ModFile> modFiles = modModule.getModFiles();
            ArrayList<ModFile> oldModFiles = oldModModule.getModFiles();
    
            for (ModFile modFile : modFiles) {
                if (!oldModFiles.stream().map(item -> item.getId()).anyMatch(item -> item == modFile.getId())) {
                    inserted.add(new FileModule(modFile.getId(), modModule.getId()));
                }
            }
    
            for (ModFile modFile : oldModFiles) {
                if (!modFiles.stream().map(item -> item.getId()).anyMatch(item -> item == modFile.getId())) {
                    deleted.add(new FileModule(modFile.getId(), modModule.getId()));
                }
            }
        }
    }

    public void commit() {
        for (FileModule fileModule : inserted) {
            fileModuleDAO.insert(fileModule);
        }
        for (FileModule fileModule : deleted) {
            fileModuleDAO.delete(fileModule);
        }
    }

}
