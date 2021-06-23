package info.oo.factories;

import java.util.ArrayList;
import java.util.stream.Collectors;

import info.oo.entities.Mod;
import info.oo.entities.ModFile;
import info.oo.entities.ModLoader;
import info.oo.entities.ModOrigin;
import info.oo.factories.interfaces.IModFilePageFactory;

public class ModFilePageFactory implements IModFilePageFactory {

    public ArrayList<ModFile> create(
        ArrayList<ModFile> modFiles,
        ArrayList<Mod> mods,
        ArrayList<ModLoader> modLoaders,
        ArrayList<ModOrigin> modOrigins
    ) {
        ArrayList<Mod> createdMods = new ArrayList<Mod>(mods.stream().map(item -> new Mod(
            item.getId(),
            item.getName(),
            item.getURL(),
            item.getSummary(),
            modLoaders.stream()
                .filter(item2 -> item2.getId() == item.getModLoader().getId())
                .findFirst()
                .get(),
            modOrigins.stream()
                .filter(item2 -> item2.getId() == item.getModOrigin().getId())
                .findFirst()
                .get()
        )).collect(Collectors.toList()));
        ArrayList<ModFile> createdModFiles = new ArrayList<ModFile>(modFiles.stream().map(item -> new ModFile(
            item.getId(),
            item.getFileName(),
            item.getURL(),
            item.getMinecraftVersion(),
            createdMods.stream()
                .filter(item2 -> item2.getId() == item.getMod().getId())
                .findFirst()
                .get()
        )).collect(Collectors.toList()));
        return createdModFiles;
    }
    


}
