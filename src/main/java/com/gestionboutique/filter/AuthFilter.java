package com.gestionboutique.filter;

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
 * Filtre d'authentification.
 *
 * <p>Intercepte toutes les requêtes vers les ressources protégées.
 * Si l'utilisateur n'est pas connecté, il est redirigé vers la page
 * de connexion.</p>
 *
 * <p>Les URLs couvertes :</p>
 * <ul>
 *     <li>{@code /dashboard}</li>
 *     <li>{@code /produits/*}</li>
 *     <li>{@code /utilisateurs/*}</li>
 *     <li>{@code /achats/*}</li>
 *     <li>{@code /livraisons/*}</li>
 *     <li>{@code /clients/*}</li>
 *     <li>{@code /grossistes/*}</li>
 *     <li>{@code /ticket}</li>
 *     <li>{@code /historique/*}</li>
 * </ul>
 *
 * <p>Les JSP situées dans {@code /WEB-INF/views/} ne sont accessibles
 * que via un forward de Servlet, elles sont donc protégées par
 * construction.</p>
 */
@WebFilter(urlPatterns = {
        "/dashboard",
        "/produits/*",
        "/utilisateurs/*",
        "/achats/*",
        "/livraisons/*",
        "/clients/*",
        "/grossistes/*",
        "/ticket",
        "/historique/*"
})
public class AuthFilter implements Filter {

    /**
     * URL de redirection en cas de non-authentification.
     */
    private static final String LOGIN_URL = "/login";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Déjà connecté → laisser passer
        if (SessionUtil.estConnecte(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        // Non connecté → redirection vers la page de connexion
        String contextPath = httpRequest.getContextPath();
        httpResponse.sendRedirect(contextPath + LOGIN_URL);
    }
}