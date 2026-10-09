package com.gestionboutique.dao;

import com.gestionboutique.config.DatabaseConnection;
import com.gestionboutique.model.Produit;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsable de l'accès aux données de la table {@code produit}.
 *
 * <p>Utilise le pool de connexions HikariCP fourni par
 * {@link DatabaseConnection}.</p>
 */
public class ProduitDAO {

    private static final String INSERT_SQL =
            "INSERT INTO produit (nom, prix_achat, prix_vente, stock) "
                    + "VALUES (?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT id, nom, prix_achat, prix_vente, stock "
                    + "FROM produit "
                    + "ORDER BY nom ASC";

    private static final String SELECT_BY_NAME_SQL =
            "SELECT id, nom, prix_achat, prix_vente, stock "
                    + "FROM produit "
                    + "WHERE nom ILIKE ? "
                    + "ORDER BY nom ASC";

    private static final String SELECT_BY_ID_SQL =
            "SELECT id, nom, prix_achat, prix_vente, stock "
                    + "FROM produit "
                    + "WHERE id = ?";

    private static final String UPDATE_SQL =
            "UPDATE produit "
                    + "SET nom = ?, prix_achat = ?, prix_vente = ?, stock = ? "
                    + "WHERE id = ?";

    private static final String DELETE_SQL =
            "DELETE FROM produit WHERE id = ?";

    /**
     * Ajoute un produit dans la base de données.
     *
     * <p>Met à jour l'identifiant du produit passé en paramètre
     * avec la clé générée par PostgreSQL.</p>
     */
    public void ajouter(Produit produit) throws SQLException {

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     INSERT_SQL,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setString(1, produit.getNom());
            statement.setBigDecimal(2, produit.getPrixAchat());
            statement.setBigDecimal(3, produit.getPrixVente());
            statement.setInt(4, produit.getStock());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    produit.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    /**
     * Récupère tous les produits, triés par nom.
     */
    public List<Produit> findAll() throws SQLException {

        List<Produit> produits = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                produits.add(mapResultSetToProduit(resultSet));
            }
        }

        return produits;
    }

    /**
     * Recherche les produits dont le nom contient la chaîne donnée
     * (recherche insensible à la casse grâce à ILIKE).
     */
    public List<Produit> findByNom(String nom) throws SQLException {

        List<Produit> produits = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_NAME_SQL)) {

            statement.setString(1, "%" + nom + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    produits.add(mapResultSetToProduit(resultSet));
                }
            }
        }

        return produits;
    }

    /**
     * Récupère un produit par son identifiant.
     *
     * @return le produit trouvé, ou {@code null} s'il n'existe pas.
     */
    public Produit findById(Long id) throws SQLException {

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToProduit(resultSet);
                }
            }
        }

        return null;
    }

    /**
     * Met à jour un produit existant.
     */
    public void modifier(Produit produit) throws SQLException {

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {

            statement.setString(1, produit.getNom());
            statement.setBigDecimal(2, produit.getPrixAchat());
            statement.setBigDecimal(3, produit.getPrixVente());
            statement.setInt(4, produit.getStock());
            statement.setLong(5, produit.getId());

            statement.executeUpdate();
        }
    }

    /**
     * Supprime un produit par son identifiant.
     */
    public void supprimer(Long id) throws SQLException {

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    /**
     * Convertit une ligne de {@link ResultSet} en objet {@link Produit}.
     */
    private Produit mapResultSetToProduit(ResultSet resultSet) throws SQLException {

        Long id = resultSet.getLong("id");
        String nom = resultSet.getString("nom");
        BigDecimal prixAchat = resultSet.getBigDecimal("prix_achat");
        BigDecimal prixVente = resultSet.getBigDecimal("prix_vente");
        int stock = resultSet.getInt("stock");

        return new Produit(id, nom, prixAchat, prixVente, stock);
    }
}