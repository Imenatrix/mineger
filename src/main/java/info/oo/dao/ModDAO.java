package info.oo.dao;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import info.oo.dao.interfaces.IModDAO;
import info.oo.entities.Mod;
import info.oo.entities.ModFile;
import info.oo.entities.ModLoader;
import info.oo.entities.ModOrigin;
import info.oo.utils.clarice.Clarice;

public class ModDAO implements IModDAO {

    private ArrayList<Mod> cache;

    public ModDAO() {
        this.cache = new ArrayList<Mod>();
    }

    public Mod getById(int id) {
        
        Optional<Mod> optionalMod = cache.stream().filter(item -> item.getId() == id).findFirst();

        if (optionalMod.isPresent()) {
            return optionalMod.get();
        }

        String query = "select * from `mod` where id = ?";
        return Clarice.executeQueryOr(
            query,
            stmt -> stmt.setInt(1, id),
            result -> {
                Mod mod = resultToMod(result);
                cache.add(mod);
                return mod;
            },
            null
        );
    }

    public ArrayList<Mod> getAllByModFiles(ArrayList<ModFile> modFiles) {
        String wildcards = modFiles.stream()
            .map(item -> item.getMod().getId())
            .map(item -> "?")
            .collect(Collectors.joining(","));
        String query = "select * from `mod` where id in (" + wildcards + ")";
        Stream<Integer> ids = modFiles.stream().map(item -> item.getMod().getId());
        return Clarice.executeQueryOr(
            query,
            stmt -> Clarice.prepareIntegerIterator(stmt, ids.iterator()),
            result -> resultToModFileArrayList(result),
            new ArrayList<Mod>()
        );
    }

    private ArrayList<Mod> resultToModFileArrayList(ResultSet result) throws SQLException {
        ArrayList<Mod> mods = new ArrayList<Mod>();
        while (result.next()) {
            mods.add(parseModFromResult(result));
        }
        return mods;
    }

    private Mod resultToMod(ResultSet result) throws SQLException {
        result.next();
        return parseModFromResult(result);
    }

    private Mod parseModFromResult(ResultSet result) throws SQLException {
        try {
            return new Mod(
                result.getInt("id"),
                result.getString("name"),
                new URL(result.getString("url")),
                result.getString("summary"),
                new ModLoader(result.getInt("mod_loader_id")),
                new ModOrigin(result.getInt("mod_origin_id"))
            );
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
