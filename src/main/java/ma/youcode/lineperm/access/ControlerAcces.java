package ma.youcode.lineperm.access;

import ma.youcode.lineperm.model.FichierProtege;
import ma.youcode.lineperm.model.User;

/**
 * Controlercces
 */
public class ControlerAcces {

    private ControlerAcces() { }
    /**
     * Retourne true si l'utilisateur a le droit demandé sur le fichier.
     * @param droit 'r', 'w' ou 'd'
     */
    public static boolean estAutorise(User user, FichierProtege fichier, char droit){
        if (user == null || fichier == null) {
            return false;
        }

        boolean estProprio = user.getLogin().equals(fichier.getProprietaire());

        if (estProprio) {
            // Bloc de gauche uniquement
            switch (droit) {
                case 'r': return fichier.isrProp();
                case 'w': return fichier.iswProp();
                case 'd': return fichier.isdProp();
                default:  return false;
            }
        } else {
            // Bloc de droite uniquement
            switch (droit) {
                case 'r': return fichier.isrAutre();
                case 'w': return fichier.iswAutre();
                case 'd': return fichier.isdAutre();
                default:  return false;
            }
        }

    }

    // Method static  qui compare les logins.
    public static boolean estProprietaire(User user, FichierProtege fichier) {
        if (user == null || fichier == null) return false;
        return user.getLogin().equals(fichier.getProprietaire()); //Utilisée par chmod pour vérifier que seul le propriétaire peut modifier les droits.
    }
}