<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des grossistes - Gestion Boutique</title>
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<main class="page-container">

    <header class="page-header">
        <h1>Liste des grossistes</h1>
        <p>Gérez vos fournisseurs et grossistes.</p>
    </header>

    <c:if test="${not empty messageSucces}">
        <div class="alert-success">${messageSucces}</div>
    </c:if>
    <c:if test="${not empty messageErreur}">
        <div class="alert-error">${messageErreur}</div>
    </c:if>

    <div class="actions-bar">
        <form method="get"
              action="${pageContext.request.contextPath}/grossistes/liste"
              class="search-bar">
            <input type="text" name="q"
                   value="${recherche}"
                   placeholder="Rechercher par nom..."
                   class="form-control">
            <button type="submit" class="btn-primary">Rechercher</button>
        </form>

        <a href="${pageContext.request.contextPath}/grossistes/nouveau"
           class="btn-primary">+ Ajouter un grossiste</a>
    </div>

    <div class="table-container">
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Téléphone</th>
                    <th>Email</th>
                    <th>Adresse</th>
                    <th>Contact</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="g" items="${grossistes}">
                    <tr>
                        <td>${g.id}</td>
                        <td>${g.nom}</td>
                        <td>${g.telephone}</td>
                        <td>${g.email != null ? g.email : '-'}</td>
                        <td>${g.adresse != null ? g.adresse : '-'}</td>
                        <td>${g.contactNom != null ? g.contactNom : '-'}</td>
                        <td>
                            <c:choose>
                                <c:when test="${g.actif}">
                                    <span class="badge badge-actif">Actif</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-inactif">Inactif</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="actions-cell">
                            <a href="${pageContext.request.contextPath}/grossistes/modifier?id=${g.id}"
                               class="btn-small btn-edit">Modifier</a>

                            <form method="post"
                                  action="${pageContext.request.contextPath}/grossistes/basculer"
                                  style="display:inline;"
                                  onsubmit="return confirm('Confirmer ?');">
                                <input type="hidden" name="id" value="${g.id}">
                                <button type="submit"
                                        class="btn-small ${g.actif ? 'btn-deactivate' : 'btn-activate'}">
                                    ${g.actif ? 'Désactiver' : 'Activer'}
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty grossistes}">
                    <tr>
                        <td colspan="8" class="empty-row">Aucun grossiste enregistré.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

</main>

<%@ include file="footer.jsp" %>
</body>
</html>