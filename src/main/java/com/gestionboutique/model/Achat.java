package com.gestionboutique.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un achat effectué par un client.
 */
public class Achat {

    private Long id;
    private Long clientId;
    private Long utilisateurId;
    private LocalDateTime dateAchat;
    private BigDecimal total;
    private String statut;

    private List<LigneAchat> lignes = new ArrayList<>();

    // Pour affichage uniquement
    private String clientNom;
    private String utilisateurNom;

    public Achat() {
        this.total = BigDecimal.ZERO;
        this.statut = "PAYE";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Long getUtilisateurId() { return utilisateurId; }
    public void setUtilisateurId(Long utilisateurId) { this.utilisateurId = utilisateurId; }

    public LocalDateTime getDateAchat() { return dateAchat; }
    public void setDateAchat(LocalDateTime dateAchat) { this.dateAchat = dateAchat; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public List<LigneAchat> getLignes() { return lignes; }
    public void setLignes(List<LigneAchat> lignes) { this.lignes = lignes; }

    public String getClientNom() { return clientNom; }
    public void setClientNom(String clientNom) { this.clientNom = clientNom; }

    public String getUtilisateurNom() { return utilisateurNom; }
    public void setUtilisateurNom(String utilisateurNom) { this.utilisateurNom = utilisateurNom; }

    /**
     * Calcule le total à partir des lignes.
     */
    public void recalculerTotal() {
        BigDecimal t = BigDecimal.ZERO;
        for (LigneAchat l : lignes) {
            if (l.getSousTotal() != null) {
                t = t.add(l.getSousTotal());
            }
        }
        this.total = t;
    }

    public void ajouterLigne(LigneAchat ligne) {
        this.lignes.add(ligne);
        recalculerTotal();
    }
}