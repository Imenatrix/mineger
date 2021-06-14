package info.oo.services.interfaces;

import info.oo.entities.ModFile;
import info.oo.entities.ModModule;
import info.oo.utils.VoidCallback;

public interface IModModuleInstaller {
    
    public void install(ModModule modModule, VoidCallback<ModFile> onFetchOne, VoidCallback<ModFile> onFinish);

}
