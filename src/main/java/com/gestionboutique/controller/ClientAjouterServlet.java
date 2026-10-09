package com.gestionboutique.controller;

import com.gestionboutique.model.Client;
import com.gestionboutique.service.ClientService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/clients/nouveau")
public class ClientAjouterServlet extends HttpServlet {

    private ClientService clientService;

    @Override
    public void init() {
        clientService = new ClientService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("titrePage", "Ajouter un client");
        request.getRequestDispatcher("/WEB-INF/views/ajoutClient.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            Client c = new Client();
            c.setNom(request.getParameter("nom"));
            c.setPrenom(request.getParameter("prenom"));
            c.setTelephone(request.getParameter("telephone"));
            c.setEmail(request.getParameter("email"));
            c.setAdresse(request.getParameter("adresse"));
            c.setActif(true);

            clientService.ajouter(c);

            request.getSession().setAttribute("flashMessage",
                    "Le client " + c.getNomComplet() + " a bien été enregistré.");

            response.sendRedirect(request.getContextPath() + "/clients/liste");
            return;

        } catch (IllegalArgumentException e) {
            request.setAttribute("messageErreur", e.getMessage());
        } catch (SQLException e) {
            log("Erreur ajout client.", e);
            request.setAttribute("messageErreur", "Impossible d'enregistrer le client.");
        }

        request.setAttribute("titrePage", "Ajouter un client");
        request.getRequestDispatcher("/WEB-INF/views/ajoutClient.jsp")
               .forward(request, response);
    }
}