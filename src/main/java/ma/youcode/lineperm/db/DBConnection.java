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
}
