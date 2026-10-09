<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de bord</title>
</head>

<body>

<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<main class="page-content">

    <section class="page-title-section">
        <div>
            <h1>Tableau de bord</h1>
            <p>Bienvenue dans votre espace de gestion.</p>
        </div>
    </section>

    <c:if test="${not empty messageErreur}">
        <div class="alert-error">${messageErreur}</div>
    </c:if>

    <section class="dashboard-cards">

        <article class="dashboard-card dashboard-card-blue">
            <div class="dashboard-card-icon">P</div>
            <div class="dashboard-card-content">
                <h2>Produits</h2>
                <p>Gérer les produits de la boutique</p>
                <a href="${pageContext.request.contextPath}/produits/recherche"
                   class="dashboard-card-link">
                    Consulter les produits
                </a>
            </div>
        </article>

        <article class="dashboard-card dashboard-card-green">
            <div class="dashboard-card-icon">C</div>
            <div class="dashboard-card-content">
                <h2>Clients</h2>
                <p>Gérer les clients de la boutique</p>
                <a href="${pageContext.request.contextPath}/clients/liste"
                   class="dashboard-card-link">
                    Consulter les clients
                </a>
            </div>
        </article>

        <article class="dashboard-card dashboard-card-orange">
            <div class="dashboard-card-icon">A</div>
            <div class="dashboard-card-content">
                <h2>Achats</h2>
                <p>Enregistrer et consulter les achats</p>
                <a href="${pageContext.request.contextPath}/achats/historique"
                   class="dashboard-card-link">
                    Voir les achats
                </a>
            </div>
        </article>

        <article class="dashboard-card dashboard-card-purple">
            <div class="dashboard-card-icon">L</div>
            <div class="dashboard-card-content">
                <h2>Livraisons</h2>
                <p>Gérer les livraisons reçues</p>
                <a href="${pageContext.request.contextPath}/livraisons/historique"
                   class="dashboard-card-link">
                    Voir les livraisons
                </a>
            </div>
        </article>

    </section>

    <section class="dashboard-welcome-box">
        <h2>Bienvenue dans Gestion Boutique</h2>
        <p>Utilisez le menu pour accéder aux différentes fonctionnalités de l'application.</p>
    </section>

</main>

<%@ include file="footer.jsp" %>

</body>
</html>