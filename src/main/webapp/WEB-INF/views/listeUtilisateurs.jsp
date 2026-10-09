<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Liste des utilisateurs - Gestion Boutique</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<main class="page-container">

    <header class="page-header">
        <h1>Liste des utilisateurs</h1>
        <p>Gérez les membres de votre équipe et leurs rôles.</p>
    </header>

    <c:if test="${not empty messageSucces}">
        <div class="alert-success">${messageSucces}</div>
    </c:if>

    <c:if test="${not empty messageErreur}">
        <div class="alert-error">${messageErreur}</div>
    </c:if>

    <div class="actions-bar">
        <a href="${pageContext.request.contextPath}/utilisateurs/nouveau"
           class="btn-primary">
            + Ajouter un utilisateur
        </a>
    </div>

    <div class="table-container">
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Matricule</th>
                    <th>Sexe</th>
                    <th>Identifiant</th>
                    <th>Rôle</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${utilisateurs}">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.nom}</td>
                        <td>${u.prenom}</td>
                        <td>${u.matricule}</td>
                        <td>${u.sexe}</td>
                        <td>${u.identifiant}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.role == 'ADMIN'}">
                                    <span class="badge badge-admin">Admin</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-vendeur">Vendeur</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${u.actif}">
                                    <span class="badge badge-actif">Actif</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-inactif">Inactif</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="actions-cell">
                            <a href="${pageContext.request.contextPath}/utilisateurs/modifier?id=${u.id}"
                               class="btn-small btn-edit">Modifier</a>

                            <form method="post"
                                  action="${pageContext.request.contextPath}/utilisateurs/basculer"
                                  style="display:inline;"
                                  onsubmit="return confirm('Confirmer le changement de statut ?');">
                                <input type="hidden" name="id" value="${u.id}">
                                <button type="submit"
                                        class="btn-small ${u.actif ? 'btn-deactivate' : 'btn-activate'}">
                                    ${u.actif ? 'Désactiver' : 'Activer'}
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty utilisateurs}">
                    <tr>
                        <td colspan="9" class="empty-row">
                            Aucun utilisateur enregistré.
                        </td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

</main>

<%@ include file="footer.jsp" %>
</body>
</html>