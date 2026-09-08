package ma.youcode.lineperm.ui;

import java.util.Scanner;

import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.UserService;

public class ConsoleApp {

    private UserService userService = new UserService();
    private Scanner scanner = new Scanner(System.in);
    private User utilisateurConnecte = null;

    public void demarrer() {
        System.out.println("=========================");
        System.out.println("LinPerm - gestion de fichiers & droits");
        System.out.println("=========================");

        String commande;
        while (true) {

            String ligne = scanner.nextLine().trim();
            if (ligne.isEmpty())
                continue;

            String[] mots = ligne.split("\\s+");
            commande = mots[0].toLowerCase();

            // GARDE 1 : Commande qui nécessite connexion (ex: logout)
            if (utilisateurConnecte == null && (commande.equals("logout") || commande.equals("ls") || commande.equals("touch"))) {
                System.out.println("Vous devez être connecté pour cette commande.");
                continue;
            }

             // GARDE 2 : Déjà connecté, on refuse signup/login
            if (utilisateurConnecte != null && (commande.equals("signup") || commande.equals("login"))) {
                System.out.println("Vous êtes déjà connecté. Faites 'logout' d'abord.");
                continue;
            }

            switch (commande) {
                case "signup":
                    handleSignup();;
                    break;
                case "login":
                    handleLogin();
                    break;
                case "logout":
                    handleLogout();
                    break;
                 case "exit":
                    System.out.println("Au revoir.");
                    break;
                default:
                    System.out.println("Commande inconnue. Tapez 'help'.");
                    break;
            }
        } ;
        scanner.close();
    }

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

    private void handleLogin() {
        System.out.print("Login : ");
        String login = scanner.nextLine().trim();
        System.out.print("Mot de passe : ");
        String password = scanner.nextLine();

        User user = userService.connecter(login, password);
        if (user == null) {
            System.out.println("Identifiants incorrects."); // Message UNIQUE !
        } else {
            utilisateurConnecte = user;
            System.out.println("Bienvenue " + login + " !");
        }
    }

    private void handleLogout() {
        if (utilisateurConnecte == null) {
            System.out.println("Vous n'êtes pas connecté.");
            return;
        }
        utilisateurConnecte = null;
        System.out.println("Déconnecté.");
    }
}
