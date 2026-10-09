package com.gestionboutique.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une livraison d'un grossiste vers la boutique.
 */
public class Livraison {

    private Long id;
    private Long grossisteId;
    private Long utilisateurId;
    private LocalDateTime dateLivraison;
    private BigDecimal total;
    private String reference;

    private List<LigneLivraison> lignes = new ArrayList<>();

    // Pour affichage
    private String grossisteNom;
    private String utilisateurNom;

    public Livraison() {
        this.total = BigDecimal.ZERO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getGrossisteId() { return grossisteId; }
    public void setGrossisteId(Long grossisteId) { this.grossisteId = grossisteId; }

    public Long getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(Long utilisateurId) { this.utilisateurId = utilisateurId; }

    public LocalDateTime getDateLivraison() { return dateLivraison; }
    public void setDateLivraison(LocalDateTime dateLivraison) { this.dateLivraison = dateLivraison; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public List<LigneLivraison> getLignes() { return lignes; }
    public void setLignes(List<LigneLivraison> lignes) { this.lignes = lignes; }

    public String getGrossisteNom() { return grossisteNom; }
    public void setGrossisteNom(String grossisteNom) { this.grossisteNom = grossisteNom; }

    public String getUtilisateurNom() { return utilisateurNom; }
    public void setUtilisateurNom(String utilisateurNom) { this.utilisateurNom = utilisateurNom; }

    public void recalculerTotal() {
        BigDecimal t = BigDecimal.ZERO;
        for (LigneLivraison l : lignes) {
            if (l.getSousTotal() != null) {
                t = t.add(l.getSousTotal());
            }
        }
        this.total = t;
    }

    public void ajouterLigne(LigneLivraison ligne) {
        this.lignes.add(ligne);
        recalculerTotal();
    }
}