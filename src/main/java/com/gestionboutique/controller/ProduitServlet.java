package com.gestionboutique.controller;

import com.gestionboutique.model.Produit;
import com.gestionboutique.service.ProduitService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@WebServlet(urlPatterns = {"/produits/ajouter", "/produits/recherche"})
public class ProduitServlet extends HttpServlet {

    private ProduitService produitService;

    @Override
    public void init() throws ServletException {
        produitService = new ProduitService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = request.getServletPath();

        if ("/produits/ajouter".equals(path)) {
            request.setAttribute("titrePage", "Ajouter un produit");
            request.getRequestDispatcher("/WEB-INF/views/ajoutProduit.jsp")
                   .forward(request, response);
        } else {
            // /produits/recherche
            String nomRecherche = request.getParameter("nom");
            if (nomRecherche != null) {
                nomRecherche = nomRecherche.trim();
            }

            try {
                List<Produit> produits = produitService.rechercherProduits(nomRecherche);
                request.setAttribute("produits", produits);
                request.setAttribute("nomRecherche", nomRecherche);
            } catch (SQLException exception) {
                log("Erreur lors de la recherche des produits.", exception);
                request.setAttribute("produits", Collections.emptyList());
                request.setAttribute("messageErreur", "Impossible de charger les produits.");
            }

            request.setAttribute("titrePage", "Rechercher un produit");
            request.getRequestDispatcher("/WEB-INF/views/rechercheProduit.jsp")
                   .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String nom = request.getParameter("nom");
        String prixAchatStr = request.getParameter("prixAchat");
        String prixVenteStr = request.getParameter("prixVente");
        String stockStr = request.getParameter("stock");

        try {
            if (nom == null || nom.trim().isEmpty()) {
                throw new IllegalArgumentException("Le nom du produit est obligatoire.");
            }

            Produit produit = new Produit();
            produit.setNom(nom.trim());
            produit.setPrixAchat(BigDecimal.valueOf(parseDouble(prixAchatStr, "prix d'achat")));
            produit.setPrixVente(BigDecimal.valueOf(parseDouble(prixVenteStr, "prix de vente")));
            produit.setStock(parseInt(stockStr, "stock"));

            produitService.ajouterProduit(produit);

            request.setAttribute("messageSucces", "Le produit a bien été enregistré.");

        } catch (IllegalArgumentException exception) {
            request.setAttribute("messageErreur", exception.getMessage());
        } catch (SQLException exception) {
            log("Erreur lors de l'enregistrement du produit.", exception);
            request.setAttribute("messageErreur", "Impossible d'enregistrer le produit.");
        }

        request.setAttribute("titrePage", "Ajouter un produit");
        request.getRequestDispatcher("/WEB-INF/views/ajoutProduit.jsp")
               .forward(request, response);
    }

    private double parseDouble(String valeur, String champ) throws IllegalArgumentException {
        if (valeur == null || valeur.trim().isEmpty()) {
            throw new IllegalArgumentException("Le " + champ + " est obligatoire.");
        }
        try {
            double resultat = Double.parseDouble(valeur.trim());
            if (resultat < 0) {
                throw new IllegalArgumentException("Le " + champ + " doit être positif.");
            }
            return resultat;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le " + champ + " est invalide.");
        }
    }

    private int parseInt(String valeur, String champ) throws IllegalArgumentException {
        if (valeur == null || valeur.trim().isEmpty()) {
            throw new IllegalArgumentException("Le " + champ + " est obligatoire.");
        }
        try {
            int resultat = Integer.parseInt(valeur.trim());
            if (resultat < 0) {
                throw new IllegalArgumentException("Le " + champ + " doit être positif.");
            }
            return resultat;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le " + champ + " est invalide.");
        }
    }
}