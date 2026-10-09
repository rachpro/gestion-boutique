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
import java.util.Collections;
import java.util.List;

@WebServlet("/clients/liste")
public class ClientListeServlet extends HttpServlet {

    private ClientService clientService;

    @Override
    public void init() {
        clientService = new ClientService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String recherche = request.getParameter("q");

        try {
            List<Client> clients = clientService.rechercherParNom(recherche);
            request.setAttribute("clients", clients);
            request.setAttribute("recherche", recherche);
        } catch (SQLException e) {
            log("Erreur chargement clients.", e);
            request.setAttribute("clients", Collections.emptyList());
            request.setAttribute("messageErreur", "Impossible de charger les clients.");
        }

        // Message flash
        String flash = (String) request.getSession().getAttribute("flashMessage");
        if (flash != null) {
            request.setAttribute("messageSucces", flash);
            request.getSession().removeAttribute("flashMessage");
        }

        request.setAttribute("titrePage", "Liste des clients");
        request.getRequestDispatcher("/WEB-INF/views/listeClients.jsp")
               .forward(request, response);
    }
}