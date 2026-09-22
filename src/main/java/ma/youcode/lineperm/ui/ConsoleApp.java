package ma.youcode.lineperm.ui;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import ma.youcode.lineperm.model.FichierProtege;
import ma.youcode.lineperm.model.User;
import ma.youcode.lineperm.service.FileService;
import ma.youcode.lineperm.service.LogService;
import ma.youcode.lineperm.service.UserService;

public class ConsoleApp {

    private final UserService userService = new UserService();// charger() -> lit users.txt
    private final FileService fileService = new FileService();// charger() -> lit files.txt
    private final Scanner scanner = new Scanner(System.in);

    private User utilisateurConnecte = null; // non connecté au debut
    private boolean running = true;

    public void demarrer() {
        System.out.println("=======================================");
        System.out.println("LinePerm - gestion de fichiers & droits");
        System.out.println("=======================================");

        while (running) {// afficherPrompt()=> linperm> username@wrd|
            afficherPrompt();// prompt devient "username@linperm>wrd"
            String ligne = scanner.nextLine().trim();
            traiter(ligne);// traiter(ligne)
        }
        scanner.close();
    }

    // ============================================================
    // TRAITEMENT D'UNE LIGNE
    // ============================================================

    private void traiter(String ligne) {
        if (ligne.isEmpty())
            return;

        String[] mots = ligne.split("\\s+");
        String commande = mots[0].toLowerCase();

        // GARDE 1 : commandes nécessitant une connexion
        if (utilisateurConnecte == null // pas connecté
                && (commande.equals("logout") || commande.equals("ls") || commande.equals("touch")
                        || commande.equals("cat") || commande.equals("nano") || commande.equals("chmod")
                        || commande.equals("stats"))) {
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
            case "signup":
                handleSignup();
                break;
            case "login":
                handleLogin();
                break;
            case "logout":
                handleLogout();
                break;
            case "help":
                showHelp();
                break;
            case "ls":
                handleLs();
                break;
            case "touch":
                handleTouch(mots);
                break;
            case "cat":
                handleCat(mots);
                break;
            case "nano":
                handleNano(mots);
                break;
            case "chmod":
                handleChmod(mots);
                break;
            case "stats":
                handleStats();
                break;
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
            System.out.println("Commandes : logout | help | exit | cat | nano | chmod | touch | stats");
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
        System.out.println("Déconnecté.");// prompt redevient "linperm>"
    }

    // touch
    private void handleTouch(String[] mots) {
        if (mots.length < 2) {
            System.out.println("Usage : touch <nom>");
            return;
        }
        String nom = mots[1];
        if (fileService.touch(nom, utilisateurConnecte)) {
            System.out.println("Fichier '" + nom + "' créé.");
        } else {
            System.out.println("Permission denied.");
        }
    }

    // Ls
    private void handleLs() {
        var liste = fileService.lister();
        if (liste.isEmpty()) {
            System.out.println("(aucun fichier)");
            return;
        }
        for (var f : liste)
            System.out.println(f.toString());
    }

    // Cat
    private void handleCat(String[] mots) {
        if (mots.length < 2) {
            System.out.println("Usage : cat <nom>");
            return;
        }
        String nom = mots[1];
        var opt = fileService.find(nom);
        if (opt.isEmpty()) {
            System.out.println("Fichier introuvable.");
            return;
        }
        var f = opt.get();
        if (!fileService.peutLire(utilisateurConnecte, f)) {
            logService.enregistrer(utilisateurConnecte.getId(), f.getId(), "LECTURE", "REFUSE");
            System.out.println("Permission denied.");
            return;
        }
        logService.enregistrer(utilisateurConnecte.getId(), f.getId(), "LECTURE", "OK");
        System.out.println("(contenu du fichier — non stocké en BDD dans cette version)");
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
                if (!contenuActuel.endsWith("\n"))
                    System.out.println();
            }
        }

        System.out.println("--- Saisis ton texte. Tape EOF seul sur une ligne pour enregistrer. ---");

        StringBuilder sb = new StringBuilder();
        int nbLignes = 0;
        while (true) {
            String l = scanner.nextLine();
            if (l.equals("EOF"))
                break;
            sb.append(l).append("\n");
            nbLignes++;
        }

