package com.gestionboutique.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.gestionboutique.dao.UtilisateurDAO;
import com.gestionboutique.model.Role;
import com.gestionboutique.model.Utilisateur;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service métier pour la gestion des utilisateurs.
 */
public class UtilisateurService {

    private static final int BCRYPT_COST = 12;

    private final UtilisateurDAO utilisateurDAO;

    public UtilisateurService() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    // ==================== Authentification ====================

    public Optional<Utilisateur> authentifier(String identifiant, String motDePasse)
            throws SQLException {

        if (identifiant == null || identifiant.isBlank()
                || motDePasse == null || motDePasse.isBlank()) {
            return Optional.empty();
        }

        Optional<Utilisateur> opt = utilisateurDAO.findByIdentifiant(identifiant.trim());

        if (opt.isEmpty()) {
            return Optional.empty();
        }

        Utilisateur utilisateur = opt.get();

        if (!utilisateur.isActif()) {
            return Optional.empty();
        }

        BCrypt.Result result = BCrypt.verifyer()
                .verify(motDePasse.toCharArray(),
                        utilisateur.getMotDePasse().toCharArray());

        return result.verified ? Optional.of(utilisateur) : Optional.empty();
    }

    // ==================== Création ====================

    public void ajouterUtilisateur(Utilisateur utilisateur, String motDePasseEnClair)
            throws SQLException {

        validerUtilisateur(utilisateur, motDePasseEnClair, true);

        if (utilisateurDAO.identifiantExiste(utilisateur.getIdentifiant())) {
            throw new IllegalArgumentException("Cet identifiant est déjà utilisé.");
        }

        if (utilisateurDAO.matriculeExiste(utilisateur.getMatricule())) {
            throw new IllegalArgumentException("Ce matricule est déjà utilisé.");
        }

        String hash = BCrypt.withDefaults()
                .hashToString(BCRYPT_COST, motDePasseEnClair.toCharArray());

        utilisateur.setMotDePasse(hash);
        utilisateurDAO.ajouter(utilisateur);
    }

    // ==================== Modification ====================

    public void modifierUtilisateur(Utilisateur utilisateur, String nouveauMotDePasse)
            throws SQLException {

        if (utilisateur == null || utilisateur.getId() == null) {
            throw new IllegalArgumentException("Utilisateur invalide.");
        }

        validerUtilisateur(utilisateur, nouveauMotDePasse, false);

        if (utilisateurDAO.identifiantExistePourAutre(utilisateur.getIdentifiant(), utilisateur.getId())) {
            throw new IllegalArgumentException("Cet identifiant est déjà utilisé par un autre utilisateur.");
        }

        if (utilisateurDAO.matriculeExistePourAutre(utilisateur.getMatricule(), utilisateur.getId())) {
            throw new IllegalArgumentException("Ce matricule est déjà utilisé par un autre utilisateur.");
        }

        // Si un nouveau mot de passe est fourni, on le hash
        if (nouveauMotDePasse != null && !nouveauMotDePasse.isBlank()) {
            String hash = BCrypt.withDefaults()
                    .hashToString(BCRYPT_COST, nouveauMotDePasse.toCharArray());
            utilisateur.setMotDePasse(hash);
        }

        utilisateurDAO.modifier(utilisateur);
    }

    // ==================== Basculer actif ====================

    public void basculerActif(Long id) throws SQLException {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }

        Optional<Utilisateur> opt = utilisateurDAO.findById(id);
        if (opt.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur introuvable.");
        }

        Utilisateur u = opt.get();

        // Empêche de désactiver le dernier admin actif
        if (u.getRole() == Role.ADMIN && u.isActif()) {
            long adminActifs = utilisateurDAO.findAll().stream()
                    .filter(x -> x.getRole() == Role.ADMIN && x.isActif())
                    .count();
            if (adminActifs <= 1) {
                throw new IllegalArgumentException(
                        "Impossible de désactiver le dernier administrateur actif."
                );
            }
        }

        utilisateurDAO.basculerActif(id);
    }

    // ==================== Lecture ====================

    public List<Utilisateur> listerTous() throws SQLException {
        return utilisateurDAO.findAll();
    }

    public Optional<Utilisateur> trouverParId(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Identifiant invalide.");
        }
        return utilisateurDAO.findById(id);
    }

    // ==================== Validation ====================

    private void validerUtilisateur(Utilisateur u, String motDePasse, boolean creation) {

        if (u == null) {
            throw new IllegalArgumentException("L'utilisateur est obligatoire.");
        }

        if (isBlank(u.getNom())) {
            throw new IllegalArgumentException("Le nom est obligatoire.");
        }

        if (isBlank(u.getPrenom())) {
            throw new IllegalArgumentException("Le prénom est obligatoire.");
        }

        if (isBlank(u.getMatricule())) {
            throw new IllegalArgumentException("Le matricule est obligatoire.");
        }

        if (isBlank(u.getSexe()) || (!"M".equals(u.getSexe()) && !"F".equals(u.getSexe()))) {
            throw new IllegalArgumentException("Le sexe doit être 'M' ou 'F'.");
        }

        if (u.getDateNaissance() == null) {
            throw new IllegalArgumentException("La date de naissance est obligatoire.");
        }

        if (u.getDateNaissance().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date de naissance ne peut pas être dans le futur.");
        }

        if (isBlank(u.getIdentifiant())) {
            throw new IllegalArgumentException("L'identifiant est obligatoire.");
        }

        if (u.getIdentifiant().length() < 4) {
            throw new IllegalArgumentException("L'identifiant doit contenir au moins 4 caractères.");
        }

        if (creation) {
            if (motDePasse == null || motDePasse.length() < 6) {
                throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères.");
            }
        } else {
            // En modification, si un mot de passe est fourni, il doit être valide
            if (motDePasse != null && !motDePasse.isBlank() && motDePasse.length() < 6) {
                throw new IllegalArgumentException("Le nouveau mot de passe doit contenir au moins 6 caractères.");
            }
        }

        if (u.getRole() == null) {
            u.setRole(Role.VENDEUR);
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}