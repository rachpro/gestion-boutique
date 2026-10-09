package com.gestionboutique.controller;

import com.gestionboutique.model.Livraison;
import com.gestionboutique.service.LivraisonService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;

@WebServlet("/livraisons/historique")
public class HistoriqueLivraisonsServlet extends HttpServlet {

    private LivraisonService livraisonService;

    @Override
    public void init() {
        livraisonService = new LivraisonService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String dateDebutStr = request.getParameter("dateDebut");
        String dateFinStr = request.getParameter("dateFin");

        LocalDate dateDebut = null;
        LocalDate dateFin = null;

        try {
            if (dateDebutStr != null && !dateDebutStr.isBlank()) {
                dateDebut = LocalDate.parse(dateDebutStr);
            }
            if (dateFinStr != null && !dateFinStr.isBlank()) {
                dateFin = LocalDate.parse(dateFinStr);
            }
        } catch (DateTimeParseException e) {
            request.setAttribute("messageErreur", "Format de date invalide.");
        }

        try {
            List<Livraison> livraisons = livraisonService.rechercherParDate(dateDebut, dateFin);
            request.setAttribute("livraisons", livraisons);
        } catch (SQLException e) {
            log("Erreur chargement historique livraisons.", e);
            request.setAttribute("livraisons", Collections.emptyList());
            request.setAttribute("messageErreur", "Impossible de charger l'historique.");
        }

        String flash = (String) request.getSession().getAttribute("flashMessage");
        if (flash != null) {
            request.setAttribute("messageSucces", flash);
            request.getSession().removeAttribute("flashMessage");
        }

        request.setAttribute("titrePage", "Historique des livraisons");
        request.getRequestDispatcher("/WEB-INF/views/historiqueLivraisons.jsp")
               .forward(request, response);
    }
}