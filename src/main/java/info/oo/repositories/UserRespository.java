package info.oo.repositories;

import info.oo.dao.interfaces.IFileModuleDAO;
import info.oo.dao.interfaces.IModDAO;
import info.oo.dao.interfaces.IModFileDAO;
import info.oo.dao.interfaces.IModLoaderDAO;
import info.oo.dao.interfaces.IModModuleDAO;
import info.oo.dao.interfaces.IModOriginDAO;
import info.oo.dao.interfaces.IUserDAO;
import info.oo.entities.FileModule;
import info.oo.entities.Mod;
import info.oo.entities.ModFile;
import info.oo.entities.ModLoader;
import info.oo.entities.ModModule;
import info.oo.entities.ModOrigin;
import info.oo.entities.User;
import info.oo.factories.interfaces.IUserFactory;
import info.oo.repositories.interfaces.IUserRepository;
import info.oo.repositories.unitsOfWork.ModModuleUnitOfWork;
import info.oo.repositories.unitsOfWork.UserModModulesUnitOfWork;

import java.util.ArrayList;

public class UserRespository implements IUserRepository {

    private IUserDAO userDAO;
    private IModModuleDAO modModuleDAO;
    private IModLoaderDAO modLoaderDAO;
    private IModFileDAO modFileDAO;
    private IFileModuleDAO fileModuleDAO;
    private IModDAO modDAO;
    private IModOriginDAO modOriginDAO;
    private IUserFactory userFactory;

    public UserRespository(
        IUserDAO userDAO,
        IModModuleDAO modModuleDAO,
        IModLoaderDAO modLoaderDAO,
        IModFileDAO modFileDAO,
        IFileModuleDAO fileModuleDAO,
        IModDAO modDAO,
        IModOriginDAO modOriginDAO,
        IUserFactory userFactory
    ) {
        this.userDAO = userDAO;
        this.modModuleDAO = modModuleDAO;
        this.modLoaderDAO = modLoaderDAO;
        this.modFileDAO = modFileDAO;
        this.fileModuleDAO = fileModuleDAO;
        this.modDAO = modDAO;
        this.modOriginDAO = modOriginDAO;
        this.userFactory = userFactory;
    }
    
    public User getById(int id) {

        User user = userDAO.getById(id);
        ArrayList<ModModule> modModules = modModuleDAO.getAllByUserId(id);
        ArrayList<ModLoader> modLoaders = modLoaderDAO.getAllByModModules(modModules);
        ArrayList<FileModule> fileModules = fileModuleDAO.getAllByModModules(modModules);
        ArrayList<ModFile> modFiles = modFileDAO.getByFileModules(fileModules);
        ArrayList<Mod> mods = modDAO.getAllByModFiles(modFiles);
        ArrayList<ModOrigin> modOrigins = modOriginDAO.getAllByMods(mods);
        modLoaders.addAll(modLoaderDAO.getAllByMods(mods));

        return userFactory.create(
            user,
            modModules,
            fileModules,
            modFiles,
            mods,
            modLoaders,
            modOrigins
        );
    }

    public void save(User user) {
        User oldUser = getById(user.getId());
        ArrayList<ModModule> modModules = user.getModModules();
        ArrayList<ModModule> oldModModules = oldUser.getModModules();
        UserModModulesUnitOfWork userModModulesUnitOfWork = new UserModModulesUnitOfWork(
            user,
            modModules,
            oldModModules,
            modModuleDAO
        );
        userModModulesUnitOfWork.commit();

        ArrayList<ModModule> mantainedModModules = new ArrayList<ModModule>(modModules);
        mantainedModModules.removeIf(item ->
            !oldModModules.stream()
                .map(item2 -> item2.getId())
                .anyMatch(item2 -> item2 == item.getId())
        );
        for (ModModule modModule : mantainedModModules) {
            ModModule oldModModule = oldModModules.stream()
                .filter(item -> item.getId() == modModule.getId())
                .findFirst()
                .get();
            ModModuleUnitOfWork modModuleUnitOfWork = new ModModuleUnitOfWork(modModule, oldModModule, modModuleDAO);
            modModuleUnitOfWork.commit();
        }
    }

}
