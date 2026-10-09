package com.gestionboutique.dao;

import com.gestionboutique.config.DatabaseConnection;
import com.gestionboutique.model.Role;
import com.gestionboutique.model.Utilisateur;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO responsable de l'accès aux données de la table {@code utilisateur}.
 */
public class UtilisateurDAO {

    private static final String INSERT_SQL =
            "INSERT INTO utilisateur "
                    + "(nom, prenom, matricule, sexe, date_naissance, "
                    + " identifiant, mot_de_passe, role, actif) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE utilisateur SET "
                    + "nom = ?, prenom = ?, matricule = ?, sexe = ?, "
                    + "date_naissance = ?, identifiant = ?, mot_de_passe = ?, "
                    + "role = ?, actif = ? "
                    + "WHERE id = ?";

    private static final String SELECT_COLUMNS =
            "SELECT id, nom, prenom, matricule, sexe, date_naissance, "
                    + "identifiant, mot_de_passe, role, actif, date_creation "
                    + "FROM utilisateur ";

    private static final String SELECT_BY_IDENTIFIANT_SQL =
            SELECT_COLUMNS + "WHERE identifiant = ?";

    private static final String SELECT_BY_ID_SQL =
            SELECT_COLUMNS + "WHERE id = ?";

    private static final String SELECT_ALL_SQL =
            SELECT_COLUMNS + "ORDER BY nom ASC, prenom ASC";

    private static final String EXISTS_IDENTIFIANT_SQL =
            "SELECT 1 FROM utilisateur WHERE identifiant = ?";

    private static final String EXISTS_MATRICULE_SQL =
            "SELECT 1 FROM utilisateur WHERE matricule = ?";

    private static final String EXISTS_IDENTIFIANT_AUTRE_SQL =
            "SELECT 1 FROM utilisateur WHERE identifiant = ? AND id <> ?";

    private static final String EXISTS_MATRICULE_AUTRE_SQL =
            "SELECT 1 FROM utilisateur WHERE matricule = ? AND id <> ?";

    private static final String BASCULER_ACTIF_SQL =
            "UPDATE utilisateur SET actif = NOT actif WHERE id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM utilisateur WHERE id = ?";

    // ==================== CRUD ====================

    public void ajouter(Utilisateur utilisateur) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            bind(ps, utilisateur, false);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    utilisateur.setId(keys.getLong(1));
                }
            }
        }
    }

    public void modifier(Utilisateur utilisateur) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(UPDATE_SQL)) {

            bind(ps, utilisateur, true);
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

    // ==================== Lecture ====================

    public Optional<Utilisateur> findByIdentifiant(String identifiant) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_IDENTIFIANT_SQL)) {

            ps.setString(1, identifiant);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<Utilisateur> findById(Long id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    public List<Utilisateur> findAll() throws SQLException {
        List<Utilisateur> liste = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) liste.add(map(rs));
        }
        return liste;
    }

    // ==================== Vérifications ====================

    public boolean identifiantExiste(String identifiant) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(EXISTS_IDENTIFIANT_SQL)) {
            ps.setString(1, identifiant);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean matriculeExiste(String matricule) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(EXISTS_MATRICULE_SQL)) {
            ps.setString(1, matricule);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean identifiantExistePourAutre(String identifiant, Long id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(EXISTS_IDENTIFIANT_AUTRE_SQL)) {
            ps.setString(1, identifiant);
            ps.setLong(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean matriculeExistePourAutre(String matricule, Long id) throws SQLException {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(EXISTS_MATRICULE_AUTRE_SQL)) {
            ps.setString(1, matricule);
            ps.setLong(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ==================== Helpers ====================

    private void bind(PreparedStatement ps, Utilisateur u, boolean includeId) throws SQLException {
        ps.setString(1, u.getNom());
        ps.setString(2, u.getPrenom());
        ps.setString(3, u.getMatricule());
        ps.setString(4, u.getSexe());
        ps.setDate(5, Date.valueOf(u.getDateNaissance()));
        ps.setString(6, u.getIdentifiant());
        ps.setString(7, u.getMotDePasse());
        ps.setString(8, u.getRole().name());
        ps.setBoolean(9, u.isActif());

        if (includeId) {
            ps.setLong(10, u.getId());
        }
    }

    private Utilisateur map(ResultSet rs) throws SQLException {
        Utilisateur u = new Utilisateur();
        u.setId(rs.getLong("id"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setMatricule(rs.getString("matricule"));
        u.setSexe(rs.getString("sexe"));
        u.setDateNaissance(rs.getDate("date_naissance").toLocalDate());
        u.setIdentifiant(rs.getString("identifiant"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        u.setRole(Role.valueOf(rs.getString("role")));
        u.setActif(rs.getBoolean("actif"));

        Timestamp ts = rs.getTimestamp("date_creation");
        if (ts != null) u.setDateCreation(ts.toLocalDateTime());

        return u;
    }
}