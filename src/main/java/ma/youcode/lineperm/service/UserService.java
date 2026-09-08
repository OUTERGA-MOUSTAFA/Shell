package ma.youcode.lineperm.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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


	public boolean isLoggedIn(User currentUser) {
        return currentUser != null;
    }

}