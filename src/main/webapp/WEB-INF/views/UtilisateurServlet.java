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

@WebServlet("/utilisateurs/nouveau")
public class UtilisateurServlet extends HttpServlet {

    private UtilisateurService utilisateurService;

    @Override
    public void init() throws ServletException {
        utilisateurService = new UtilisateurService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        request.setAttribute("titrePage", "Ajouter un utilisateur");
        request.setAttribute("roles", Role.values());

        request.getRequestDispatcher("/WEB-INF/views/ajoutUtilisateur.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String matricule = request.getParameter("matricule");
        String sexe = request.getParameter("sexe");
        String dateNaissanceStr = request.getParameter("dateNaissance");
        String identifiant = request.getParameter("identifiant");
        String motDePasse = request.getParameter("motDePasse");
        String confirmation = request.getParameter("confirmationMotDePasse");
        String roleStr = request.getParameter("role");

        try {
            if (motDePasse == null || !motDePasse.equals(confirmation)) {
                throw new IllegalArgumentException(
                        "Les deux mots de passe ne correspondent pas."
                );
            }

            LocalDate dateNaissance = LocalDate.parse(dateNaissanceStr);

            Utilisateur u = new Utilisateur();
            u.setNom(nom);
            u.setPrenom(prenom);
            u.setMatricule(matricule);
            u.setSexe(sexe);
            u.setDateNaissance(dateNaissance);
            u.setIdentifiant(identifiant);
            u.setRole(roleStr == null || roleStr.isBlank()
                    ? Role.VENDEUR
                    : Role.valueOf(roleStr));
            u.setActif(true);

            utilisateurService.ajouterUtilisateur(u, motDePasse);

            // Message flash + redirection vers la liste
            request.getSession().setAttribute("flashMessage",
                    "L'utilisateur " + u.getPrenom() + " " + u.getNom()
                            + " a bien été enregistré.");

            response.sendRedirect(request.getContextPath() + "/utilisateurs/liste");
            return;

        } catch (DateTimeParseException e) {
            request.setAttribute("messageErreur", "Date de naissance invalide.");
        } catch (IllegalArgumentException e) {
            request.setAttribute("messageErreur", e.getMessage());
        } catch (SQLException e) {
            log("Erreur lors de l'enregistrement de l'utilisateur.", e);
            request.setAttribute("messageErreur",
                    "Impossible d'enregistrer l'utilisateur.");
        }

        // En cas d'erreur, on réaffiche le formulaire avec les données saisies
        request.setAttribute("titrePage", "Ajouter un utilisateur");
        request.setAttribute("roles", Role.values());

        request.getRequestDispatcher("/WEB-INF/views/ajoutUtilisateur.jsp")
               .forward(request, response);
    }
}