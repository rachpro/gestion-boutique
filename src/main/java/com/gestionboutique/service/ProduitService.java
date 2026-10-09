package com.gestionboutique.service;

import com.gestionboutique.dao.ProduitDAO;
import com.gestionboutique.model.Produit;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProduitService {

    private final ProduitDAO produitDAO;

    public ProduitService() {
        this.produitDAO = new ProduitDAO();
    }

    public void ajouterProduit(Produit produit)
            throws SQLException {

        validerProduit(produit);

        produitDAO.ajouter(produit);
    }

    public List<Produit> rechercherProduits(String nom)
            throws SQLException {

        if (nom == null || nom.isBlank()) {
            return produitDAO.findAll();
        }

        return produitDAO.findByNom(nom.trim());
    }

    public Produit rechercherParId(Long id)
            throws SQLException {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant du produit est invalide."
            );
        }

        return produitDAO.findById(id);
    }

    public void modifierProduit(Produit produit)
            throws SQLException {

        if (produit == null
                || produit.getId() == null
                || produit.getId() <= 0) {

            throw new IllegalArgumentException(
                    "Le produit à modifier est invalide."
            );
        }

        validerProduit(produit);

        produitDAO.modifier(produit);
    }

    public void supprimerProduit(Long id)
            throws SQLException {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "L'identifiant du produit est invalide."
            );
        }

        produitDAO.supprimer(id);
    }

    private void validerProduit(Produit produit) {

        if (produit == null) {
            throw new IllegalArgumentException(
                    "Le produit ne peut pas être null."
            );
        }

        if (produit.getNom() == null
                || produit.getNom().isBlank()) {

            throw new IllegalArgumentException(
                    "Le nom du produit est obligatoire."
            );
        }

        String nom = produit.getNom().trim();

        if (nom.length() < 2) {
            throw new IllegalArgumentException(
                    "Le nom du produit doit contenir "
                            + "au moins 2 caractères."
            );
        }

        if (nom.length() > 150) {
            throw new IllegalArgumentException(
                    "Le nom du produit ne doit pas dépasser "
                            + "150 caractères."
            );
        }

        BigDecimal prixAchat = produit.getPrixAchat();

        if (prixAchat == null
                || prixAchat.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Le prix d'achat doit être positif ou nul."
            );
        }

        BigDecimal prixVente = produit.getPrixVente();

        if (prixVente == null
                || prixVente.compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Le prix de vente doit être positif ou nul."
            );
        }

        if (produit.getStock() < 0) {
            throw new IllegalArgumentException(
                    "Le stock ne peut pas être négatif."
            );
        }

        produit.setNom(nom);
    }
}
