package com.gestionboutique.controller;

import com.gestionboutique.model.Utilisateur;
import com.gestionboutique.service.UtilisateurService;
import com.gestionboutique.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginServlet extends HttpServlet {

    private UtilisateurService utilisateurService;

    @Override
    public void init() throws ServletException {
        utilisateurService = new UtilisateurService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/logout".equals(path)) {
            SessionUtil.deconnecter(request);
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Affiche la page de connexion
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String identifiant = request.getParameter("identifiant");
        String motDePasse = request.getParameter("motDePasse");

        try {
            Optional<Utilisateur> opt = utilisateurService.authentifier(identifiant, motDePasse);

            if (opt.isPresent()) {
                SessionUtil.setUtilisateur(request, opt.get());
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }

            request.setAttribute("messageErreur", "Identifiant ou mot de passe incorrect.");
            request.setAttribute("identifiant", identifiant);
            request.getRequestDispatcher("/login.jsp").forward(request, response);

        } catch (SQLException exception) {
            log("Erreur lors de l'authentification.", exception);
            request.setAttribute("messageErreur",
                    "Une erreur est survenue. Veuillez réessayer.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}