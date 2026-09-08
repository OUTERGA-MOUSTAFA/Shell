package ma.youcode.lineperm.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import ma.youcode.lineperm.model.User;

class UserService {
	private final Map<String, User> users = new HashMap<>();
    private final Path storagePath = Paths.get("users.txt");


	public boolean existe(String login) {
        return users.containsKey(login);
    }

	public static void main(String [] args){

	  public User connecter(String login, String password) {
        User user = users.get(login);
        if (user == null) return null; // login inconnu

        boolean check = BCrypt.checkpw(password, user.getPasswordHash());
        if (!check) return null; // mauvais password (même message que login inconnu)

        return user; // succès
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


	public boolean isLoggedIn(User currentUser) {
        return currentUser != null;
    }

}