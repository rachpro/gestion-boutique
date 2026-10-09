<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des clients - Gestion Boutique</title>
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<main class="page-container">

    <header class="page-header">
        <h1>Liste des clients</h1>
        <p>Gérez les clients de votre boutique.</p>
    </header>

    <c:if test="${not empty messageSucces}">
        <div class="alert-success">${messageSucces}</div>
    </c:if>
    <c:if test="${not empty messageErreur}">
        <div class="alert-error">${messageErreur}</div>
    </c:if>

    <div class="actions-bar">

        <form method="get"
              action="${pageContext.request.contextPath}/clients/liste"
              class="search-bar">
            <input type="text" name="q"
                   value="${recherche}"
                   placeholder="Rechercher par nom ou prénom..."
                   class="form-control">
            <button type="submit" class="btn-primary">Rechercher</button>
        </form>

        <a href="${pageContext.request.contextPath}/clients/nouveau"
           class="btn-primary">+ Ajouter un client</a>

    </div>

    <div class="table-container">
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Téléphone</th>
                    <th>Email</th>
                    <th>Adresse</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="c" items="${clients}">
                    <tr>
                        <td>${c.id}</td>
                        <td>${c.nom}</td>
                        <td>${c.prenom}</td>
                        <td>${c.telephone}</td>
                        <td>${c.email != null ? c.email : '-'}</td>
                        <td>${c.adresse != null ? c.adresse : '-'}</td>
                        <td>
                            <c:choose>
                                <c:when test="${c.actif}">
                                    <span class="badge badge-actif">Actif</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-inactif">Inactif</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="actions-cell">
                            <a href="${pageContext.request.contextPath}/clients/modifier?id=${c.id}"
                               class="btn-small btn-edit">Modifier</a>

                            <form method="post"
                                  action="${pageContext.request.contextPath}/clients/basculer"
                                  style="display:inline;"
                                  onsubmit="return confirm('Confirmer ?');">
                                <input type="hidden" name="id" value="${c.id}">
                                <button type="submit"
                                        class="btn-small ${c.actif ? 'btn-deactivate' : 'btn-activate'}">
                                    ${c.actif ? 'Désactiver' : 'Activer'}
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty clients}">
                    <tr>
                        <td colspan="8" class="empty-row">Aucun client enregistré.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

</main>

<%@ include file="footer.jsp" %>
</body>
</html>