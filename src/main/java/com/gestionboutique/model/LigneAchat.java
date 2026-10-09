package com.gestionboutique.model;

import java.math.BigDecimal;

/**
 * Représente une ligne dans un achat (un produit + quantité).
 */
public class LigneAchat {

    private Long id;
    private Long achatId;
    private Long produitId;
    private int quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal sousTotal;

    // Pour affichage uniquement (non persisté)
    private String produitNom;

    public LigneAchat() {
    }

    public LigneAchat(Long produitId, int quantite, BigDecimal prixUnitaire) {
        this.produitId = produitId;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.sousTotal = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAchatId() { return achatId; }
    public void setAchatId(Long achatId) { this.achatId = achatId; }

    public Long getProduitId() { return produitId; }
    public void setProduitId(Long produitId) { this.produitId = produitId; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public BigDecimal getSousTotal() { return sousTotal; }
    public void setSousTotal(BigDecimal sousTotal) { this.sousTotal = sousTotal; }

    public String getProduitNom() { return produitNom; }
    public void setProduitNom(String produitNom) { this.produitNom = produitNom; }
}