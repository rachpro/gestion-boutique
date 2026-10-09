package com.gestionboutique.dao;

import com.gestionboutique.config.DatabaseConnection;
import com.gestionboutique.model.Grossiste;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO pour la table {@code grossiste}.
 */
public class GrossisteDAO {

    private static final String INSERT_SQL =
            "INSERT INTO grossiste (nom, telephone, email, adresse, contact_nom, actif) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE grossiste SET nom = ?, telephone = ?, email = ?, "
                    + "adresse = ?, contact_nom = ?, actif = ? WHERE id = ?";

    private static final String SELECT_COLUMNS =
            "SELECT id, nom, telephone, email, adresse, contact_nom, actif, date_creation "
                    + "FROM grossiste ";

    private static final String SELECT_ALL_SQL =
            SELECT_COLUMNS + "ORDER BY nom ASC";

    private static final String SELECT_BY_ID_SQL =
            SELECT_COLUMNS + "WHERE id = ?";

    private static final String SELECT_BY_NOM_SQL =
            SELECT_COLUMNS + "WHERE nom ILIKE ? ORDER BY nom ASC";

    private static final String BASCULER_ACTIF_SQL =
            "UPDATE grossiste SET actif = NOT actif WHERE id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM grossiste WHERE id = ?";

    public void ajouter(Grossiste g) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, g.getNom());
            ps.setString(2, g.getTelephone());
            ps.setString(3, g.getEmail());
            ps.setString(4, g.getAdresse());
            ps.setString(5, g.getContactNom());
            ps.setBoolean(6, g.isActif());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) g.setId(keys.getLong(1));
            }
        }
    }

    public void modifier(Grossiste g) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, g.getNom());
            ps.setString(2, g.getTelephone());
            ps.setString(3, g.getEmail());
            ps.setString(4, g.getAdresse());
            ps.setString(5, g.getContactNom());
            ps.setBoolean(6, g.isActif());
            ps.setLong(7, g.getId());

            ps.executeUpdate();
        }
    }

    public void basculerActif(Long id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(BASCULER_ACTIF_SQL)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public void supprimer(Long id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(DELETE_SQL)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public Optional<Grossiste> findById(Long id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public List<Grossiste> findAll() throws SQLException {
        List<Grossiste> liste = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(map(rs));
        }
        return liste;
    }

    public List<Grossiste> findByNom(String recherche) throws SQLException {
        List<Grossiste> liste = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_NOM_SQL)) {
            ps.setString(1, "%" + recherche + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) liste.add(map(rs));
            }
        }
        return liste;
    }

    private Grossiste map(ResultSet rs) throws SQLException {
        Grossiste g = new Grossiste();
        g.setId(rs.getLong("id"));
        g.setNom(rs.getString("nom"));
        g.setTelephone(rs.getString("telephone"));
        g.setEmail(rs.getString("email"));
        g.setAdresse(rs.getString("adresse"));
        g.setContactNom(rs.getString("contact_nom"));
        g.setActif(rs.getBoolean("actif"));

        Timestamp ts = rs.getTimestamp("date_creation");
        if (ts != null) g.setDateCreation(ts.toLocalDateTime());

        return g;
    }
}