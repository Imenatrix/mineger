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
        this.inserted = new ArrayList<FileModule>();
        this.deleted = new ArrayList<FileModule>();

        for (ModModule modModule : modModules) {

            ModModule oldModModule = getOldModModule(oldModModules, modModule);

            ArrayList<ModFile> modFiles = modModule.getModFiles();
            ArrayList<ModFile> oldModFiles = oldModModule.getModFiles();
    
            addInsertedModFiles(modModule, modFiles, oldModFiles);
            addDeletedModFiles(modModule, modFiles, oldModFiles);
        }
    }

    private void addDeletedModFiles(ModModule modModule, ArrayList<ModFile> modFiles, ArrayList<ModFile> oldModFiles) {
        for (ModFile modFile : oldModFiles) {
            if (!modFiles.stream().map(item -> item.getId()).anyMatch(item -> item == modFile.getId())) {
                deleted.add(new FileModule(modFile.getId(), modModule.getId()));
            }
        }
    }

    private void addInsertedModFiles(ModModule modModule, ArrayList<ModFile> modFiles, ArrayList<ModFile> oldModFiles) {
        for (ModFile modFile : modFiles) {
            if (!oldModFiles.stream().map(item -> item.getId()).anyMatch(item -> item == modFile.getId())) {
                inserted.add(new FileModule(modFile.getId(), modModule.getId()));
            }
        }
    }

    private ModModule getOldModModule(ArrayList<ModModule> oldModModules, ModModule modModule) {
        return oldModModules.stream()
            .filter(item -> item.getId() == modModule.getId())
            .findFirst()
            .get();
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
