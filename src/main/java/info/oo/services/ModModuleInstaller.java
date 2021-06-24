package info.oo.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import info.oo.entities.ModFile;
import info.oo.entities.ModModule;
import info.oo.services.interfaces.IModModuleInstaller;
import info.oo.utils.VoidCallback;

public class ModModuleInstaller implements IModModuleInstaller {

    private Path cache = Paths.get(
        System.getProperty("user.home"),
        "AppData",
        "Roaming",
        ".minecraft",
        "mineger-cache"
    );

    private Path mods = Paths.get(
        System.getProperty("user.home"),
        "AppData",
        "Roaming",
        ".minecraft",
        "mods"
    );

    public void install(ModModule modModule, VoidCallback<ModFile> onFetchOne, VoidCallback<ModModule> onFinish) {
        createCacheFolderIfNotExists();
        createModsFolderIfNotExists();
        fetchModFilesFromModModule(modModule, onFetchOne);
        copyModLoaderFromCacheToModsFolder(modModule);
        onFinish.call(modModule);
    }

    private void createModsFolderIfNotExists() {
        try {
            FileUtils.deleteDirectory(mods.toFile());
            Files.createDirectories(mods);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createCacheFolderIfNotExists() {
        try {
            Files.createDirectories(cache);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchModFilesFromModModule(ModModule modModule, VoidCallback<ModFile> onFetchOne) {
        for (ModFile modFile : modModule.getModFiles()) {
            
            Path path = cache.resolve(modFile.getFileName());
            File file = path.toFile();
            
            if (!file.exists()) {
                fetchModFile(modFile, file);
            }
            onFetchOne.call(modFile);
        }
    }

    private void copyModLoaderFromCacheToModsFolder(ModModule modModule) {
        for (ModFile modFile : modModule.getModFiles()) {
            copyFileFromCacheToModsFolder(modFile);
        }
    }

    private void copyFileFromCacheToModsFolder(ModFile modFile) {
        try {
            Files.copy(
                cache.resolve(modFile.getFileName()),
                mods.resolve(modFile.getFileName())
            );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchModFile(ModFile modFile, File file) {
        try (
            ReadableByteChannel channel = Channels.newChannel(modFile.getURL().openStream());
            FileOutputStream stream = new FileOutputStream(file);
        ) {
            stream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
