package com.gestionboutique.controller;

import com.gestionboutique.model.Achat;
import com.gestionboutique.model.Client;
import com.gestionboutique.model.LigneAchat;
import com.gestionboutique.model.Produit;
import com.gestionboutique.model.Utilisateur;
import com.gestionboutique.service.AchatService;
import com.gestionboutique.service.ClientService;
import com.gestionboutique.service.ProduitService;
import com.gestionboutique.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/achats/nouveau")
public class AchatServlet extends HttpServlet {

    private AchatService achatService;
    private ClientService clientService;
    private ProduitService produitService;

    @Override
    public void init() {
        achatService = new AchatService();
        clientService = new ClientService();
        produitService = new ProduitService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            request.setAttribute("clients", clientService.listerTous());
            request.setAttribute("produits", produitService.rechercherProduits(null));
        } catch (SQLException e) {
            log("Erreur chargement données achat.", e);
            request.setAttribute("messageErreur", "Impossible de charger les données.");
        }

        request.setAttribute("titrePage", "Nouvel achat");
        request.getRequestDispatcher("/WEB-INF/views/nouvelAchat.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        Utilisateur utilisateur = SessionUtil.getUtilisateur(request);
        if (utilisateur == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Long clientId = Long.parseLong(request.getParameter("clientId"));

            String[] produitIds = request.getParameterValues("produitId");
            String[] quantites = request.getParameterValues("quantite");

            if (produitIds == null || produitIds.length == 0) {
                throw new IllegalArgumentException("Veuillez ajouter au moins un produit.");
            }

            Achat achat = new Achat();
            achat.setClientId(clientId);
            achat.setUtilisateurId(utilisateur.getId());
            achat.setStatut("PAYE");

            List<LigneAchat> lignes = new ArrayList<>();

            for (int i = 0; i < produitIds.length; i++) {
                String pidStr = produitIds[i];
                String qteStr = (quantites != null && i < quantites.length) ? quantites[i] : null;

                if (pidStr == null || pidStr.isBlank()) continue;
                if (qteStr == null || qteStr.isBlank()) continue;

                Long pid = Long.parseLong(pidStr);
                int qte = Integer.parseInt(qteStr);

                if (qte <= 0) continue;

                // On laisse le service fixer le prix et valider le stock
                LigneAchat ligne = new LigneAchat();
                ligne.setProduitId(pid);
                ligne.setQuantite(qte);
                ligne.setPrixUnitaire(BigDecimal.ZERO); // sera corrigé par le service
                ligne.setSousTotal(BigDecimal.ZERO);

                lignes.add(ligne);
            }

            if (lignes.isEmpty()) {
                throw new IllegalArgumentException("Aucune ligne valide dans l'achat.");
            }

            achat.setLignes(lignes);

            achatService.enregistrerAchat(achat);

            request.getSession().setAttribute("flashMessage",
                    "Achat #" + achat.getId() + " enregistré avec succès. Total : "
                            + achat.getTotal() + " FCFA");

            response.sendRedirect(request.getContextPath() + "/achats/historique");
            return;

        } catch (NumberFormatException e) {
            request.setAttribute("messageErreur", "Données invalides dans le formulaire.");
        } catch (IllegalArgumentException e) {
            request.setAttribute("messageErreur", e.getMessage());
        } catch (SQLException e) {
            log("Erreur enregistrement achat.", e);
            request.setAttribute("messageErreur", "Impossible d'enregistrer l'achat.");
        }

        // En cas d'erreur, on recharge le formulaire
        try {
            request.setAttribute("clients", clientService.listerTous());
            request.setAttribute("produits", produitService.rechercherProduits(null));
        } catch (SQLException ignored) { }

        request.setAttribute("titrePage", "Nouvel achat");
        request.getRequestDispatcher("/WEB-INF/views/nouvelAchat.jsp")
               .forward(request, response);
    }
}