package com.gestionboutique.controller;

import com.gestionboutique.model.Role;
import com.gestionboutique.model.Utilisateur;
import com.gestionboutique.service.UtilisateurService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@WebServlet("/utilisateurs/modifier")
public class UtilisateurModifierServlet extends HttpServlet {

    private UtilisateurService utilisateurService;

    @Override
    public void init() throws ServletException {
        utilisateurService = new UtilisateurService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");

        try {
            Long id = Long.parseLong(idStr);
            Optional<Utilisateur> opt = utilisateurService.trouverParId(id);

            if (opt.isEmpty()) {
                request.getSession().setAttribute("flashMessage", "Utilisateur introuvable.");
                response.sendRedirect(request.getContextPath() + "/utilisateurs/liste");
                return;
            }

            request.setAttribute("utilisateur", opt.get());
            request.setAttribute("roles", Role.values());
            request.setAttribute("titrePage", "Modifier un utilisateur");

            request.getRequestDispatcher("/WEB-INF/views/modifierUtilisateur.jsp")
                   .forward(request, response);

        } catch (NumberFormatException | SQLException e) {
            log("Erreur lors du chargement de l'utilisateur.", e);
            response.sendRedirect(request.getContextPath() + "/utilisateurs/liste");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        Long id = null;

        try {
            id = Long.parseLong(request.getParameter("id"));

            String nom = request.getParameter("nom");
            String prenom = request.getParameter("prenom");
            String matricule = request.getParameter("matricule");
            String sexe = request.getParameter("sexe");
            String dateNaissanceStr = request.getParameter("dateNaissance");
            String identifiant = request.getParameter("identifiant");
            String motDePasse = request.getParameter("motDePasse"); // facultatif
            String roleStr = request.getParameter("role");
            boolean actif = "true".equals(request.getParameter("actif"))
                    || "on".equals(request.getParameter("actif"));

            LocalDate dateNaissance = LocalDate.parse(dateNaissanceStr);

            Optional<Utilisateur> opt = utilisateurService.trouverParId(id);
            if (opt.isEmpty()) {
                request.getSession().setAttribute("flashMessage", "Utilisateur introuvable.");
                response.sendRedirect(request.getContextPath() + "/utilisateurs/liste");
                return;
            }

            Utilisateur u = opt.get();
            u.setNom(nom);
            u.setPrenom(prenom);
            u.setMatricule(matricule);
            u.setSexe(sexe);
            u.setDateNaissance(dateNaissance);
            u.setIdentifiant(identifiant);
            u.setRole(roleStr == null || roleStr.isBlank()
                    ? Role.VENDEUR
                    : Role.valueOf(roleStr));
            u.setActif(actif);

            utilisateurService.modifierUtilisateur(u, motDePasse);

            request.getSession().setAttribute("flashMessage",
                    "L'utilisateur a bien été modifié.");

            response.sendRedirect(request.getContextPath() + "/utilisateurs/liste");
            return;

        } catch (DateTimeParseException e) {
            request.setAttribute("messageErreur", "Date de naissance invalide.");

        } catch (IllegalArgumentException e) {
            // Couvre NumberFormatException ET les erreurs de validation
            request.setAttribute("messageErreur", e.getMessage() != null
                    ? e.getMessage()
                    : "Données invalides.");

        } catch (SQLException e) {
            log("Erreur lors de la modification de l'utilisateur.", e);
            request.setAttribute("messageErreur", "Impossible de modifier l'utilisateur.");
        }

        // En cas d'erreur, on réaffiche le formulaire avec les données existantes
        try {
            if (id != null) {
                utilisateurService.trouverParId(id)
                        .ifPresent(u -> request.setAttribute("utilisateur", u));
            }
        } catch (SQLException ignored) {
            // On ignore : on affichera au pire un formulaire vide
        }

        request.setAttribute("roles", Role.values());
        request.setAttribute("titrePage", "Modifier un utilisateur");
        request.getRequestDispatcher("/WEB-INF/views/modifierUtilisateur.jsp")
               .forward(request, response);
    }
}