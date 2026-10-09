package com.gestionboutique.dao;

import com.gestionboutique.config.DatabaseConnection;
import com.gestionboutique.model.Client;

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
 * DAO pour la table {@code client}.
 */
public class ClientDAO {

    private static final String INSERT_SQL =
            "INSERT INTO client (nom, prenom, telephone, email, adresse, actif) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE client SET nom = ?, prenom = ?, telephone = ?, "
                    + "email = ?, adresse = ?, actif = ? WHERE id = ?";

    private static final String SELECT_COLUMNS =
            "SELECT id, nom, prenom, telephone, email, adresse, actif, date_creation "
                    + "FROM client ";

    private static final String SELECT_ALL_SQL =
            SELECT_COLUMNS + "ORDER BY nom ASC, prenom ASC";

    private static final String SELECT_BY_ID_SQL =
            SELECT_COLUMNS + "WHERE id = ?";

    private static final String SELECT_BY_NOM_SQL =
            SELECT_COLUMNS + "WHERE nom ILIKE ? OR prenom ILIKE ? "
                    + "ORDER BY nom ASC";

    private static final String BASCULER_ACTIF_SQL =
            "UPDATE client SET actif = NOT actif WHERE id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM client WHERE id = ?";

    public void ajouter(Client client) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getTelephone());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getAdresse());
            ps.setBoolean(6, client.isActif());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) client.setId(keys.getLong(1));
            }
        }
    }

    public void modifier(Client client) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setString(3, client.getTelephone());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getAdresse());
            ps.setBoolean(6, client.isActif());
            ps.setLong(7, client.getId());

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

    public Optional<Client> findById(Long id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public List<Client> findAll() throws SQLException {
        List<Client> liste = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(map(rs));
        }
        return liste;
    }

    public List<Client> findByNom(String recherche) throws SQLException {
        List<Client> liste = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_NOM_SQL)) {
            String pattern = "%" + recherche + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) liste.add(map(rs));
            }
        }
        return liste;
    }

    private Client map(ResultSet rs) throws SQLException {
        Client c = new Client();
        c.setId(rs.getLong("id"));
        c.setNom(rs.getString("nom"));
        c.setPrenom(rs.getString("prenom"));
        c.setTelephone(rs.getString("telephone"));
        c.setEmail(rs.getString("email"));
        c.setAdresse(rs.getString("adresse"));
        c.setActif(rs.getBoolean("actif"));

        Timestamp ts = rs.getTimestamp("date_creation");
        if (ts != null) c.setDateCreation(ts.toLocalDateTime());

        return c;
    }
}