        fileService.nano(nom, sb.toString(), utilisateurConnecte);
        System.out.println("Fichier '" + nom + "' enregistré (" + nbLignes + " ligne(s)).");
    }

    // Chmod
    private void handleChmod(String[] mots) {
        if (mots.length < 3) {
            System.out.println("Usage : chmod <r|w|d|-r|-w|-d> <nom>");
            return;
        }
        String arg = mots[1];
        String nom = mots[2];

        var opt = fileService.find(nom);
        if (opt.isEmpty()) {
            System.out.println("Fichier introuvable.");
            return;
        }
        var f = opt.get();

        if (!fileService.estProprietaire(utilisateurConnecte, f)) {
            System.out.println("Permission denied.");
            return;
        }
        boolean retirer = arg.startsWith("-");
        String c = retirer ? arg.substring(1) : arg;
        if (c.length() != 1) {
            System.out.println("Argument invalide.");
            return;
        }
        char droit = c.charAt(0);
        if (droit != 'r' && droit != 'w' && droit != 'd') {
            System.out.println("Droit invalide (r, w ou d).");
            return;
        }
        String avant = f.droitsToString();
        fileService.chmod(f, droit, retirer);
        System.out.println(nom + " : " + avant + " --> " + f.droitsToString());
    }

    // Stats
    public void handleStats() {

        System.out.println("Bienvenue dans LogAnalyzer. Choisissez une statistique par son numéro.");
        boolean dansMenu = true;
        while (dansMenu) {
            afficherMenu();
            System.out.print("Choix : ");
            String choix = scanner.nextLine().trim();
            switch (choix) {
                case "1":
                    System.out.println("Nombre total d'actions : " + logService.totalActions());
                    break;
                case "2":
                    System.out.println("Accès refusés : " + logService.totalRefuses());
                    break;
                case "3":
                    System.out.println("Utilisateurs distincts : " + logService.utilisateursDistincts());
                    break;
                case "4":
                    System.out.println("Actions par utilisateur : " + logService.actionsParUtilisateur());
                    break;
                case "5":
                    System.out.println("Top 3 des fichiers consultés :");
                    logService.top3Fichiers()
                            .forEach(e -> System.out.println("  " + e.getKey() + " (" + e.getValue() + " lectures)"));
                    break;
                case "6":
                    System.out.print("Nom de l'utilisateur : ");
                    String u = scanner.nextLine().trim();
                    System.out.println("Accès refusés pour " + u + " : " + logService.refusesParUtilisateur(u));
                    break;
                case "7":
                    var opt = logService.utilisateurPlusActif();
                    if (opt.isPresent())
                        System.out.println("Utilisateur le plus actif : " + opt.get().getKey() + " ("
                                + opt.get().getValue() + " actions)");
                    else
                        System.out.println("(aucun log)");
                    break;
                case "8":
                    System.out.println("Répartition des actions par type : " + logService.repartitionParAction());
                    break;
                case "0":
                    dansMenu = false;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    // Menu
    public static void afficherMenu() {
        System.out.println("\nBienvenue dans LogAnalyzer. Choisissez une statistique par son numéro.");
        System.out.println("======   | LogAnalyzer |   ======");
        System.out.println("1) Nombre total d'actions");
        System.out.println("2) Nombre d'accès refusés");
        System.out.println("3) Utilisateurs distincts");
        System.out.println("4) Actions par utilisateur");
        System.out.println("5) Top 3 des fichiers consultés");
        System.out.println("6) Accès refusés d'un utilisateur");
        System.out.println("7) Utilisateur le plus actif");
        System.out.println("8) Répartition des actions par type");
        System.out.println("0) Quitter");
        System.out.print("Choix : ");
    }

    private final LogService logService = new LogService();

    public void totalActions() {
        System.out.println("Nombre total d'actions : " + logService.TotalActions());
    }

    private void totalRefuses() {
        System.out.println("Accès refusés : " + logService.TotalRefuses());
    }

    private void utilisateurs() {
        System.out.println("Accès refusés : " + logService.Utilisateurs());
    }

    private void actionsParUser() {
        System.out.println("Actions par utilisateur : " + logService.ActionsParUser());
    }

    private void top3() {
        List<Map.Entry<String, Long>> top3 = logService.Top3Fichiers();
        if (top3.isEmpty()) {
            System.out.println("(aucune lecture enregistrée)");
            return;
        }
        System.out.println("Top 3 des fichiers consultés :");
        int rang = 1;
        for (Map.Entry<String, Long> e : top3) {
            System.out.println("  " + rang + ". " + e.getKey() + " (" + e.getValue() + " lectures)");
            rang++;
        }
    }

    private void refusesUser() {
        System.out.print("Nom de l'utilisateur : ");
        String u = scanner.nextLine().trim();
        long n = logService.RefusesParUtilisateur(u);
        System.out.println("Accès refusés pour " + u + " : " + n);
    }

    private void plusActif() {
        Optional<Map.Entry<String, Long>> opt = logService.UtilisateurPlusActif();
        if (opt.isPresent()) {
            Map.Entry<String, Long> e = opt.get();
            System.out.println("Utilisateur le plus actif : " + e.getKey() + " (" + e.getValue() + " actions)");
        } else {
            System.out.println("(aucun log)");
        }
    }

    private void repartition() {
        System.out.println("Répartition des actions par type : " + logService.RepartitionParAction());
    }
}
