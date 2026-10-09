package com.gestionboutique.controller;

import com.gestionboutique.model.Grossiste;
import com.gestionboutique.service.GrossisteService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@WebServlet("/grossistes/liste")
public class GrossisteListeServlet extends HttpServlet {

    private GrossisteService grossisteService;

    @Override
    public void init() {
        grossisteService = new GrossisteService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String recherche = request.getParameter("q");

        try {
            List<Grossiste> grossistes = grossisteService.rechercherParNom(recherche);
            request.setAttribute("grossistes", grossistes);
            request.setAttribute("recherche", recherche);
        } catch (SQLException e) {
            log("Erreur chargement grossistes.", e);
            request.setAttribute("grossistes", Collections.emptyList());
            request.setAttribute("messageErreur", "Impossible de charger les grossistes.");
        }

        String flash = (String) request.getSession().getAttribute("flashMessage");
        if (flash != null) {
            request.setAttribute("messageSucces", flash);
            request.getSession().removeAttribute("flashMessage");
        }

        request.setAttribute("titrePage", "Liste des grossistes");
        request.getRequestDispatcher("/WEB-INF/views/listeGrossistes.jsp")
               .forward(request, response);
    }
}