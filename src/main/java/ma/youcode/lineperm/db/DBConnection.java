package ma.youcode.lineperm.db;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;
// import java.sql.Statement;
// import org.sqlite.SQLiteDataSource;

// public class DBConnection {
//     private static DBConnection instance;
//     // private Connection connection;
//     private final SQLiteDataSource dataSource;

//     private static final String URL = "jdbc:sqlite:auditdb.db";// SQLite crée automatiquement le fichier auditdb.db s'il
//                                                                // n'existe pas

//     private DBConnection() {

//         // Créer le DataSource une seule fois
//         dataSource = new SQLiteDataSource();
//         dataSource.setUrl(URL);

//         initSchema();
//         // try {
//         // Class.forName("org.sqlite.JDBC");
//         // this.connection = DriverManager.getConnection(URL);
//         // try (Statement st = connection.createStatement()) {
//         // st.execute("PRAGMA foreign_keys = ON");
//         // }
//         // initSchema();
//         // } catch (ClassNotFoundException | SQLException e) {
//         // throw new RuntimeException("Impossible de se connecter à SQLite : " +
//         // e.getMessage(), e);
//         // }

//     }

//     public static synchronized DBConnection getInstance() {
//         if (instance == null) {
//             instance = new DBConnection();
//         }
//         return instance;
//     }

// ouvrire la connexion à la bd
// public Connection getConnection() {
//     try {
//         // if (connection == null || connection.isClosed()) {
//         // connection = DriverManager.getConnection(URL);
//         // try (Statement st = connection.createStatement()) {
//         // st.execute("PRAGMA foreign_keys = ON");
//         // }
//         // }

/**
  * Retourne une connexion.
  * SQLiteDataSource gère la création/reconnexion otomatikman.
  */
//         return dataSource.getConnection();
//     } catch (SQLException e) {
//         throw new RuntimeException("Connexion perdue : " + e.getMessage(), e);
//     }
// }

/** Crée les tables si elles n'existent pas (premier lancement). */
// private void initSchema() {
//     String users = """
//                 CREATE TABLE IF NOT EXISTS users (
//                     id INTEGER PRIMARY KEY AUTOINCREMENT,
//                     login TEXT UNIQUE NOT NULL,
//                     password_hash TEXT NOT NULL
//                 )
//             """;

//     String fichiers = """
//                 CREATE TABLE IF NOT EXISTS fichiers (
//                     id INTEGER PRIMARY KEY AUTOINCREMENT,
//                     nom TEXT NOT NULL,
//                     proprietaire_id INTEGER NOT NULL,
//                     droits_proprio TEXT NOT NULL DEFAULT 'rwd',
//                     droits_autres  TEXT NOT NULL DEFAULT '---',
//                     contenu TEXT DEFAULT '',
//                     FOREIGN KEY (proprietaire_id) REFERENCES users(id)
//                 )
//             """;

//     String logs = """
//                 CREATE TABLE IF NOT EXISTS logs (
//                     id INTEGER PRIMARY KEY AUTOINCREMENT,
//                     utilisateur_id INTEGER NOT NULL,
//                     fichier_id INTEGER NOT NULL,
//                     action TEXT NOT NULL,
//                     resultat TEXT NOT NULL,
//                     date_action TEXT NOT NULL DEFAULT (datetime('now')),
//                     FOREIGN KEY (utilisateur_id) REFERENCES users(id),
//                     FOREIGN KEY (fichier_id)     REFERENCES fichiers(id)
//                 )
//             """;

//     try (Connection conn = dataSource.getConnection(); Statement st = conn.createStatement()) {
//         st.execute(users);
//         st.execute(fichiers);
//         st.execute(logs);
//     } catch (SQLException e) {
//         throw new RuntimeException("Erreur création schéma : " + e.getMessage(), e);
//     }
// }

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static DBConnection instance;
    private final HikariDataSource dataSource;

    private static final String URL = "jdbc:sqlite:auditdb.db";

    private DBConnection() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setDriverClassName("org.sqlite.JDBC");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(30_000);
        config.setIdleTimeout(600_000);
        config.setMaxLifetime(1_800_000);
        config.setPoolName("AuditDB-Pool");

        this.dataSource = new HikariDataSource(config);

        initSchema();
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Impossible d'obtenir une connexion : " +
                    e.getMessage(), e);
        }
    }

    /** Ferme le pool (à appeler à la fin de l'application). */
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

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
                droits_autres TEXT NOT NULL DEFAULT '---',
                contenu TEXT DEFAULT '',
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
                FOREIGN KEY (fichier_id) REFERENCES fichiers(id)
                )
                """;

        try (Connection conn = dataSource.getConnection();
                Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
            st.execute(users);
            st.execute(fichiers);
            st.execute(logs);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur création schéma : " + e.getMessage(), e);
        }
    }
}
