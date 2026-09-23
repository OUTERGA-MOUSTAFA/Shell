package ma.youcode.lineperm.access;

import ma.youcode.lineperm.model.FichierProtege;
import ma.youcode.lineperm.model.User;

/**
 * La règle des catégories.
 * Une seule catégorie s'applique, jamais les deux.
 */
public class ControlerAcces {

    private ControlerAcces() {
    }

    /**
     * Retourne true si l'utilisateur a le droit demandé.
     * 
     * @param droit 'r', 'w' ou 'd'
     */
    public static boolean estAutorise(User user, FichierProtege fichier, char droit) {
        if (user == null || fichier == null)
            return false;

        boolean estProprio = user.getId() == fichier.getProprietaireId();

        String bloc = estProprio ? fichier.getDroitsProprio() : fichier.getDroitsAutres();
        return bloc.contains(String.valueOf(droit));
    }

    public static boolean estProprietaire(User user, FichierProtege fichier) {
        if (user == null || fichier == null)
            return false;
        return user.getId() == fichier.getProprietaireId();
    }
}