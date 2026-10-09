package com.gestionboutique.util;

import com.gestionboutique.model.Utilisateur;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Utilitaire pour manipuler la session HTTP de l'utilisateur connecté.
 */
public final class SessionUtil {

    public static final String SESSION_USER_ATTR = "utilisateurConnecte";

    private SessionUtil() {
    }

    public static void setUtilisateur(HttpServletRequest request, Utilisateur utilisateur) {
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_USER_ATTR, utilisateur);
        session.setMaxInactiveInterval(30 * 60); // 30 minutes
    }

    public static Utilisateur getUtilisateur(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (Utilisateur) session.getAttribute(SESSION_USER_ATTR);
    }

    public static boolean estConnecte(HttpServletRequest request) {
        return getUtilisateur(request) != null;
    }

    public static void deconnecter(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}