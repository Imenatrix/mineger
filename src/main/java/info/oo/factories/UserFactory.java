package info.oo.factories;

import java.util.ArrayList;

import info.oo.entities.FileModule;
import info.oo.entities.Mod;
import info.oo.entities.ModFile;
import info.oo.entities.ModLoader;
import info.oo.entities.ModModule;
import info.oo.entities.ModOrigin;
import info.oo.entities.User;
import info.oo.factories.interfaces.IModModulesFactory;
import info.oo.factories.interfaces.IUserFactory;

public class UserFactory implements IUserFactory{

    private IModModulesFactory modModulesFactory;

    public UserFactory(IModModulesFactory modModulesFactory) {
        this.modModulesFactory = modModulesFactory;
    }
    
    public User create(
        User user,
        ArrayList<ModModule> modModules,
        ArrayList<FileModule> fileModules,
        ArrayList<ModFile> modFiles,
        ArrayList<Mod> mods,
        ArrayList<ModLoader> modLoaders,
        ArrayList<ModOrigin> modOrigins
    ) {
        return new User(
            user.getId(),
            user.getName(),
            modModulesFactory.create(
                modModules,
                fileModules,
                modFiles,
                mods,
                modLoaders,
                modOrigins
            )
        );
    }

}
