package info.oo.factories.interfaces;

import java.util.ArrayList;

import info.oo.entities.FileModule;
import info.oo.entities.Mod;
import info.oo.entities.ModFile;
import info.oo.entities.ModLoader;
import info.oo.entities.ModModule;
import info.oo.entities.ModOrigin;
import info.oo.entities.User;

public interface IUserFactory {
    public User create(
        User user,
        ArrayList<ModModule> modModules,
        ArrayList<FileModule> fileModules,
        ArrayList<ModFile> modFiles,
        ArrayList<Mod> mods,
        ArrayList<ModLoader> modLoaders,
        ArrayList<ModOrigin> modOrigins
    );
}
