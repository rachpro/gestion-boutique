package com.gestionboutique.service;

import com.gestionboutique.dao.ClientDAO;
import com.gestionboutique.model.Client;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des clients.
 */
public class ClientService {

    private final ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
    }

    public void ajouter(Client client) throws SQLException {
        valider(client);
        clientDAO.ajouter(client);
    }

    public void modifier(Client client) throws SQLException {
        if (client == null || client.getId() == null) {
            throw new IllegalArgumentException("Client invalide.");
        }
        valider(client);
        clientDAO.modifier(client);
    }

    public void basculerActif(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }
        clientDAO.basculerActif(id);
    }

    public void supprimer(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }
        clientDAO.supprimer(id);
    }

    public List<Client> listerTous() throws SQLException {
        return clientDAO.findAll();
    }

    public List<Client> rechercherParNom(String recherche) throws SQLException {
        if (recherche == null || recherche.isBlank()) {
            return clientDAO.findAll();
        }
        return clientDAO.findByNom(recherche.trim());
    }

    public Optional<Client> trouverParId(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }
        return clientDAO.findById(id);
    }

    private void valider(Client c) {
        if (c == null) {
            throw new IllegalArgumentException("Le client est obligatoire.");
        }
        if (isBlank(c.getNom())) {
            throw new IllegalArgumentException("Le nom est obligatoire.");
        }
        if (isBlank(c.getPrenom())) {
            throw new IllegalArgumentException("Le prénom est obligatoire.");
        }
        if (isBlank(c.getTelephone())) {
            throw new IllegalArgumentException("Le téléphone est obligatoire.");
        }
        if (c.getTelephone().length() < 8) {
            throw new IllegalArgumentException("Le téléphone doit contenir au moins 8 caractères.");
        }
        if (c.getEmail() != null && !c.getEmail().isBlank()
                && !c.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("L'email est invalide.");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}