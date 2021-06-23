package info.oo.factories;

import java.util.ArrayList;
import java.util.stream.Collectors;

import info.oo.entities.Mod;
import info.oo.entities.ModFile;
import info.oo.entities.ModLoader;
import info.oo.entities.ModOrigin;
import info.oo.factories.interfaces.IModFilesFactory;
import info.oo.factories.interfaces.IModsFactory;

public class ModFilesFactory implements IModFilesFactory {

    private IModsFactory modsFactory;

    public ModFilesFactory(IModsFactory modsFactory) {
        this.modsFactory = modsFactory;
    }

    public ArrayList<ModFile> create(
        ArrayList<ModFile> modFiles,
        ArrayList<Mod> mods,
        ArrayList<ModLoader> modLoaders,
        ArrayList<ModOrigin> modOrigins
    ) {
        return new ArrayList<ModFile>(modFiles.stream().map(item -> new ModFile(
            item.getId(),
            item.getFileName(),
            item.getURL(),
            item.getMinecraftVersion(),
            getModFromModFile(
                modsFactory.create(
                    mods,
                    modLoaders,
                    modOrigins
                ),
                item
            )
        )).collect(Collectors.toList()));
    }

    private Mod getModFromModFile(ArrayList<Mod> createdMods, ModFile modFile) {
        return createdMods.stream()
            .filter(mod -> mod.getId() == modFile.getMod().getId())
            .findFirst()
            .get();
    }
    


}
