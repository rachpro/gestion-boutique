package com.gestionboutique.model;

import java.time.LocalDateTime;

/**
 * Entité représentant un grossiste (fournisseur).
 */
public class Grossiste {

    private Long id;
    private String nom;
    private String telephone;
    private String email;
    private String adresse;
    private String contactNom;
    private boolean actif;
    private LocalDateTime dateCreation;

    public Grossiste() {
        this.actif = true;
    }

    public Grossiste(String nom, String telephone) {
        this();
        this.nom = nom;
        this.telephone = telephone;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getContactNom() { return contactNom; }
    public void setContactNom(String contactNom) { this.contactNom = contactNom; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    @Override
    public String toString() {
        return "Grossiste{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", telephone='" + telephone + '\'' +
                ", actif=" + actif +
                '}';
    }
}