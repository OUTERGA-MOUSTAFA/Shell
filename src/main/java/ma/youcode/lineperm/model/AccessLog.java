package ma.youcode.lineperm.model;

/**
 * AccessLog
 */
public class AccessLog {

     
    private final String date;
    private final String heure;
    private final String utilisateur;
    private final String action;   // LECTURE, ECRITURE, SUPPRESSION
    private final String fichier;
    private final String resultat; // true => accept || false => refusé

    public AccessLog(String date, String heure, String utilisateur, String action, String fichie,
        String resultat
    ){
        this.date = date;
        this.heure = heure;
        this.utilisateur = utilisateur;
        this.action = action;
        this.fichier = fichie;
        this.resultat = resultat;
    }

     public String getDate() { return date; }
    public String getHeure() { return heure; }
    public String getUtilisateur() { return utilisateur; }
    public String getAction() { return action; }
    public String getFichier() { return fichier; }
    public String getResultat() { return resultat; }

    @Override
    public String toString() {
        return date + ";" + heure + ";" + utilisateur + ";" +
               action + ";" + fichier + ";" + resultat;
    }
}