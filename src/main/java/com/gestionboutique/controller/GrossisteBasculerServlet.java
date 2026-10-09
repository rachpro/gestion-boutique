package com.gestionboutique.controller;

import com.gestionboutique.service.GrossisteService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/grossistes/basculer")
public class GrossisteBasculerServlet extends HttpServlet {

    private GrossisteService grossisteService;

    @Override
    public void init() {
        grossisteService = new GrossisteService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            grossisteService.basculerActif(id);
            request.getSession().setAttribute("flashMessage",
                    "Le statut du grossiste a été modifié.");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("flashMessage", "Identifiant invalide.");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("flashMessage", e.getMessage());
        } catch (SQLException e) {
            log("Erreur basculement grossiste.", e);
            request.getSession().setAttribute("flashMessage",
                    "Impossible de modifier le statut.");
        }

        response.sendRedirect(request.getContextPath() + "/grossistes/liste");
    }
}