package ma.youcode.lineperm.model;

public class FichierProtege {
    private int id;
    private final String nom;
    private final int proprietaireId;
    private String droitsProprio;
    private String droitsAutres;
    private String contenu;

    public FichierProtege(String nom, int proprietaireId) {
        this(0, nom, proprietaireId, "rwd", "---", "");
    }

    public FichierProtege(int id, String nom, int proprietaireId,
            String droitsProprio, String droitsAutres) {
        this(id, nom, proprietaireId, droitsProprio, droitsAutres, "");
    }

    public FichierProtege(int id, String nom, int proprietaireId,
            String droitsProprio, String droitsAutres, String contenu) {
        this.id = id;
        this.nom = nom;
        this.proprietaireId = proprietaireId;
        this.droitsProprio = droitsProprio;
        this.droitsAutres = droitsAutres;
        this.contenu = contenu == null ? "" : contenu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public int getProprietaireId() {
        return proprietaireId;
    }

    public String getDroitsProprio() {
        return droitsProprio;
    }

    public String getDroitsAutres() {
        return droitsAutres;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String c) {
        this.contenu = c == null ? "" : c;
    }

    public void setDroitsProprio(String d) {
        this.droitsProprio = d;
    }

    public void setDroitsAutres(String d) {
        this.droitsAutres = d;
    }

    public String droitsToString() {
        return droitsProprio + "|" + droitsAutres;
    }

    @Override
    public String toString() {
        return droitsToString() + " user#" + proprietaireId + " " + nom;
    }
}