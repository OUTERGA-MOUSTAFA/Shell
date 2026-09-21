package ma.youcode.lineperm.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import ma.youcode.lineperm.model.AccessLog;

public class LogService {

    private List<AccessLog> logs = new ArrayList<AccessLog>();
    private final Path logPath = Paths.get("src/main/resources/access.log");

    public LogService() {
        charger();
    }

    // le chargement une seul fois
    public void charger() {
        // Lire le fichier access.log et remplir la liste logs
        if (!Files.exists(logPath)) {
            System.out.println("(access.log introuvable — aucune analyse possible)");
            return;
        }
        try {
            List<String> lignes = Files.readAllLines(logPath);
            for (String ligne : lignes) {
                if (ligne.trim().isEmpty())
                    continue;
                String[] parts = ligne.split(";");
                if (parts.length != 6)
                    continue;

                logs.add(new AccessLog(
                        parts[0], parts[1], parts[2],
                        parts[3], parts[4], parts[5]));
            }
        } catch (IOException e) {
            System.err.println("Erreur chargement access.log : " + e.getMessage());
        }
    }

    // les fonctions de Menu

    // function 1
    public long TotalActions() {
        return logs.stream().count();
    }

    // function 2
    public long TotalRefuses() {

        return logs.stream().filter(log -> log.getResultat().equals("refuse")).count();
    }

    // function 3
    public List<String> Utilisateurs() {
        return logs.stream().map(log -> log.getUtilisateur()).distinct().toList();
    }

    // function 4
    public Map<String, Long> ActionsParUser() {

        // return logs.stream().forEach( item ->{
        // item.getUtilisateur();
        // item.getAction();
        // } ).toList();

        return logs.stream()
                .collect(Collectors.groupingBy(
                        log -> log.getUtilisateur(),
                        Collectors.counting()));
    }

    // function 5
    public List<Map.Entry<String, Long>> Top3Fichiers() {
        return logs.stream()
                .filter(l -> l.getAction().equalsIgnoreCase("LECTURE"))
                .collect(Collectors.groupingBy(
                        AccessLog::getFichier,
                        Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    // function 6
    public long RefusesParUtilisateur(String utilisateur) {
        return logs.stream()
                .filter(l -> l.getUtilisateur().equalsIgnoreCase(utilisateur))
                .filter(l -> l.getResultat().equalsIgnoreCase("REFUSE"))
                .count();
    }

    // function 7
    public Optional<Map.Entry<String, Long>> UtilisateurPlusActif() {
        return logs.stream()
                .collect(Collectors.groupingBy(
                        AccessLog::getUtilisateur,
                        Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue());
    }

    // function 8
    public Map<String, Long> RepartitionParAction() {
        return logs.stream()
                .collect(Collectors.groupingBy(
                        AccessLog::getAction,
                        Collectors.counting()));
    }
}
