package info.oo.factories;

import java.util.ArrayList;
import java.util.stream.Collectors;

import info.oo.entities.FileModule;
import info.oo.entities.Mod;
import info.oo.entities.ModFile;
import info.oo.entities.ModLoader;
import info.oo.entities.ModModule;
import info.oo.entities.ModOrigin;
import info.oo.factories.interfaces.IModFilesFactory;
import info.oo.factories.interfaces.IModModulesFactory;

public class ModModulesFactory implements IModModulesFactory {

    private IModFilesFactory modFilesFactory;

    public ModModulesFactory(IModFilesFactory modFilesFactory) {
        this.modFilesFactory = modFilesFactory;
    }
    
    public ArrayList<ModModule> create(
        ArrayList<ModModule> modModules,
        ArrayList<FileModule> fileModules,
        ArrayList<ModFile> modFiles,
        ArrayList<Mod> mods,
        ArrayList<ModLoader> modLoaders,
        ArrayList<ModOrigin> modOrigins
    ) {
        ArrayList<ModFile> createdModFiles = modFilesFactory.create(modFiles, mods, modLoaders, modOrigins);
        return new ArrayList<ModModule>(modModules.stream().map(modModule -> new ModModule(
            modModule.getId(),
            modModule.getName(),
            modModule.getMinecraftVersion(),
            getModFilesFromModModule(fileModules, createdModFiles, modModule),
            modModule.getModLoader(),
            modModule.getUser()
        )).collect(Collectors.toList()));
    }

    private ArrayList<ModFile> getModFilesFromModModule(ArrayList<FileModule> fileModules, ArrayList<ModFile> modFiles, ModModule modModule) {
        return new ArrayList<ModFile>(
            modFiles.stream()
                .filter(modFile -> isModFileFromModModule(fileModules, modModule, modFile))
                .collect(Collectors.toList())
        );
    }

    private boolean isModFileFromModModule(ArrayList<FileModule> fileModules, ModModule modModule, ModFile modFile) {
        return fileModules.stream().anyMatch(fileModule ->
            fileModule.getModModuleId() == modModule.getId() &&
            fileModule.getModFileId() == modFile.getId()
        );
    }

}
