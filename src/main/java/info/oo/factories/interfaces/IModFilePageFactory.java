package info.oo.factories.interfaces;

import java.util.ArrayList;

import info.oo.entities.Mod;
import info.oo.entities.ModFile;
import info.oo.entities.ModLoader;
import info.oo.entities.ModOrigin;

public interface IModFilePageFactory {
    public ArrayList<ModFile> create(
        ArrayList<ModFile> modFiles,
        ArrayList<Mod> mods,
        ArrayList<ModLoader> modLoaders,
        ArrayList<ModOrigin> modOrigins
    );
}
