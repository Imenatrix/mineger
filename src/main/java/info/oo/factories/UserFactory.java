package info.oo.factories;

import java.util.ArrayList;
import java.util.stream.Collectors;

import info.oo.entities.FileModule;
import info.oo.entities.Mod;
import info.oo.entities.ModFile;
import info.oo.entities.ModLoader;
import info.oo.entities.ModModule;
import info.oo.entities.ModOrigin;
import info.oo.entities.User;
import info.oo.factories.interfaces.IUserFactory;

public class UserFactory implements IUserFactory{
    
    public User create(
        User user,
        ArrayList<ModModule> modModules,
        ArrayList<ModFile> modFiles,
        ArrayList<ModLoader> modLoaders,
        ArrayList<FileModule> fileModules,
        ArrayList<Mod> mods,
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
        User createdUser = new User(
            user.getId(),
            user.getName(),
            createdModModules
        );
        return createdUser;
    }

}
