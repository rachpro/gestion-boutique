package com.gestionboutique.service;

import com.gestionboutique.dao.AchatDAO;
import com.gestionboutique.dao.ProduitDAO;
import com.gestionboutique.model.Achat;
import com.gestionboutique.model.LigneAchat;
import com.gestionboutique.model.Produit;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des achats.
 */
public class AchatService {

    private final AchatDAO achatDAO;
    private final ProduitDAO produitDAO;

    public AchatService() {
        this.achatDAO = new AchatDAO();
        this.produitDAO = new ProduitDAO();
    }

    /**
     * Enregistre un achat avec vérification du stock.
     */
    public void enregistrerAchat(Achat achat) throws SQLException {

        if (achat == null) {
            throw new IllegalArgumentException("L'achat est obligatoire.");
        }
        if (achat.getClientId() == null) {
            throw new IllegalArgumentException("Veuillez sélectionner un client.");
        }
        if (achat.getUtilisateurId() == null) {
            throw new IllegalArgumentException("Vendeur non identifié.");
        }
        if (achat.getLignes() == null || achat.getLignes().isEmpty()) {
            throw new IllegalArgumentException("L'achat doit contenir au moins un produit.");
        }

        for (LigneAchat l : achat.getLignes()) {

            if (l.getQuantite() <= 0) {
                throw new IllegalArgumentException("La quantité doit être positive.");
            }

            Produit p = produitDAO.findById(l.getProduitId());

            if (p == null) {
                throw new IllegalArgumentException(
                        "Produit introuvable (id=" + l.getProduitId() + ")."
                );
            }

            if (p.getStock() < l.getQuantite()) {
                throw new IllegalArgumentException(
                        "Stock insuffisant pour \"" + p.getNom() + "\" (stock = "
                                + p.getStock() + ", demandé = " + l.getQuantite() + ")."
                );
            }

            l.setPrixUnitaire(p.getPrixVente());
            l.setSousTotal(
                    p.getPrixVente().multiply(BigDecimal.valueOf(l.getQuantite()))
            );
        }

        achat.recalculerTotal();
        achatDAO.ajouter(achat);
    }

    public List<Achat> listerTous() throws SQLException {
        return achatDAO.findAll();
    }

    /**
     * Recherche les achats avec filtre par date.
     */
    public List<Achat> rechercherParDate(LocalDate dateDebut, LocalDate dateFin) throws SQLException {
        if (dateDebut == null && dateFin == null) {
            return achatDAO.findAll();
        }
        return achatDAO.findByDateRange(dateDebut, dateFin);
    }

    public Optional<Achat> trouverParId(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }
        return achatDAO.findById(id);
    }
}