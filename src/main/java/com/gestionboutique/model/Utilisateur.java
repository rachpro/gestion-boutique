package com.gestionboutique.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entité représentant un utilisateur de l'application.
 *
 * <p>Le mot de passe n'est jamais stocké en clair : il est hashé
 * avec BCrypt avant persistance.</p>
 */
public class Utilisateur {

    private Long id;

    private String nom;

    private String prenom;

    private String matricule;

    /** "M" ou "F". */
    private String sexe;

    private LocalDate dateNaissance;

    private String identifiant;

    /** Mot de passe hashé (BCrypt). */
    private String motDePasse;

    private Role role;

    private boolean actif;

    private LocalDateTime dateCreation;

    public Utilisateur() {
        this.role = Role.VENDEUR;
        this.actif = true;
    }

    public Utilisateur(
            String nom,
            String prenom,
            String matricule,
            String sexe,
            LocalDate dateNaissance,
            String identifiant,
            String motDePasse,
            Role role) {

        this();
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.identifiant = identifiant;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // ==================== Getters / Setters ====================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getIdentifiant() { return identifiant; }
    public void setIdentifiant(String identifiant) { this.identifiant = identifiant; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    /**
     * Retourne le nom complet : "Prénom Nom".
     */
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", matricule='" + matricule + '\'' +
                ", identifiant='" + identifiant + '\'' +
                ", role=" + role +
                ", actif=" + actif +
                '}';
    }
}