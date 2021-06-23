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
        ArrayList<ModModule> createdModModules = new ArrayList<ModModule>(modModules.stream().map(item -> new ModModule(
            item.getId(),
            item.getName(),
            item.getMinecraftVersion(),
            new ArrayList<ModFile>(
                createdModFiles.stream()
                    .filter(
                        item2 -> fileModules.stream()
                            .filter(item3 -> item3.getModModuleId() == item.getId())
                            .anyMatch(item3 -> item3.getModFileId() == item2.getId())
                    )
                    .collect(Collectors.toList())
            ),
            item.getModLoader(),
            item.getUser()
        )).collect(Collectors.toList()));
        return createdModModules;
    }

}
