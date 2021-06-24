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

    public int getTotalPages(
        int limit,
        Integer modLoaderId,
        Integer modOriginId,
        String minecraftVersion,
        String search
    ) {
        return modFileDAO.getTotalPages(
            limit,
            modLoaderId,
            modOriginId,
            minecraftVersion,
            search
        );
    }
    
    public ArrayList<ModFile> getPage(
        int limit,
        int page,
        Integer modLoaderId,
        Integer modOriginId,
        String minecraftVersion,
        String search
    ) {
        ArrayList<ModFile> modFiles = modFileDAO.getPage(
            limit,
            page,
            modLoaderId,
            modOriginId,
            minecraftVersion,
            search
        );
        ArrayList<Mod> mods = fetchModsFromModFiles(modFiles);
        ArrayList<ModLoader> modLoaders = fetchModLoadersFromMods(mods);
        ArrayList<ModOrigin> modOrigins = fetchModOriginsFromMods(mods);
        return modFilePageFactory.create(modFiles, mods, modLoaders, modOrigins);
    }

    private ArrayList<ModLoader> fetchModLoadersFromMods(ArrayList<Mod> mods) {
        ArrayList<ModLoader> modLoaders = new ArrayList<ModLoader>();
        for (Mod mod : mods) {
            int modLoaderId = mod.getModLoader().getId();
            if (!modLoaders.stream().map(item -> item.getId()).anyMatch(item -> item == modLoaderId)) {
                ModLoader modLoader = modLoaderDAO.getById(modLoaderId);
                modLoaders.add(modLoader);
            }
        }
        return modLoaders;
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
    
}
