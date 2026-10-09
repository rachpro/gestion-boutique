package com.gestionboutique.dao;

import com.gestionboutique.config.DatabaseConnection;
import com.gestionboutique.model.Achat;
import com.gestionboutique.model.LigneAchat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO pour les tables {@code achat} et {@code ligne_achat}.
 */
public class AchatDAO {

    private static final String INSERT_ACHAT_SQL =
            "INSERT INTO achat (client_id, utilisateur_id, date_achat, total, statut) "
                    + "VALUES (?, ?, NOW(), ?, ?)";

    private static final String INSERT_LIGNE_SQL =
            "INSERT INTO ligne_achat (achat_id, produit_id, quantite, prix_unitaire, sous_total) "
                    + "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_STOCK_SQL =
            "UPDATE produit SET stock = stock - ? WHERE id = ?";

    private static final String SELECT_ACHATS_SQL =
            "SELECT a.id, a.client_id, a.utilisateur_id, a.date_achat, a.total, a.statut, "
                    + " c.nom AS client_nom, c.prenom AS client_prenom, "
                    + " u.nom AS user_nom, u.prenom AS user_prenom "
                    + "FROM achat a "
                    + "JOIN client c ON c.id = a.client_id "
                    + "JOIN utilisateur u ON u.id = a.utilisateur_id ";

    private static final String SELECT_ALL_SQL =
            SELECT_ACHATS_SQL + "ORDER BY a.date_achat DESC";

    private static final String SELECT_BY_ID_SQL =
            SELECT_ACHATS_SQL + "WHERE a.id = ?";

    private static final String SELECT_LIGNES_SQL =
            "SELECT l.id, l.achat_id, l.produit_id, l.quantite, l.prix_unitaire, l.sous_total, "
                    + " p.nom AS produit_nom "
                    + "FROM ligne_achat l "
                    + "JOIN produit p ON p.id = l.produit_id "
                    + "WHERE l.achat_id = ?";

    /**
     * Enregistre un achat avec ses lignes et décrémente le stock.
     */
    public void ajouter(Achat achat) throws SQLException {

        Connection c = null;
        try {
            c = DatabaseConnection.getConnection();
            c.setAutoCommit(false);

            try (PreparedStatement ps = c.prepareStatement(INSERT_ACHAT_SQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, achat.getClientId());
                ps.setLong(2, achat.getUtilisateurId());
                ps.setBigDecimal(3, achat.getTotal());
                ps.setString(4, achat.getStatut());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        achat.setId(keys.getLong(1));
                    } else {
                        throw new SQLException("Impossible de récupérer l'id de l'achat.");
                    }
                }
            }

            try (PreparedStatement psLigne = c.prepareStatement(INSERT_LIGNE_SQL, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psStock = c.prepareStatement(UPDATE_STOCK_SQL)) {

                for (LigneAchat l : achat.getLignes()) {
                    psLigne.setLong(1, achat.getId());
                    psLigne.setLong(2, l.getProduitId());
                    psLigne.setInt(3, l.getQuantite());
                    psLigne.setBigDecimal(4, l.getPrixUnitaire());
                    psLigne.setBigDecimal(5, l.getSousTotal());
                    psLigne.executeUpdate();

                    try (ResultSet keys = psLigne.getGeneratedKeys()) {
                        if (keys.next()) l.setId(keys.getLong(1));
                    }
                    l.setAchatId(achat.getId());

                    psStock.setInt(1, l.getQuantite());
                    psStock.setLong(2, l.getProduitId());
                    psStock.executeUpdate();
                }
            }

            c.commit();

        } catch (SQLException e) {
            if (c != null) {
                try { c.rollback(); } catch (SQLException ignored) { }
            }
            throw e;

        } finally {
            if (c != null) {
                try { c.setAutoCommit(true); c.close(); } catch (SQLException ignored) { }
            }
        }
    }

    public List<Achat> findAll() throws SQLException {
        List<Achat> liste = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(mapAchat(rs));
        }
        return liste;
    }

    /**
     * Recherche les achats dans une plage de dates.
     */
    public List<Achat> findByDateRange(LocalDate dateDebut, LocalDate dateFin) throws SQLException {

        List<Achat> liste = new ArrayList<>();

        StringBuilder sql = new StringBuilder(SELECT_ACHATS_SQL);
        sql.append("WHERE 1 = 1 ");

        if (dateDebut != null) {
            sql.append("AND a.date_achat >= ? ");
        }
        if (dateFin != null) {
            sql.append("AND a.date_achat <= ? ");
        }
        sql.append("ORDER BY a.date_achat DESC");

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int index = 1;
            if (dateDebut != null) {
                ps.setTimestamp(index++, Timestamp.valueOf(dateDebut.atStartOfDay()));
            }
            if (dateFin != null) {
                ps.setTimestamp(index, Timestamp.valueOf(dateFin.atTime(23, 59, 59)));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) liste.add(mapAchat(rs));
            }
        }

        return liste;
    }

    public Optional<Achat> findById(Long id) throws SQLException {
        Achat achat = null;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) achat = mapAchat(rs);
            }
        }

        if (achat == null) return Optional.empty();

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_LIGNES_SQL)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) achat.getLignes().add(mapLigne(rs));
            }
        }

        return Optional.of(achat);
    }

    private Achat mapAchat(ResultSet rs) throws SQLException {
        Achat a = new Achat();
        a.setId(rs.getLong("id"));
        a.setClientId(rs.getLong("client_id"));
        a.setUtilisateurId(rs.getLong("utilisateur_id"));

        Timestamp ts = rs.getTimestamp("date_achat");
        if (ts != null) a.setDateAchat(ts.toLocalDateTime());

        a.setTotal(rs.getBigDecimal("total"));
        a.setStatut(rs.getString("statut"));

        String cn = rs.getString("client_nom");
        String cp = rs.getString("client_prenom");
        if (cn != null) a.setClientNom(cp + " " + cn);

        String un = rs.getString("user_nom");
        String up = rs.getString("user_prenom");
        if (un != null) a.setUtilisateurNom(up + " " + un);

        return a;
    }

    private LigneAchat mapLigne(ResultSet rs) throws SQLException {
        LigneAchat l = new LigneAchat();
        l.setId(rs.getLong("id"));
        l.setAchatId(rs.getLong("achat_id"));
        l.setProduitId(rs.getLong("produit_id"));
        l.setQuantite(rs.getInt("quantite"));
        l.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
        l.setSousTotal(rs.getBigDecimal("sous_total"));
        l.setProduitNom(rs.getString("produit_nom"));
        return l;
    }
}