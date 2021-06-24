package info.oo.repositories.unitsOfWork;

import java.util.ArrayList;

import info.oo.dao.interfaces.IModModuleDAO;
import info.oo.entities.ModModule;
import info.oo.entities.User;

public class UserModModulesUnitOfWork {

    private User user;
    private ArrayList<ModModule> insertedModModules;
    private ArrayList<ModModule> deletedModModules;
    private IModModuleDAO modModuleDAO;
    
    public UserModModulesUnitOfWork(User user, ArrayList<ModModule> modModules, ArrayList<ModModule> oldModModules, IModModuleDAO modModuleDAO) {

        this.user = user;
        this.modModuleDAO = modModuleDAO;

        insertedModModules = new ArrayList<ModModule>(modModules);
        insertedModModules.removeIf(item -> 
            oldModModules.stream()
                .map(item2 -> item2.getId())
                .anyMatch(item2 -> item2 == item.getId())
        );

        deletedModModules = new ArrayList<ModModule>(oldModModules);
        deletedModModules.removeIf(item -> 
            modModules.stream()
                .map(item2 -> item2.getId())
                .anyMatch(item2 -> item2 == item.getId())
        );
    }

    public void commit() {
        for (ModModule modModule : insertedModModules) {
            ModModule newModModule = modModuleDAO.insert(modModule, user);
            modModule.setId(newModModule.getId());
        }
        for (ModModule modModule : deletedModModules) {
            modModuleDAO.delete(modModule);
        }
    }

}
