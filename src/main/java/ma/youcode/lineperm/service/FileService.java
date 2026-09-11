package ma.youcode.lineperm.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import ma.youcode.lineperm.model.FichierProtege;

/**
 * FileService
 */
public class FileService {

    
    Map<String, FichierProtege> fichiers = new HashMap();

    //Le dossier "data/" sera créé au premier besoin avec Files.createDirectories().
    Path dataDir = Paths.get("data/");

    Path storagePath = Paths.get("files.txt");


    //  private final Map<String, FichierProtege> fichiers = new HashMap<>();
    // private final Path storagePath = Paths.get("files.txt");
    // private final Path dataDir = Paths.get("data");

    /** Crée un fichier vide en rwd|---. */
    public boolean touch(String nom, String proprietaire) {
        // Refus si vide ou contient un chemin
        if (nom == null || nom.trim().isEmpty()) return false;
        if (nom.contains("/") || nom.contains("\\")) return false;
        if (fichiers.containsKey(nom)) return false;

        FichierProtege f = new FichierProtege(nom, proprietaire);
        fichiers.put(nom, f);
        ecrireContenuDisque(nom, "");
        sauvegarder();
        return true;
    }
}