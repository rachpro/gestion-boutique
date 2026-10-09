package com.gestionboutique.model;

import java.math.BigDecimal;

/**
 * Représente une ligne dans une livraison.
 */
public class LigneLivraison {

    private Long id;
    private Long livraisonId;
    private Long produitId;
    private int quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal sousTotal;

    private String produitNom;

    public LigneLivraison() {
    }

    public LigneLivraison(Long produitId, int quantite) {
        this.produitId = produitId;
        this.quantite = quantite;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLivraisonId() { return livraisonId; }
    public void setLivraisonId(Long livraisonId) { this.livraisonId = livraisonId; }

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