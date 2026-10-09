<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>
        <%= request.getAttribute("titrePage") != null
                ? request.getAttribute("titrePage")
                : "Gestion Boutique" %>
    </title>

    <link rel="stylesheet"
          type="text/css"
          href="${pageContext.request.contextPath}/css/style.css?v=3">

    <script
        defer
        src="${pageContext.request.contextPath}/js/validation.js?v=3">
    </script>
</head>

<body>

    <header class="app-header">

        <div class="app-header-content">

            <a class="app-logo"
               href="${pageContext.request.contextPath}/dashboard">
                Gestion Boutique
            </a>

            <div class="app-user-area">

                <c:choose>
                    <%-- Utilisateur connecté --%>
                    <c:when test="${not empty sessionScope.utilisateurConnecte}">
                        <span class="app-user-name">
                            <strong>
                                ${sessionScope.utilisateurConnecte.prenom}
                                ${sessionScope.utilisateurConnecte.nom}
                            </strong>

                            <span class="app-user-role">
                                (${sessionScope.utilisateurConnecte.role.libelle})
                            </span>
                        </span>
                    </c:when>

                    <%-- Pas connecté --%>
                    <c:otherwise>
                        <span class="app-user-name">
                            Non connecté
                        </span>
                    </c:otherwise>
                </c:choose>

                <a class="logout-link"
                   href="${pageContext.request.contextPath}/logout">
                    Déconnexion
                </a>

            </div>

        </div>

    </header>