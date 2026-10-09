package com.gestionboutique.controller;

import com.gestionboutique.model.Utilisateur;
import com.gestionboutique.service.UtilisateurService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@WebServlet("/utilisateurs/liste")
public class UtilisateurListeServlet extends HttpServlet {

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

        try {
            List<Utilisateur> utilisateurs = utilisateurService.listerTous();
            request.setAttribute("utilisateurs", utilisateurs);

        } catch (SQLException e) {
            log("Erreur lors du chargement des utilisateurs.", e);
            request.setAttribute("utilisateurs", Collections.emptyList());
            request.setAttribute("messageErreur",
                    "Impossible de charger la liste des utilisateurs.");
        }

        // Récupère le message flash éventuel (après modif)
        String flash = (String) request.getSession().getAttribute("flashMessage");
        if (flash != null) {
            request.setAttribute("messageSucces", flash);
            request.getSession().removeAttribute("flashMessage");
        }

        request.setAttribute("titrePage", "Liste des utilisateurs");
        request.getRequestDispatcher("/WEB-INF/views/listeUtilisateurs.jsp")
               .forward(request, response);
    }
}