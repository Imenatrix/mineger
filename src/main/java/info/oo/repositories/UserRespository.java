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
        ArrayList<ModLoader> modLoaders = fetchModLoadersFromModModules(modModules);
        ArrayList<FileModule> fileModules = fetchFileModulesFromModModules(modModules);
        ArrayList<ModFile> modFiles = fetchModFilesFromFileModules(fileModules);
        ArrayList<Mod> mods = fetchModsFromModFiles(modFiles);
        ArrayList<ModOrigin> modOrigins = fetchModOriginsFromMods(mods);
        updateModLoadersFromMods(modLoaders, mods);

        return userFactory.create(
            user,
            modModules,
            modFiles,
            modLoaders,
            fileModules,
            mods,
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

    private void updateModLoadersFromMods(ArrayList<ModLoader> modLoaders, ArrayList<Mod> mods) {
        for (Mod mod : mods) {
            int modLoaderId = mod.getModLoader().getId();
            if (!modLoaders.stream().map(item -> item.getId()).anyMatch(item -> item == modLoaderId)) {
                ModLoader modLoader = modLoaderDAO.getById(modLoaderId);
                modLoaders.add(modLoader);
            }
        }
    }

    private ArrayList<ModOrigin> fetchModOriginsFromMods(ArrayList<Mod> mods) {
        ArrayList<ModOrigin> modOrigins = new ArrayList<ModOrigin>();
        for (Mod mod : mods) {
            int modOriginId = mod.getModOrigin().getId();
            if (!modOrigins.stream().map(item -> item.getId()).anyMatch(item -> item == modOriginId)) {
                ModOrigin modOrigin = modOriginDAO.getById(modOriginId);
                modOrigins.add(modOrigin);
            }
        }
        return modOrigins;
    }

    private ArrayList<Mod> fetchModsFromModFiles(ArrayList<ModFile> modFiles) {
        ArrayList<Mod> mods = new ArrayList<Mod>();
        for (ModFile modFile : modFiles) {
            int modId = modFile.getMod().getId();
            if (!mods.stream().map(item -> item.getId()).anyMatch(item -> item == modId)) {
                Mod mod = modDAO.getById(modId);
                mods.add(mod);
            }
        }
        return mods;
    }

    private ArrayList<ModFile> fetchModFilesFromFileModules(ArrayList<FileModule> fileModules) {
        ArrayList<ModFile> modFiles = new ArrayList<ModFile>();
        for (FileModule fileModule : fileModules) {
            int modFileId = fileModule.getModFileId();
            if (!modFiles.stream().map(item -> item.getId()).anyMatch(item -> item == modFileId)) {
                ModFile modFile = modFileDAO.getById(modFileId);
                modFiles.add(modFile);
            }
        }
        return modFiles;
    }

    private ArrayList<FileModule> fetchFileModulesFromModModules(ArrayList<ModModule> modModules) {
        ArrayList<FileModule> fileModules = new ArrayList<FileModule>();
        for (ModModule modModule : modModules) {
            int modModuleId = modModule.getId();
            if (!fileModules.stream().map(item -> item.getModModuleId()).anyMatch(item -> item == modModuleId)) {
                fileModules.addAll(fileModuleDAO.getAllByModModuleId(modModuleId));
            }
        }
        return fileModules;
    }

    private ArrayList<ModLoader> fetchModLoadersFromModModules(ArrayList<ModModule> modModules) {
        ArrayList<ModLoader> modLoaders = new ArrayList<ModLoader>();
        for (ModModule modModule : modModules) {
            int modLoaderId = modModule.getModLoader().getId();
            if (!modLoaders.stream().map(item -> item.getId()).anyMatch(item -> item == modLoaderId)) {
                ModLoader modLoader = modLoaderDAO.getById(modLoaderId);
                modLoaders.add(modLoader);
            }
        }
        return modLoaders;
    }

}
