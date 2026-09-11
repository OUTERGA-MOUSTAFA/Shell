package ma.youcode.lineperm.ui;

import java.util.List;
import java.util.Scanner;

import ma.youcode.lineperm.model.FichierProtege;
import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.FileService;
import ma.youcode.lineperm.service.UserService;

public class ConsoleApp {

    private final UserService userService = new UserService();
    private final FileService fileService = new FileService();
    private final Scanner scanner = new Scanner(System.in);
    private User utilisateurConnecte = null;
    private boolean running = true;

    public void demarrer() {
        System.out.println("=========================");
        System.out.println("LinPerm - gestion de fichiers & droits");
        System.out.println("=========================");

        while (running) {
            afficherPrompt();
            String ligne = scanner.nextLine().trim();
            traiter(ligne);
        }
        scanner.close();
    }


    
    // ============================================================
    // TRAITEMENT D'UNE LIGNE
    // ============================================================

    private void traiter(String ligne) {
        if (ligne.isEmpty()) return;

        String[] mots = ligne.split("\\s+");
        String commande = mots[0].toLowerCase();

        // GARDE 1 : commandes nécessitant une connexion
        if (utilisateurConnecte == null
                && (commande.equals("logout")
                 || commande.equals("ls")
                 || commande.equals("touch")
                 || commande.equals("cat")
                 || commande.equals("nano")
                 || commande.equals("chmod"))) {
            System.out.println("Vous devez être connecté pour cette commande.");
            return;
        }

        // GARDE 2 : déjà connecté → refuser signup/login
        if (utilisateurConnecte != null
                && (commande.equals("signup") || commande.equals("login"))) {
            System.out.println("Vous êtes déjà connecté. Faites 'logout' d'abord.");
            return;
        }

        switch (commande) {
            case "signup":  handleSignup();  break;
            case "login":   handleLogin();   break;
            case "logout":  handleLogout();  break;
            case "help":    showHelp();      break;
            case "ls":      handleLs();      break;
            case "touch":   handleTouch(mots); break;
            case "cat":     handleCat(mots);   break;
            case "nano":    handleNano(mots);  break;
            case "chmod":   handleChmod(mots); break;
            case "exit":
                System.out.println("Au revoir.");
                running = false;
                break;
            default:
                System.out.println("Commande inconnue. Tapez 'help'.");
                break;
        }
    }







    // Help
    private void showHelp() {
        if (utilisateurConnecte == null) {
            System.out.println("Commandes : signup | login | help | exit");
        } else {
            System.out.println("Commandes : logout | help | exit");
        }
    }

    // Afficher Prompt
    private void afficherPrompt() {
        if (utilisateurConnecte != null) {
            System.out.print(utilisateurConnecte.getLogin() + "@linperm>wrd| ");
        } else {
            System.out.print("linperm> ");
        }
    }

    // signUp
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

    // Login
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

    // logout
    private void handleLogout() {
        if (utilisateurConnecte == null) {
            System.out.println("Vous n'êtes pas connecté.");
            return;
        }
        utilisateurConnecte = null;
        System.out.println("Déconnecté.");
    }

    // touch
    private void handleTouch(String[] mots) {
        if (mots.length < 2) {
            System.out.println("Usage : touch <nom>");
            return;
        }
        String nom = mots[1];
        if (fileService.touch(nom, utilisateurConnecte.getLogin())) {
            System.out.println("Fichier '" + nom + "' créé.");
        } else {
            System.out.println("Permission denied.");
        }
    }

    // Ls
    private void handleLs() {
        List<FichierProtege> tous = fileService.lister();
        if (tous.isEmpty()) {
            System.out.println("(aucun fichier)");
            return;
        }
        for (FichierProtege f : tous) {
            System.out.println(f.toString());
        }
    }


    // Cat
    private void handleCat(String[] mots) {
        if (mots.length < 2) {
            System.out.println("Usage : cat <nom>");
            return;
        }
        String nom = mots[1];
        String contenu = fileService.cat(nom, utilisateurConnecte);
        if (contenu == null) {
            System.out.println("Permission denied.");
            return;
        }
        if (contenu.isEmpty()) {
            System.out.println("(fichier vide)");
        } else {
            System.out.print(contenu);
            if (!contenu.endsWith("\n")) System.out.println();
        }
    }


    // Nano
    private void handleNano(String[] mots) {
        if (mots.length < 2) {
            System.out.println("Usage : nano <nom>");
            return;
        }
        String nom = mots[1];

        if (!fileService.existe(nom)) {
            System.out.println("Fichier introuvable. Utilisez 'touch' d'abord.");
            return;
        }
        // Vérifie le droit w AVANT l'édition
        if (!fileService.peutEcrire(nom, utilisateurConnecte)) {
            System.out.println("Permission denied.");
            return;
        }

        System.out.println("--- Mode édition : " + nom + " ---");

        // Cas limite : w sans r → on masque le contenu actuel
        if (!fileService.peutLire(nom, utilisateurConnecte)) {
            System.out.println("(contenu masqué — vous n'avez pas le droit de lecture)");
        } else {
            String contenuActuel = fileService.cat(nom, utilisateurConnecte);
            if (contenuActuel == null || contenuActuel.isEmpty()) {
                System.out.println("(fichier vide)");
            } else {
                System.out.print(contenuActuel);
                if (!contenuActuel.endsWith("\n")) System.out.println();
            }
        }

        System.out.println("--- Saisis ton texte. Tape EOF seul sur une ligne pour enregistrer. ---");

        StringBuilder sb = new StringBuilder();
        int nbLignes = 0;
        while (true) {
            String l = scanner.nextLine();
            if (l.equals("EOF")) break;
            sb.append(l).append("\n");
            nbLignes++;
        }

        fileService.nano(nom, sb.toString(), utilisateurConnecte);
        System.out.println("Fichier '" + nom + "' enregistré (" + nbLignes + " ligne(s)).");
    }
}
