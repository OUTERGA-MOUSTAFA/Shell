package ma.youcode.lineperm.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ma.youcode.lineperm.access.ControlerAcces;
import ma.youcode.lineperm.model.FichierProtege;
import ma.youcode.lineperm.model.User;

/**
 * FileService
 */
public class FileService {

    public FileService(){
        charger();
    }    
    Map<String, FichierProtege> fichiers = new HashMap();

    //Le dossier "data/" sera créé au premier besoin avec Files.createDirectories().
    Path dataDir = Paths.get("data/");

    Path storagePath = Paths.get("files.txt");


    //  private final Map<String, FichierProtege> fichiers = new HashMap<>();
    // private final Path storagePath = Paths.get("files.txt");
    // private final Path dataDir = Paths.get("data");

     private void charger() {
        if (!Files.exists(storagePath)) {
            return; // premier lancement
        }
        try {
            List<String> lines = Files.readAllLines(storagePath);
            for (String line : lines) {
                // format : nom:proprietaire;rwd;r---
                String[] parts = line.split(":", 2);
                if (parts.length != 2) continue;

                String nom = parts[0];
                String[] meta = parts[1].split(";");
                if (meta.length != 3) continue;

                String proprietaire = meta[0];
                String droitsProp = meta[1];  // ex: "rwd"
                String droitsAutre = meta[2]; // ex: "r--"

                FichierProtege f = new FichierProtege(
                        nom, proprietaire,
                        droitsProp.contains("r"),
                        droitsProp.contains("w"),
                        droitsProp.contains("d"),
                        droitsAutre.contains("r"),
                        droitsAutre.contains("w"),
                        droitsAutre.contains("d")
                );
                fichiers.put(nom, f);
            }
        } catch (IOException e) {
            System.err.println("Erreur chargement fichiers : " + e.getMessage());
        }
    }

    // COMMANDES
    public boolean existe(String nom) {
        return fichiers.containsKey(nom);
    }

    // Crée un fichier vide en rwd|---.
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

    /**
     * Lecture : retourne null si refusé ou inexistant.
     * Chaîne vide = fichier vide (cas légitime).
     */
    public String cat(String nom, User user) {
        FichierProtege f = fichiers.get(nom);
        if (f == null) return null;
        if (!ControlerAcces.estAutorise(user, f, 'r')) return null;
        return lireContenuDisque(nom);
    }


    public  List<FichierProtege> lister(){
        
        // return fichiers.values().stream().toList();
        return new ArrayList<>(fichiers.values());
    }

    /** Retourne true si l'utilisateur peut voir le contenu (droit r). */
    public boolean peutLire(String nom, User user) {
        FichierProtege f = fichiers.get(nom);
        if (f == null) return false;
        return ControlerAcces.estAutorise(user, f, 'r');
    }
}