package info.oo.repositories;

import java.util.ArrayList;

import info.oo.dao.interfaces.IModDAO;
import info.oo.dao.interfaces.IModFileDAO;
import info.oo.dao.interfaces.IModLoaderDAO;
import info.oo.dao.interfaces.IModOriginDAO;
import info.oo.entities.Mod;
import info.oo.entities.ModFile;
import info.oo.entities.ModLoader;
import info.oo.entities.ModOrigin;
import info.oo.factories.interfaces.IModFilesFactory;
import info.oo.repositories.interfaces.IModFilePageRepository;

public class ModFilePageRepository implements IModFilePageRepository {

    private IModFileDAO modFileDAO;
    private IModDAO modDAO;
    private IModLoaderDAO modLoaderDAO;
    private IModOriginDAO modOriginDAO;
    private IModFilesFactory modFilePageFactory;

    public ModFilePageRepository(
        IModFileDAO modFileDAO,
        IModDAO modDAO,
        IModLoaderDAO modLoaderDAO,
        IModOriginDAO modOriginDAO,
        IModFilesFactory modFilePageFactory
    ) {
        this.modFileDAO = modFileDAO;
        this.modDAO = modDAO;
        this.modLoaderDAO = modLoaderDAO;
        this.modOriginDAO = modOriginDAO;
        this.modFilePageFactory = modFilePageFactory;
    }

    public int getTotalPages(int limit, Integer modLoaderId, Integer modOriginId, String minecraftVersion, String search) {
        return modFileDAO.getTotalPages(limit, modLoaderId, modOriginId, minecraftVersion, search);
    }
    
    public ArrayList<ModFile> getPage(int limit, int page, Integer modLoaderId, Integer modOriginId, String minecraftVersion, String search) {
        ArrayList<ModFile> modFiles = modFileDAO.getPage(limit, page, modLoaderId, modOriginId, minecraftVersion, search);
        ArrayList<Mod> mods = modDAO.getAllByModFiles(modFiles);
        ArrayList<ModLoader> modLoaders = modLoaderDAO.getAllByMods(mods);
        ArrayList<ModOrigin> modOrigins = modOriginDAO.getAllByMods(mods);
        return modFilePageFactory.create(modFiles, mods, modLoaders, modOrigins);
    }
    
}
