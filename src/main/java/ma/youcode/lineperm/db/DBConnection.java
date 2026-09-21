package ma.youcode.lineperm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;
    private static final String URL = "jdbc:sqlite:auditdb.db";// SQLite crée automatiquement le fichier auditdb.db s'il n'existe pas

    private DBConnection(){

        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(URL);
            try (Statement st = connection.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON");
            }
            initSchema();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Impossible de se connecter à SQLite : " + e.getMessage(), e);
        }

    }


    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    // ouvrire la connexion à la bd
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                try (Statement st = connection.createStatement()) {
                    st.execute("PRAGMA foreign_keys = ON");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Connexion perdue : " + e.getMessage(), e);
        }
        return connection;
    }

    /** Crée les tables si elles n'existent pas (premier lancement). */
    private void initSchema() {
        String users = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                login TEXT UNIQUE NOT NULL,
                password_hash TEXT NOT NULL
            )
        """;

        String fichiers = """
            CREATE TABLE IF NOT EXISTS fichiers (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                proprietaire_id INTEGER NOT NULL,
                droits_proprio TEXT NOT NULL DEFAULT 'rwd',
                droits_autres  TEXT NOT NULL DEFAULT '---',
                FOREIGN KEY (proprietaire_id) REFERENCES users(id)
            )
        """;

        String logs = """
            CREATE TABLE IF NOT EXISTS logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                utilisateur_id INTEGER NOT NULL,
                fichier_id INTEGER NOT NULL,
                action TEXT NOT NULL,
                resultat TEXT NOT NULL,
                date_action TEXT NOT NULL DEFAULT (datetime('now')),
                FOREIGN KEY (utilisateur_id) REFERENCES users(id),
                FOREIGN KEY (fichier_id)     REFERENCES fichiers(id)
            )
        """;

        try (Statement st = connection.createStatement()) {
            st.execute(users);
            st.execute(fichiers);
            st.execute(logs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur création schéma : " + e.getMessage(), e);
        }
    }
}
