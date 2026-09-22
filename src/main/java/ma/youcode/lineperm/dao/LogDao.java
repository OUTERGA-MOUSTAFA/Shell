package ma.youcode.lineperm.dao;

import  java.sql.*;
import java.util.*;

import ma.youcode.lineperm.model.AccessLog;
public class LogDao extends AbstractDao {

    @Override
    public AccessLog save(AccessLog l) {
        String sql = "INSERT INTO logs(utilisateur_id, fichier_id, action, resultat) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, l.getUtilisateurId());
            ps.setInt(2, l.getFichierId());
            ps.setString(3, l.getAction());
            ps.setString(4, l.getResultat());
            ps.executeUpdate();
            return l;
        } catch (SQLException e) {
            System.err.println("LogDao.save : " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<AccessLog> findById(int id) {
        String sql = "SELECT * FROM logs WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("LogDao.findById : " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<AccessLog> findAll() {
        List<AccessLog> list = new ArrayList<>();
        String sql = "SELECT * FROM logs ORDER BY date_action DESC";
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("LogDao.findAll : " + e.getMessage());
        }
        return list;
    }

    // Un log d'audit est immuable : suppression interdite. */
    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("Un log d'audit est immuable.");
    }




    // LES 8 ANALYSES (SQL pur)
    

    // 1. Nombre total d'actions */
    public long compterTotal() {
        return scalar("SELECT COUNT(*) FROM logs");
    }

    /** 2. Nombre d'accès refusés */
    public long compterRefuses() {
        return scalar("SELECT COUNT(*) FROM logs WHERE resultat = 'REFUSE'");
    }

    // 3. Utilisateurs distincts (ceux qui ont au moins 1 log) */
    public List<String> utilisateursDistincts() {
        List<String> list = new ArrayList<>();
        String sql = """
            SELECT DISTINCT u.login
            FROM users u
            JOIN logs l ON l.utilisateur_id = u.id
            ORDER BY u.login
        """;
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(rs.getString("login"));
        } catch (SQLException e) {
            System.err.println("LogDao.utilisateursDistincts : " + e.getMessage());
        }
        return list;
    }

    // 4. Actions par utilisateur (Map login -> count) */
    public Map<String, Long> actionsParUtilisateur() {
        Map<String, Long> map = new LinkedHashMap<>();
        String sql = """
            SELECT u.login, COUNT(*) AS n
            FROM users u
            JOIN logs l ON l.utilisateur_id = u.id
            GROUP BY u.login
            ORDER BY n DESC
        """;
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) map.put(rs.getString("login"), rs.getLong("n"));
        } catch (SQLException e) {
            System.err.println("LogDao.actionsParUtilisateur : " + e.getMessage());
        }
        return map;
    }

    // 5. Top 3 des fichiers consultés (LECTURE uniquement) */
    public List<Map.Entry<String, Long>> top3Fichiers() {
        List<Map.Entry<String, Long>> list = new ArrayList<>();
        String sql = """
            SELECT f.nom, COUNT(*) AS n
            FROM fichiers f
            JOIN logs l ON l.fichier_id = f.id
            WHERE l.action = 'LECTURE'
            GROUP BY f.nom
            ORDER BY n DESC
            LIMIT 3
        """;
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new AbstractMap.SimpleEntry<>(rs.getString("nom"), rs.getLong("n")));
            }
        } catch (SQLException e) {
            System.err.println("LogDao.top3Fichiers : " + e.getMessage());
        }
        return list;
    }

    // 6. Accès refusés d'un utilisateur donné *
    public long refusesParUtilisateur(String login) {
        String sql = """
            SELECT COUNT(*) FROM logs l
            JOIN users u ON u.id = l.utilisateur_id
            WHERE u.login = ? AND l.resultat = 'REFUSE'
        """;
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.err.println("LogDao.refusesParUtilisateur : " + e.getMessage());
        }
        return 0;
    }

    // 7. Utilisateur le plus actif *
    public Optional<Map.Entry<String, Long>> utilisateurPlusActif() {
        String sql = """
            SELECT u.login, COUNT(*) AS n
            FROM users u
            JOIN logs l ON l.utilisateur_id = u.id
            GROUP BY u.login
            ORDER BY n DESC
            LIMIT 1
        """;
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return Optional.of(new AbstractMap.SimpleEntry<>(
                        rs.getString("login"), rs.getLong("n")));
            }
        } catch (SQLException e) {
            System.err.println("LogDao.utilisateurPlusActif : " + e.getMessage());
        }
        return Optional.empty();
    }

    //8. Répartition des actions par type *
    public Map<String, Long> repartitionParAction() {
        Map<String, Long> map = new LinkedHashMap<>();
        String sql = "SELECT action, COUNT(*) AS n FROM logs GROUP BY action ORDER BY n DESC";
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) map.put(rs.getString("action"), rs.getLong("n"));
        } catch (SQLException e) {
            System.err.println("LogDao.repartitionParAction : " + e.getMessage());
        }
        return map;
    }

    
    // HELPERS
    private long scalar(String sql) {
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            System.err.println("LogDao.scalar : " + e.getMessage());
        }
        return 0;
    }

    private AccessLog mapRow(ResultSet rs) throws SQLException {
        return new AccessLog(
                rs.getInt("id"),
                rs.getInt("utilisateur_id"),
                rs.getInt("fichier_id"),
                rs.getString("action"),
                rs.getString("resultat"),
                rs.getString("date_action"));
    }



}
