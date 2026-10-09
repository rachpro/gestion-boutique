package com.gestionboutique.controller;

import com.gestionboutique.model.LigneLivraison;
import com.gestionboutique.model.Livraison;
import com.gestionboutique.model.Utilisateur;
import com.gestionboutique.service.GrossisteService;
import com.gestionboutique.service.LivraisonService;
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

@WebServlet("/livraisons/nouvelle")
public class LivraisonServlet extends HttpServlet {

    private LivraisonService livraisonService;
    private GrossisteService grossisteService;
    private ProduitService produitService;

    @Override
    public void init() {
        livraisonService = new LivraisonService();
        grossisteService = new GrossisteService();
        produitService = new ProduitService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            request.setAttribute("grossistes", grossisteService.listerTous());
            request.setAttribute("produits", produitService.rechercherProduits(null));
        } catch (SQLException e) {
            log("Erreur chargement données livraison.", e);
            request.setAttribute("messageErreur", "Impossible de charger les données.");
        }

        request.setAttribute("titrePage", "Nouvelle livraison");
        request.getRequestDispatcher("/WEB-INF/views/nouvelleLivraison.jsp")
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
            Long grossisteId = Long.parseLong(request.getParameter("grossisteId"));
            String reference = request.getParameter("reference");

            String[] produitIds = request.getParameterValues("produitId");
            String[] quantites = request.getParameterValues("quantite");

            if (produitIds == null || produitIds.length == 0) {
                throw new IllegalArgumentException("Veuillez ajouter au moins un produit.");
            }

            Livraison livraison = new Livraison();
            livraison.setGrossisteId(grossisteId);
            livraison.setUtilisateurId(utilisateur.getId());
            livraison.setReference(reference);

            List<LigneLivraison> lignes = new ArrayList<>();

            for (int i = 0; i < produitIds.length; i++) {
                String pidStr = produitIds[i];
                String qteStr = (quantites != null && i < quantites.length) ? quantites[i] : null;

                if (pidStr == null || pidStr.isBlank()) continue;
                if (qteStr == null || qteStr.isBlank()) continue;

                Long pid = Long.parseLong(pidStr);
                int qte = Integer.parseInt(qteStr);

                if (qte <= 0) continue;

                LigneLivraison ligne = new LigneLivraison();
                ligne.setProduitId(pid);
                ligne.setQuantite(qte);
                ligne.setPrixUnitaire(BigDecimal.ZERO);
                ligne.setSousTotal(BigDecimal.ZERO);

                lignes.add(ligne);
            }

            if (lignes.isEmpty()) {
                throw new IllegalArgumentException("Aucune ligne valide dans la livraison.");
            }

            livraison.setLignes(lignes);

            livraisonService.enregistrerLivraison(livraison);

            request.getSession().setAttribute("flashMessage",
                    "Livraison #" + livraison.getId() + " enregistrée. Total : "
                            + livraison.getTotal() + " FCFA");

            response.sendRedirect(request.getContextPath() + "/livraisons/historique");
            return;

        } catch (NumberFormatException e) {
            request.setAttribute("messageErreur", "Données invalides dans le formulaire.");
        } catch (IllegalArgumentException e) {
            request.setAttribute("messageErreur", e.getMessage());
        } catch (SQLException e) {
            log("Erreur enregistrement livraison.", e);
            request.setAttribute("messageErreur", "Impossible d'enregistrer la livraison.");
        }

        try {
            request.setAttribute("grossistes", grossisteService.listerTous());
            request.setAttribute("produits", produitService.rechercherProduits(null));
        } catch (SQLException ignored) { }

        request.setAttribute("titrePage", "Nouvelle livraison");
        request.getRequestDispatcher("/WEB-INF/views/nouvelleLivraison.jsp")
               .forward(request, response);
    }
}