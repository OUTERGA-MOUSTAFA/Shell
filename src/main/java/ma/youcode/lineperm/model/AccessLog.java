package ma.youcode.lineperm.model;

/**
 * AccessLog
 */
public class AccessLog {

    private int id;
    private final int utilisateurId;
    private final int fichierId;
    private final String dateAction;
    // private final String utilisateur;
    private final String action; // LECTURE, ECRITURE, SUPPRESSION
    // private final String fichier;
    private final String resultat; // true => accept || false => refusé

    public AccessLog(int utilisateurId, int fichierId, String action, String resultat){
        this(0, utilisateurId, fichierId, action, resultat, null);
    }
    public AccessLog(int id, int utilisateurId, int fichierId,
                     String action, String resultat, String dateAction) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.fichierId = fichierId;
        this.action = action;
        this.dateAction = dateAction;
        this.resultat = resultat;
    }

    public int getId() { return id; }
    public int getUtilisateurId() { return utilisateurId; }
    public int getFichierId() { return fichierId; }
    public String getAction() { return action; }
    public String getResultat() { return resultat; }
    public String getDateAction() { return dateAction; }

    @Override
    public String toString() {
        return "Fichier Id :" + this.getFichierId() + "; Utilisateur Id: " + getUtilisateurId() + "; action: " + this.getDateAction() + ";" +
                 "Date action: " + this.getDateAction() + ";" + "resultat: " + this.getResultat();
    }
}