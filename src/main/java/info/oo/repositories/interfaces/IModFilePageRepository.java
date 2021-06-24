package info.oo.repositories.interfaces;

import java.util.ArrayList;

import info.oo.entities.ModFile;

public interface IModFilePageRepository {
    public int getTotalPages(int limit, Integer modLoaderId, Integer modOriginId, String minecraftVersion, String search);
    public ArrayList<ModFile> getPage(int limit, int page, Integer modLoaderId, Integer modOriginId, String minecraftVersion, String search);
}
