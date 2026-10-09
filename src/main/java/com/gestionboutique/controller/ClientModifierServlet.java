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
import java.util.Optional;

@WebServlet("/clients/modifier")
public class ClientModifierServlet extends HttpServlet {

    private ClientService clientService;

    @Override
    public void init() {
        clientService = new ClientService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Optional<Client> opt = clientService.trouverParId(id);

            if (opt.isEmpty()) {
                request.getSession().setAttribute("flashMessage", "Client introuvable.");
                response.sendRedirect(request.getContextPath() + "/clients/liste");
                return;
            }

            request.setAttribute("client", opt.get());
            request.setAttribute("titrePage", "Modifier un client");
            request.getRequestDispatcher("/WEB-INF/views/modifierClient.jsp")
                   .forward(request, response);

        } catch (NumberFormatException | SQLException e) {
            log("Erreur modification client GET.", e);
            response.sendRedirect(request.getContextPath() + "/clients/liste");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Long id = null;
        try {
            id = Long.parseLong(request.getParameter("id"));
            Optional<Client> opt = clientService.trouverParId(id);

            if (opt.isEmpty()) {
                request.getSession().setAttribute("flashMessage", "Client introuvable.");
                response.sendRedirect(request.getContextPath() + "/clients/liste");
                return;
            }

            Client c = opt.get();
            c.setNom(request.getParameter("nom"));
            c.setPrenom(request.getParameter("prenom"));
            c.setTelephone(request.getParameter("telephone"));
            c.setEmail(request.getParameter("email"));
            c.setAdresse(request.getParameter("adresse"));
            c.setActif("true".equals(request.getParameter("actif"))
                    || "on".equals(request.getParameter("actif")));

            clientService.modifier(c);

            request.getSession().setAttribute("flashMessage", "Le client a bien été modifié.");
            response.sendRedirect(request.getContextPath() + "/clients/liste");
            return;

        } catch (IllegalArgumentException e) {
            request.setAttribute("messageErreur", e.getMessage());
        } catch (SQLException e) {
            log("Erreur modification client POST.", e);
            request.setAttribute("messageErreur", "Impossible de modifier le client.");
        }

        try {
            if (id != null) {
                clientService.trouverParId(id).ifPresent(cl -> request.setAttribute("client", cl));
            }
        } catch (SQLException ignored) { }

        request.setAttribute("titrePage", "Modifier un client");
        request.getRequestDispatcher("/WEB-INF/views/modifierClient.jsp")
               .forward(request, response);
    }
}