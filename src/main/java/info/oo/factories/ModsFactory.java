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
            modLoaders.stream()
                .filter(item2 -> item2.getId() == item.getModLoader().getId())
                .findFirst()
                .get(),
            modOrigins.stream()
                .filter(item2 -> item2.getId() == item.getModOrigin().getId())
                .findFirst()
                .get()
        )).collect(Collectors.toList()));
    }

}
