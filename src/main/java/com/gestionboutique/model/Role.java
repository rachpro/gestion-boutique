package com.gestionboutique.model;

/**
 * Rôles disponibles pour les utilisateurs de l'application.
 */
public enum Role {

    ADMIN("Administrateur"),
    VENDEUR("Vendeur");

    private final String libelle;

    Role(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}