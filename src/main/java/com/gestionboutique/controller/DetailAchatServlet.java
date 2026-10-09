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
import java.util.Optional;

@WebServlet("/achats/detail")
public class DetailAchatServlet extends HttpServlet {

    private AchatService achatService;

    @Override
    public void init() {
        achatService = new AchatService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Optional<Achat> opt = achatService.trouverParId(id);

            if (opt.isEmpty()) {
                request.getSession().setAttribute("flashMessage", "Achat introuvable.");
                response.sendRedirect(request.getContextPath() + "/achats/historique");
                return;
            }

            request.setAttribute("achat", opt.get());
            request.setAttribute("titrePage", "Détail de l'achat");
            request.getRequestDispatcher("/WEB-INF/views/detailAchat.jsp")
                   .forward(request, response);

        } catch (NumberFormatException | SQLException e) {
            log("Erreur détail achat.", e);
            response.sendRedirect(request.getContextPath() + "/achats/historique");
        }
    }
}