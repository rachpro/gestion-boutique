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
import java.util.Optional;

@WebServlet("/livraisons/detail")
public class DetailLivraisonServlet extends HttpServlet {

    private LivraisonService livraisonService;

    @Override
    public void init() {
        livraisonService = new LivraisonService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Optional<Livraison> opt = livraisonService.trouverParId(id);

            if (opt.isEmpty()) {
                request.getSession().setAttribute("flashMessage", "Livraison introuvable.");
                response.sendRedirect(request.getContextPath() + "/livraisons/historique");
                return;
            }

            request.setAttribute("livraison", opt.get());
            request.setAttribute("titrePage", "Détail de la livraison");
            request.getRequestDispatcher("/WEB-INF/views/detailLivraison.jsp")
                   .forward(request, response);

        } catch (NumberFormatException | SQLException e) {
            log("Erreur détail livraison.", e);
            response.sendRedirect(request.getContextPath() + "/livraisons/historique");
        }
    }
}