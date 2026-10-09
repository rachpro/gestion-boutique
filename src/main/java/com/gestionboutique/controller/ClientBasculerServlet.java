package com.gestionboutique.controller;

import com.gestionboutique.service.ClientService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/clients/basculer")
public class ClientBasculerServlet extends HttpServlet {

    private ClientService clientService;

    @Override
    public void init() {
        clientService = new ClientService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            clientService.basculerActif(id);
            request.getSession().setAttribute("flashMessage",
                    "Le statut du client a été modifié.");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("flashMessage", "Identifiant invalide.");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("flashMessage", e.getMessage());
        } catch (SQLException e) {
            log("Erreur basculement client.", e);
            request.getSession().setAttribute("flashMessage",
                    "Impossible de modifier le statut.");
        }

        response.sendRedirect(request.getContextPath() + "/clients/liste");
    }
}