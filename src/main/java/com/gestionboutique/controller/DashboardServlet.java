package com.gestionboutique.controller;

import com.gestionboutique.model.Utilisateur;
import com.gestionboutique.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        Utilisateur utilisateur = SessionUtil.getUtilisateur(request);

        // Message si l'accès a été refusé
        String acces = request.getParameter("acces");
        if ("refuse".equals(acces)) {
            request.setAttribute("messageErreur",
                    "Accès refusé : vous n'avez pas les droits nécessaires.");
        }

        request.setAttribute("utilisateur", utilisateur);
        request.setAttribute("titrePage", "Tableau de bord");

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp")
               .forward(request, response);
    }
}