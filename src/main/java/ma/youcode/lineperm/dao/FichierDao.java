package ma.youcode.lineperm.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ma.youcode.lineperm.model.FichierProtege;

public class FichierDao extends AbstractDao<FichierProtege> {

    @Override
    public FichierProtege save(FichierProtege f) {
        String sql = "INSERT INTO fichiers(nom, proprietaire_id, droits_proprio, droits_autres, contenu) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, f.getNom());
            ps.setInt(2, f.getProprietaireId());
            ps.setString(3, f.getDroitsProprio());
            ps.setString(4, f.getDroitsAutres());
            ps.setString(5, f.getContenu());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    f.setId(rs.getInt(1));
            }
            return f;
        } catch (SQLException e) {
            System.err.println("FichierDao.save : " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<FichierProtege> findById(int id) {
        String sql = "SELECT * FROM fichiers WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("FichierDao.findById : " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<FichierProtege> findAll() {
        List<FichierProtege> list = new ArrayList<>();
        String sql = "SELECT * FROM fichiers";
        try (PreparedStatement ps = getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("FichierDao.findAll : " + e.getMessage());
        }
        return list;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM fichiers WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("FichierDao.delete : " + e.getMessage());
            return false;
        }
    }

    private FichierProtege mapRow(ResultSet rs) throws SQLException {
        
         FichierProtege f = new FichierProtege(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getInt("proprietaire_id"),
                rs.getString("droits_proprio"),
                rs.getString("droits_autres") );
        f.setContenu(rs.getString("contenu"));
        return f;
    }

    public boolean updateDroits(int id, String droitsProprio, String droitsAutres) {
        String sql = "UPDATE fichiers SET droits_proprio = ?, droits_autres = ? WHERE id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, droitsProprio);
            ps.setString(2, droitsAutres);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("FichierDao.updateDroits : " + e.getMessage());
            return false;
        }
    }

    public List<FichierProtege> findByProprietaire(int userId) {
        List<FichierProtege> list = new ArrayList<>();
        String sql = "SELECT * FROM fichiers WHERE proprietaire_id = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("FichierDao.findByProprietaire : " + e.getMessage());
        }
        return list;
    }

    public Optional<FichierProtege> findByNom(String nom) {
        String sql = "SELECT * FROM fichiers WHERE nom = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, nom);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("FichierDao.findByNom : " + e.getMessage());
        }
        return Optional.empty();
    }
}
