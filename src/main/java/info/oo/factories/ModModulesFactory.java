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
        return new ArrayList<ModModule>(modModules.stream().map(modModule -> new ModModule(
            modModule.getId(),
            modModule.getName(),
            modModule.getMinecraftVersion(),
            getModFilesFromModModule(
                modFilesFactory.create(
                    modFiles,
                    mods,
                    modLoaders,
                    modOrigins
                ),
                modModule,
                fileModules
            ),
            modModule.getModLoader(),
            modModule.getUser()
        )).collect(Collectors.toList()));
    }

    private ArrayList<ModFile> getModFilesFromModModule(ArrayList<ModFile> modFiles, ModModule modModule, ArrayList<FileModule> fileModules) {
        return new ArrayList<ModFile>(
            modFiles.stream()
                .filter(modFile -> isModFileFromModModule(modModule, modFile, fileModules))
                .collect(Collectors.toList())
        );
    }

    private boolean isModFileFromModModule(ModModule modModule, ModFile modFile, ArrayList<FileModule> fileModules) {
        return fileModules.stream().anyMatch(fileModule ->
            fileModule.getModModuleId() == modModule.getId() &&
            fileModule.getModFileId() == modFile.getId()
        );
    }

}
