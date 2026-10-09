package com.gestionboutique.service;

import com.gestionboutique.dao.LivraisonDAO;
import com.gestionboutique.dao.ProduitDAO;
import com.gestionboutique.model.LigneLivraison;
import com.gestionboutique.model.Livraison;
import com.gestionboutique.model.Produit;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des livraisons.
 */
public class LivraisonService {

    private final LivraisonDAO livraisonDAO;
    private final ProduitDAO produitDAO;

    public LivraisonService() {
        this.livraisonDAO = new LivraisonDAO();
        this.produitDAO = new ProduitDAO();
    }

    public void enregistrerLivraison(Livraison livraison) throws SQLException {

        if (livraison == null) {
            throw new IllegalArgumentException("La livraison est obligatoire.");
        }
        if (livraison.getGrossisteId() == null) {
            throw new IllegalArgumentException("Veuillez sélectionner un grossiste.");
        }
        if (livraison.getUtilisateurId() == null) {
            throw new IllegalArgumentException("Utilisateur non identifié.");
        }
        if (livraison.getLignes() == null || livraison.getLignes().isEmpty()) {
            throw new IllegalArgumentException("La livraison doit contenir au moins un produit.");
        }

        for (LigneLivraison l : livraison.getLignes()) {

            if (l.getQuantite() <= 0) {
                throw new IllegalArgumentException("La quantité doit être positive.");
            }

            Produit p = produitDAO.findById(l.getProduitId());

            if (p == null) {
                throw new IllegalArgumentException(
                        "Produit introuvable (id=" + l.getProduitId() + ")."
                );
            }

            l.setPrixUnitaire(p.getPrixAchat());
            l.setSousTotal(
                    p.getPrixAchat().multiply(BigDecimal.valueOf(l.getQuantite()))
            );
        }

        livraison.recalculerTotal();
        livraisonDAO.ajouter(livraison);
    }

    public List<Livraison> listerTous() throws SQLException {
        return livraisonDAO.findAll();
    }

    /**
     * Recherche les livraisons avec filtre par date.
     */
    public List<Livraison> rechercherParDate(LocalDate dateDebut, LocalDate dateFin) throws SQLException {
        if (dateDebut == null && dateFin == null) {
            return livraisonDAO.findAll();
        }
        return livraisonDAO.findByDateRange(dateDebut, dateFin);
    }

    public Optional<Livraison> trouverParId(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }
        return livraisonDAO.findById(id);
    }
}