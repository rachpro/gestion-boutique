package com.gestionboutique.controller;

import com.gestionboutique.model.Achat;
import com.gestionboutique.service.AchatService;

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

@WebServlet("/achats/historique")
public class HistoriqueAchatsServlet extends HttpServlet {

    private AchatService achatService;

    @Override
    public void init() {
        achatService = new AchatService();
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
            List<Achat> achats = achatService.rechercherParDate(dateDebut, dateFin);
            request.setAttribute("achats", achats);
        } catch (SQLException e) {
            log("Erreur chargement historique achats.", e);
            request.setAttribute("achats", Collections.emptyList());
            request.setAttribute("messageErreur", "Impossible de charger l'historique.");
        }

        // Flash
        String flash = (String) request.getSession().getAttribute("flashMessage");
        if (flash != null) {
            request.setAttribute("messageSucces", flash);
            request.getSession().removeAttribute("flashMessage");
        }

        request.setAttribute("titrePage", "Historique des achats");
        request.getRequestDispatcher("/WEB-INF/views/historiqueAchats.jsp")
               .forward(request, response);
    }
}