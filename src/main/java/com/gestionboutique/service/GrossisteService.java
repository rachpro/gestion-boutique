package com.gestionboutique.service;

import com.gestionboutique.dao.GrossisteDAO;
import com.gestionboutique.model.Grossiste;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des grossistes.
 */
public class GrossisteService {

    private final GrossisteDAO grossisteDAO;

    public GrossisteService() {
        this.grossisteDAO = new GrossisteDAO();
    }

    public void ajouter(Grossiste g) throws SQLException {
        valider(g);
        grossisteDAO.ajouter(g);
    }

    public void modifier(Grossiste g) throws SQLException {
        if (g == null || g.getId() == null) {
            throw new IllegalArgumentException("Grossiste invalide.");
        }
        valider(g);
        grossisteDAO.modifier(g);
    }

    public void basculerActif(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }
        grossisteDAO.basculerActif(id);
    }

    public void supprimer(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }
        grossisteDAO.supprimer(id);
    }

    public List<Grossiste> listerTous() throws SQLException {
        return grossisteDAO.findAll();
    }

    public List<Grossiste> rechercherParNom(String recherche) throws SQLException {
        if (recherche == null || recherche.isBlank()) {
            return grossisteDAO.findAll();
        }
        return grossisteDAO.findByNom(recherche.trim());
    }

    public Optional<Grossiste> trouverParId(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }
        return grossisteDAO.findById(id);
    }

    private void valider(Grossiste g) {
        if (g == null) {
            throw new IllegalArgumentException("Le grossiste est obligatoire.");
        }
        if (isBlank(g.getNom())) {
            throw new IllegalArgumentException("Le nom est obligatoire.");
        }
        if (isBlank(g.getTelephone())) {
            throw new IllegalArgumentException("Le téléphone est obligatoire.");
        }
        if (g.getTelephone().length() < 8) {
            throw new IllegalArgumentException("Le téléphone doit contenir au moins 8 caractères.");
        }
        if (g.getEmail() != null && !g.getEmail().isBlank()
                && !g.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("L'email est invalide.");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}