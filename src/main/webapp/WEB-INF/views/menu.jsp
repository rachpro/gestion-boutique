<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<nav class="app-menu">

    <div class="menu-title">
        Menu principal
    </div>

    <ul class="menu-list">

        <li class="menu-item">
            <a href="${pageContext.request.contextPath}/dashboard">
                Tableau de bord
            </a>
        </li>

        <li class="menu-item">
            <a href="${pageContext.request.contextPath}/utilisateurs/liste">
                Liste des utilisateurs
            </a>
        </li>

        <li class="menu-item">
            <a href="${pageContext.request.contextPath}/utilisateurs/nouveau">
                Ajouter un utilisateur
            </a>
        </li>

        <li class="menu-item">
            <a href="${pageContext.request.contextPath}/produits/ajouter">
                Ajouter un produit
            </a>
        </li>

        <li class="menu-item">
            <a href="${pageContext.request.contextPath}/produits/recherche">
                Rechercher un produit
            </a>
        </li>

        <li class="menu-item">
            <a href="${pageContext.request.contextPath}/clients/liste">
                Clients
            </a>
        </li>

        <li class="menu-item">
            <a href="${pageContext.request.contextPath}/achats/nouveau">
                Nouvel achat
            </a>
        </li>

        <li class="menu-item">
            <a href="${pageContext.request.contextPath}/livraisons/nouvelle">
                Nouvelle livraison
            </a>
        </li>

        <li class="menu-item">
            <a href="${pageContext.request.contextPath}/achats/historique">
                Historique des achats
            </a>
        </li>

        <li class="menu-item">
            <a href="${pageContext.request.contextPath}/livraisons/historique">
                Historique des livraisons
            </a>
        </li>
      
     <li class="menu-item">
    <a href="${pageContext.request.contextPath}/grossistes/liste">
        Grossistes
    </a>
</li>

    </ul>

</nav>