package ma.youcode.lineperm.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import ma.youcode.lineperm.dao.LogDao;
import ma.youcode.lineperm.model.AccessLog;

public class LogService {

    private final LogDao logDao = new LogDao();

    public void enregistrer(int userId, int fichierId, String action, String resultat) {
        logDao.save(new AccessLog(userId, fichierId, action, resultat));
    }

    public long totalActions() {
        return logDao.compterTotal();
    }

    public long totalRefuses() {
        return logDao.compterRefuses();
    }

    public List<String> utilisateursDistincts() {
        return logDao.utilisateursDistincts();
    }

    public Map<String, Long> actionsParUtilisateur() {
        return logDao.actionsParUtilisateur();
    }

    public List<Map.Entry<String, Long>> top3Fichiers() {
        return logDao.top3Fichiers();
    }

    public long refusesParUtilisateur(String u) {
        return logDao.refusesParUtilisateur(u);
    }

    public Optional<Map.Entry<String, Long>> utilisateurPlusActif() {
        return logDao.utilisateurPlusActif();
    }

    public Map<String, Long> repartitionParAction() {
        return logDao.repartitionParAction();
    }
}
