package com.gestionboutique.model;

import java.math.BigDecimal;

public class Produit {

    private Long id;

    private String nom;

    private BigDecimal prixAchat;

    private BigDecimal prixVente;

    private int stock;

    public Produit() {
    }

    public Produit(
            String nom,
            BigDecimal prixAchat,
            BigDecimal prixVente,
            int stock) {

        this.nom = nom;
        this.prixAchat = prixAchat;
        this.prixVente = prixVente;
        this.stock = stock;
    }

    public Produit(
            Long id,
            String nom,
            BigDecimal prixAchat,
            BigDecimal prixVente,
            int stock) {

        this.id = id;
        this.nom = nom;
        this.prixAchat = prixAchat;
        this.prixVente = prixVente;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public BigDecimal getPrixAchat() {
        return prixAchat;
    }

    public void setPrixAchat(BigDecimal prixAchat) {
        this.prixAchat = prixAchat;
    }

    public BigDecimal getPrixVente() {
        return prixVente;
    }

    public void setPrixVente(BigDecimal prixVente) {
        this.prixVente = prixVente;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prixAchat=" + prixAchat +
                ", prixVente=" + prixVente +
                ", stock=" + stock +
                '}';
    }
}
