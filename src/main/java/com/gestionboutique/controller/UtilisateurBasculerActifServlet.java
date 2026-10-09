package com.gestionboutique.controller;

import com.gestionboutique.service.UtilisateurService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Bascule l'état actif/inactif d'un utilisateur.
 * Accessible uniquement via POST (formulaire avec bouton).
 */
@WebServlet("/utilisateurs/basculer")
public class UtilisateurBasculerActifServlet extends HttpServlet {

    private UtilisateurService utilisateurService;

    @Override
    public void init() throws ServletException {
        utilisateurService = new UtilisateurService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");

        try {
            Long id = Long.parseLong(idStr);
            utilisateurService.basculerActif(id);
            request.getSession().setAttribute("flashMessage",
                    "Le statut de l'utilisateur a été modifié.");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("flashMessage", "Identifiant invalide.");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("flashMessage", e.getMessage());
        } catch (SQLException e) {
            log("Erreur lors du basculement d'état.", e);
            request.getSession().setAttribute("flashMessage",
                    "Impossible de modifier le statut.");
        }

        response.sendRedirect(request.getContextPath() + "/utilisateurs/liste");
    }
}