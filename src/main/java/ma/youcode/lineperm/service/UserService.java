package ma.youcode.lineperm.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import ma.youcode.lineperm.model.User;

public class UserService {
	private final Map<String, User> users = new HashMap<>();
    private final Path storagePath = Paths.get("users.txt");

	public UserService() {
        loadUsers(); // t9ra les comptes mn l'fichier kayn ula la
    }


	public boolean existe(String login) {
        return users.containsKey(login);
    }

	private void loadUsers() {
        if (!Files.exists(storagePath)) {
            return; // Premier lancement, mafihach erreur
        }
        try {
            List<String> lines = Files.readAllLines(storagePath);
            for (String line : lines) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    User user = new User(parts[0], parts[1]);
                    users.put(user.getLogin(), user);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur chargement comptes : " + e.getMessage());
        }
    }
	
	

	public boolean creerCompte(String login, String password) {
        if (login == null || login.trim().isEmpty() || login.contains(":")) return false;
        if (password == null || password.isEmpty()) return false;
        if (existe(login)) return false;

        String salt = BCrypt.gensalt();
        String hashed = BCrypt.hashpw(password, salt);

        User newUser = new User(login, hashed);
        users.put(login, newUser);
        saveUsers();
        return true;
    }


	 private void saveUsers() {
        StringBuilder content = new StringBuilder();
        for (User user : users.values()) {
            content.append(user.toString()).append(System.lineSeparator());
        }
        try {
            Files.writeString(storagePath, content.toString());
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde comptes : " + e.getMessage());
        }
    }


	public boolean isLoggedIn(User currentUser) {
        return currentUser != null;
    }

}