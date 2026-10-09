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
import java.util.Optional;

@WebServlet("/grossistes/modifier")
public class GrossisteModifierServlet extends HttpServlet {

    private GrossisteService grossisteService;

    @Override
    public void init() {
        grossisteService = new GrossisteService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Optional<Grossiste> opt = grossisteService.trouverParId(id);

            if (opt.isEmpty()) {
                request.getSession().setAttribute("flashMessage", "Grossiste introuvable.");
                response.sendRedirect(request.getContextPath() + "/grossistes/liste");
                return;
            }

            request.setAttribute("grossiste", opt.get());
            request.setAttribute("titrePage", "Modifier un grossiste");
            request.getRequestDispatcher("/WEB-INF/views/modifierGrossiste.jsp")
                   .forward(request, response);

        } catch (NumberFormatException | SQLException e) {
            log("Erreur modification grossiste GET.", e);
            response.sendRedirect(request.getContextPath() + "/grossistes/liste");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Long id = null;
        try {
            id = Long.parseLong(request.getParameter("id"));
            Optional<Grossiste> opt = grossisteService.trouverParId(id);

            if (opt.isEmpty()) {
                request.getSession().setAttribute("flashMessage", "Grossiste introuvable.");
                response.sendRedirect(request.getContextPath() + "/grossistes/liste");
                return;
            }

            Grossiste g = opt.get();
            g.setNom(request.getParameter("nom"));
            g.setTelephone(request.getParameter("telephone"));
            g.setEmail(request.getParameter("email"));
            g.setAdresse(request.getParameter("adresse"));
            g.setContactNom(request.getParameter("contactNom"));
            g.setActif("true".equals(request.getParameter("actif"))
                    || "on".equals(request.getParameter("actif")));

            grossisteService.modifier(g);

            request.getSession().setAttribute("flashMessage", "Le grossiste a bien été modifié.");
            response.sendRedirect(request.getContextPath() + "/grossistes/liste");
            return;

        } catch (IllegalArgumentException e) {
            request.setAttribute("messageErreur", e.getMessage());
        } catch (SQLException e) {
            log("Erreur modification grossiste POST.", e);
            request.setAttribute("messageErreur", "Impossible de modifier le grossiste.");
        }

        try {
            if (id != null) {
                grossisteService.trouverParId(id).ifPresent(g -> request.setAttribute("grossiste", g));
            }
        } catch (SQLException ignored) { }

        request.setAttribute("titrePage", "Modifier un grossiste");
        request.getRequestDispatcher("/WEB-INF/views/modifierGrossiste.jsp")
               .forward(request, response);
    }
}