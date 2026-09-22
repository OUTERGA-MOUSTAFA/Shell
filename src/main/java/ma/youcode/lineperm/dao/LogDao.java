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

    /** Un log d'audit est immuable : suppression interdite. */
    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException("Un log d'audit est immuable.");
    }


}
