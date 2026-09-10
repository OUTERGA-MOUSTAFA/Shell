package ma.youcode.lineperm.model;

public class FichierProtege {

    private String nom;
    private String Proprietaire;
    private boolean rProp, wProp, dProp;
    private boolean rAutre, wAutre, dAutre;

    public FichierProtege(String nom, String Proprietaire, boolean rProp, boolean wProp, boolean dProp, boolean rAutre,
            boolean wAutre, boolean dAutre) {
        this.nom = nom;
        this.Proprietaire = Proprietaire;

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getProprietaire() {
        return Proprietaire;
    }

    public void setProprietaire(String Proprietaire) {
        this.Proprietaire = Proprietaire;
    }

    public boolean isrProp() {
        return rProp;
    }

    public boolean iswProp() {
        return wProp;
    }

    public boolean isrdrop() {
        return dProp;
    }

    public boolean setrAutre(boolean v) {
        return this.rAutre = v;
    }

    public boolean setwAutre(boolean v) {
        return this.wAutre = v;
    }

    public boolean setdAutre(boolean v) {
        return this.dAutre = v;
    }
    
    // --- Format d'affichage : rwd|r--  ---
    public String droitsToString() {
        return (rProp ? "r" : "-") + (wProp ? "w" : "-") + (dProp ? "d" : "-")
                + "|"
                + (rAutre ? "r" : "-") + (wAutre ? "w" : "-") + (dAutre ? "d" : "-");
    }

    @Override
    public String toString() {
        return droitsToString() + " " + this.Proprietaire + " " + this.nom;
    }

}