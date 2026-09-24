package ma.youcode.lineperm.service;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

import ma.youcode.lineperm.dao.UserDao;
import ma.youcode.lineperm.model.User;

public class UserService {
    // private final Map<String, User> users = new HashMap<>();
    // private final Path storagePath = Paths.get("users.txt");

    private final UserDao userDao = new UserDao();


    public boolean existe(String login) {
        return userDao.findByLogin(login).isPresent();
    }

    public boolean creerCompte(String login, String password) {
        if (login == null || login.trim().isEmpty() || login.contains(":"))
            return false;
        if (password == null || password.isEmpty())
            return false;
        if (existe(login))
            return false;

        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        User u = new User(login, hash);
        return userDao.save(u) != null;
    }

    public User connecter(String login, String password) {
        Optional<User> opt = userDao.findByLogin(login);
        if (opt.isEmpty())
            return null;
        User u = opt.get();
        return BCrypt.checkpw(password, u.getPasswordHash()) ? u : null;
    }

}