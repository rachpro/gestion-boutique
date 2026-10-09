package com.gestionboutique.controller;

import com.gestionboutique.model.Achat;
import com.gestionboutique.service.AchatService;
import com.gestionboutique.service.TicketService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/tickets/imprimer")
public class TicketServlet extends HttpServlet {

    private AchatService achatService;
    private TicketService ticketService;

    @Override
    public void init() {
        achatService = new AchatService();
        ticketService = new TicketService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Optional<Achat> opt = achatService.trouverParId(id);

            if (opt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Achat introuvable");
                return;
            }

            Achat achat = opt.get();

            byte[] pdf = ticketService.genererTicketPdf(achat);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "inline; filename=\"ticket-" + achat.getId() + ".pdf\"");
            response.setContentLength(pdf.length);

            try (OutputStream out = response.getOutputStream()) {
                out.write(pdf);
                out.flush();
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Identifiant invalide");
        } catch (SQLException e) {
            log("Erreur SQL ticket", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erreur lors du chargement de l'achat");
        } catch (Exception e) {
            log("Erreur génération ticket", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erreur lors de la génération du ticket");
        }
    }
}