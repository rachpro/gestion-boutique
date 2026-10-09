package com.gestionboutique.filter;

import com.gestionboutique.model.Role;
import com.gestionboutique.model.Utilisateur;
import com.gestionboutique.util.SessionUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Filtre d'administration.
 *
 * <p>Restreint l'accès aux pages de gestion des utilisateurs
 * ({@code /utilisateurs/*}) aux seuls utilisateurs ayant le rôle ADMIN.</p>
 *
 * <p>Un utilisateur non-admin connecté est redirigé vers le tableau de bord
 * avec un message d'erreur. Un utilisateur non connecté est redirigé vers
 * la page de connexion (via {@link AuthFilter} qui s'exécute en amont).</p>
 */
@WebFilter(urlPatterns = {"/utilisateurs/*"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Utilisateur utilisateur = SessionUtil.getUtilisateur(httpRequest);

        // Utilisateur non connecté → redirection login (géré par AuthFilter en amont)
        if (utilisateur == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        // Utilisateur connecté mais pas admin → redirection dashboard
        if (utilisateur.getRole() != Role.ADMIN) {
            httpResponse.sendRedirect(
                    httpRequest.getContextPath() + "/dashboard?acces=refuse"
            );
            return;
        }

        // Admin → laisser passer
        chain.doFilter(request, response);
    }
}