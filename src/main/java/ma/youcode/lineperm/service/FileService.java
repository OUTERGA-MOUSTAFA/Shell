package ma.youcode.lineperm.service;

import ma.youcode.lineperm.dao.FichierDao;
import ma.youcode.lineperm.dao.LogDao;
import ma.youcode.lineperm.model.AccessLog;
import ma.youcode.lineperm.model.FichierProtege;
import ma.youcode.lineperm.model.User;

import java.util.List;
import java.util.Optional;

public class FileService {

    private final FichierDao fichierDao = new FichierDao();
    private final LogDao logDao = new LogDao();

    public boolean touch(String nom, User user) {
        if (nom == null || nom.trim().isEmpty())
            return false;
        if (nom.contains("/") || nom.contains("\\"))
            return false;
        if (fichierDao.findByNom(nom).isPresent())
            return false;

        FichierProtege f = new FichierProtege(nom, user.getId());
        FichierProtege saved = fichierDao.save(f);
        if (saved == null)
            return false;

        logDao.save(new AccessLog(user.getId(), saved.getId(), "ECRITURE", "OK"));
        return true;
    }

    public List<FichierProtege> lister() {
        return fichierDao.findAll();
    }

    public Optional<FichierProtege> find(String nom) {
        return fichierDao.findByNom(nom);
    }

    public boolean existe(String nom) {
        return fichierDao.findByNom(nom).isPresent();
    }

    public boolean peutLire(User u, FichierProtege f) {
        String bloc = (u.getId() == f.getProprietaireId()) ? f.getDroitsProprio() : f.getDroitsAutres();
        return bloc.contains("r");
    }

    public boolean peutEcrire(User u, FichierProtege f) {
        String bloc = (u.getId() == f.getProprietaireId()) ? f.getDroitsProprio() : f.getDroitsAutres();
        return bloc.contains("w");
    }

    public boolean estProprietaire(User u, FichierProtege f) {
        return u.getId() == f.getProprietaireId();
    }

    public boolean chmod(FichierProtege f, char droit, boolean retirer) {
        String d = f.getDroitsAutres();
        char[] c = d.toCharArray();
        int index = (droit == 'r') ? 0 : (droit == 'w') ? 1 : 2;
        c[index] = retirer ? '-' : droit;
        String nouveau = new String(c);
        f.setDroitsAutres(nouveau);
        return fichierDao.updateDroits(f.getId(), f.getDroitsProprio(), nouveau);
    }
}