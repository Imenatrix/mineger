package info.oo.factories;

import java.util.ArrayList;
import java.util.stream.Collectors;

import info.oo.entities.Mod;
import info.oo.entities.ModLoader;
import info.oo.entities.ModOrigin;
import info.oo.factories.interfaces.IModsFactory;

public class ModsFactory implements IModsFactory {
    
    public ArrayList<Mod> create(ArrayList<Mod> mods, ArrayList<ModLoader> modLoaders, ArrayList<ModOrigin> modOrigins) {
        return new ArrayList<Mod>(mods.stream().map(item -> new Mod(
            item.getId(),
            item.getName(),
            item.getURL(),
            item.getSummary(),
            getModLoaderFromMod(modLoaders, item),
            getModOriginFromMod(modOrigins, item)
        )).collect(Collectors.toList()));
    }

    private ModOrigin getModOriginFromMod(ArrayList<ModOrigin> modOrigins, Mod mod) {
        return modOrigins.stream()
            .filter(modOrigin -> modOrigin.getId() == mod.getModOrigin().getId())
            .findFirst()
            .get();
    }

    private ModLoader getModLoaderFromMod(ArrayList<ModLoader> modLoaders, Mod mod) {
        return modLoaders.stream()
            .filter(modLoader -> modLoader.getId() == mod.getModLoader().getId())
            .findFirst()
            .get();
    }

}
