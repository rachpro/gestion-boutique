package com.gestionboutique.model;

import java.time.LocalDateTime;

/**
 * Entité représentant un client de la boutique.
 */
public class Client {

    private Long id;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String adresse;
    private boolean actif;
    private LocalDateTime dateCreation;

    public Client() {
        this.actif = true;
    }

    public Client(String nom, String prenom, String telephone) {
        this();
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
    }

    // Getters / Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", telephone='" + telephone + '\'' +
                ", actif=" + actif +
                '}';
    }
}