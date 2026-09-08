package ma.youcode.lineperm.service;


class UserService {
	public static void main(String [] args){

	  public User connecter(String login, String password) {
        User user = users.get(login);
        if (user == null) return null; // login inconnu

        boolean check = BCrypt.checkpw(password, user.getPasswordHash());
        if (!check) return null; // mauvais password (même message que login inconnu)

        return user; // succès
    }

	}
}