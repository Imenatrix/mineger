package info.oo.factories.interfaces;

import java.util.ArrayList;

import info.oo.entities.Mod;
import info.oo.entities.ModLoader;
import info.oo.entities.ModOrigin;

public interface IModsFactory {
    public ArrayList<Mod> create(ArrayList<Mod> mods, ArrayList<ModLoader> modLoaders, ArrayList<ModOrigin> modOrigins);
}
