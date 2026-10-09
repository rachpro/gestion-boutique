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

@WebServlet("/grossistes/nouveau")
public class GrossisteAjouterServlet extends HttpServlet {

    private GrossisteService grossisteService;

    @Override
    public void init() {
        grossisteService = new GrossisteService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("titrePage", "Ajouter un grossiste");
        request.getRequestDispatcher("/WEB-INF/views/ajoutGrossiste.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            Grossiste g = new Grossiste();
            g.setNom(request.getParameter("nom"));
            g.setTelephone(request.getParameter("telephone"));
            g.setEmail(request.getParameter("email"));
            g.setAdresse(request.getParameter("adresse"));
            g.setContactNom(request.getParameter("contactNom"));
            g.setActif(true);

            grossisteService.ajouter(g);

            request.getSession().setAttribute("flashMessage",
                    "Le grossiste " + g.getNom() + " a bien été enregistré.");

            response.sendRedirect(request.getContextPath() + "/grossistes/liste");
            return;

        } catch (IllegalArgumentException e) {
            request.setAttribute("messageErreur", e.getMessage());
        } catch (SQLException e) {
            log("Erreur ajout grossiste.", e);
            request.setAttribute("messageErreur", "Impossible d'enregistrer le grossiste.");
        }

        request.setAttribute("titrePage", "Ajouter un grossiste");
        request.getRequestDispatcher("/WEB-INF/views/ajoutGrossiste.jsp")
               .forward(request, response);
    }
}