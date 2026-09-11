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

    
}