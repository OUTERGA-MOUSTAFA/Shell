package ma.youcode.lineperm.ui;
import ma.youcode.lineperm.service.UserService;
import java.util.Scanner;

class ConsoleApp{
	
	//private UserService userService = new UserService();
    private Scanner scanner = new Scanner(System.in);
    //private User utilisateurConnecte = null;

    public void demarrer() {
        System.out.println("=========================");
        System.out.println("LinPerm - gestion de fichiers & droits");
        System.out.println("=========================");

        String commande;
        do {
           
            String[] mots = ligne.split("\\s+");
            commande = mots[0].toLowerCase();

            switch (commande) {
                case "signup":
                    handleSignup();
                    break;
                default:
                    System.out.println("Commande inconnue. Tapez 'help'.");
                    break;
            }
        } while (!"exit".equals(commande));
        scanner.close();

        // hundels exeptions
        private void handleSignup() {
        System.out.print("Login : ");
        String login = scanner.nextLine().trim();
        if (login.isEmpty() || login.contains(":")) {
            System.out.println("Login invalide (ne doit pas contenir ':').");
            return;
        }
        if (userService.existe(login)) {
            System.out.println("Ce login existe déjà.");
            return;
        }

        System.out.print("Mot de passe : ");
        String password = scanner.nextLine();
        if (password.isEmpty()) {
            System.out.println("Mot de passe invalide.");
            return;
        }

        boolean success = userService.creerCompte(login, password);
        if (success) {
            System.out.println("Compte créé avec succès.");
        } else {
            System.out.println("Erreur lors de la création.");
        }
        
    }
}