package com.gestionboutique.dao;

import com.gestionboutique.config.DatabaseConnection;
import com.gestionboutique.model.LigneLivraison;
import com.gestionboutique.model.Livraison;

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
 * DAO pour les tables {@code livraison} et {@code ligne_livraison}.
 */
public class LivraisonDAO {

    private static final String INSERT_LIVRAISON_SQL =
            "INSERT INTO livraison (grossiste_id, utilisateur_id, date_livraison, total, reference) "
                    + "VALUES (?, ?, NOW(), ?, ?)";

    private static final String INSERT_LIGNE_SQL =
            "INSERT INTO ligne_livraison (livraison_id, produit_id, quantite, prix_unitaire, sous_total) "
                    + "VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_STOCK_SQL =
            "UPDATE produit SET stock = stock + ? WHERE id = ?";

    private static final String SELECT_LIVRAISONS_SQL =
            "SELECT l.id, l.grossiste_id, l.utilisateur_id, l.date_livraison, l.total, l.reference, "
                    + " g.nom AS grossiste_nom, "
                    + " u.nom AS user_nom, u.prenom AS user_prenom "
                    + "FROM livraison l "
                    + "JOIN grossiste g ON g.id = l.grossiste_id "
                    + "JOIN utilisateur u ON u.id = l.utilisateur_id ";

    private static final String SELECT_ALL_SQL =
            SELECT_LIVRAISONS_SQL + "ORDER BY l.date_livraison DESC";

    private static final String SELECT_BY_ID_SQL =
            SELECT_LIVRAISONS_SQL + "WHERE l.id = ?";

    private static final String SELECT_LIGNES_SQL =
            "SELECT ll.id, ll.livraison_id, ll.produit_id, ll.quantite, "
                    + " ll.prix_unitaire, ll.sous_total, p.nom AS produit_nom "
                    + "FROM ligne_livraison ll "
                    + "JOIN produit p ON p.id = ll.produit_id "
                    + "WHERE ll.livraison_id = ?";

    /**
     * Enregistre une livraison avec ses lignes et AUGMENTE le stock.
     */
    public void ajouter(Livraison livraison) throws SQLException {

        Connection c = null;
        try {
            c = DatabaseConnection.getConnection();
            c.setAutoCommit(false);

            try (PreparedStatement ps = c.prepareStatement(INSERT_LIVRAISON_SQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, livraison.getGrossisteId());
                ps.setLong(2, livraison.getUtilisateurId());
                ps.setBigDecimal(3, livraison.getTotal());
                ps.setString(4, livraison.getReference());
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        livraison.setId(keys.getLong(1));
                    } else {
                        throw new SQLException("Impossible de récupérer l'id de la livraison.");
                    }
                }
            }

            try (PreparedStatement psLigne = c.prepareStatement(INSERT_LIGNE_SQL, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psStock = c.prepareStatement(UPDATE_STOCK_SQL)) {

                for (LigneLivraison l : livraison.getLignes()) {
                    psLigne.setLong(1, livraison.getId());
                    psLigne.setLong(2, l.getProduitId());
                    psLigne.setInt(3, l.getQuantite());
                    psLigne.setBigDecimal(4, l.getPrixUnitaire());
                    psLigne.setBigDecimal(5, l.getSousTotal());
                    psLigne.executeUpdate();

                    try (ResultSet keys = psLigne.getGeneratedKeys()) {
                        if (keys.next()) l.setId(keys.getLong(1));
                    }
                    l.setLivraisonId(livraison.getId());

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

    public List<Livraison> findAll() throws SQLException {
        List<Livraison> liste = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) liste.add(mapLivraison(rs));
        }
        return liste;
    }

    /**
     * Recherche les livraisons dans une plage de dates.
     */
    public List<Livraison> findByDateRange(LocalDate dateDebut, LocalDate dateFin) throws SQLException {

        List<Livraison> liste = new ArrayList<>();

        StringBuilder sql = new StringBuilder(SELECT_LIVRAISONS_SQL);
        sql.append("WHERE 1 = 1 ");

        if (dateDebut != null) {
            sql.append("AND l.date_livraison >= ? ");
        }
        if (dateFin != null) {
            sql.append("AND l.date_livraison <= ? ");
        }
        sql.append("ORDER BY l.date_livraison DESC");

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
                while (rs.next()) liste.add(mapLivraison(rs));
            }
        }

        return liste;
    }

    public Optional<Livraison> findById(Long id) throws SQLException {
        Livraison livraison = null;

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) livraison = mapLivraison(rs);
            }
        }

        if (livraison == null) return Optional.empty();

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(SELECT_LIGNES_SQL)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) livraison.getLignes().add(mapLigne(rs));
            }
        }

        return Optional.of(livraison);
    }

    private Livraison mapLivraison(ResultSet rs) throws SQLException {
        Livraison l = new Livraison();
        l.setId(rs.getLong("id"));
        l.setGrossisteId(rs.getLong("grossiste_id"));
        l.setUtilisateurId(rs.getLong("utilisateur_id"));

        Timestamp ts = rs.getTimestamp("date_livraison");
        if (ts != null) l.setDateLivraison(ts.toLocalDateTime());

        l.setTotal(rs.getBigDecimal("total"));
        l.setReference(rs.getString("reference"));
        l.setGrossisteNom(rs.getString("grossiste_nom"));

        String un = rs.getString("user_nom");
        String up = rs.getString("user_prenom");
        if (un != null) l.setUtilisateurNom(up + " " + un);

        return l;
    }

    private LigneLivraison mapLigne(ResultSet rs) throws SQLException {
        LigneLivraison l = new LigneLivraison();
        l.setId(rs.getLong("id"));
        l.setLivraisonId(rs.getLong("livraison_id"));
        l.setProduitId(rs.getLong("produit_id"));
        l.setQuantite(rs.getInt("quantite"));
        l.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
        l.setSousTotal(rs.getBigDecimal("sous_total"));
        l.setProduitNom(rs.getString("produit_nom"));
        return l;
    }
}