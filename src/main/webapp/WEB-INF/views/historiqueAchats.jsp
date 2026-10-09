<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historique des achats - Gestion Boutique</title>
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<main class="page-content">

    <section class="page-title-section">
        <h1>Historique des achats</h1>
        <p>Consultez tous les achats enregistrés dans la boutique.</p>
    </section>

    <c:if test="${not empty messageSucces}">
        <div class="alert-success">${messageSucces}</div>
    </c:if>

    <c:if test="${not empty messageErreur}">
        <div class="alert-error">${messageErreur}</div>
    </c:if>

    <section class="search-card">

        <form action="${pageContext.request.contextPath}/achats/historique"
              method="get"
              class="filter-form">

            <div class="form-row">

                <div class="form-group">
                    <label for="dateDebut">Date de début</label>
                    <input type="date"
                           id="dateDebut"
                           name="dateDebut"
                           class="form-control"
                           value="${param.dateDebut}">
                </div>

                <div class="form-group">
                    <label for="dateFin">Date de fin</label>
                    <input type="date"
                           id="dateFin"
                           name="dateFin"
                           class="form-control"
                           value="${param.dateFin}">
                </div>

            </div>

            <div class="form-actions">
                <button type="submit" class="btn-primary">Filtrer</button>

                <a href="${pageContext.request.contextPath}/achats/historique"
                   class="btn-secondary">Réinitialiser</a>

                <a href="${pageContext.request.contextPath}/achats/nouveau"
                   class="btn-primary">+ Nouvel achat</a>
            </div>

        </form>

    </section>


    <section class="table-card">

        <div class="table-card-header">
            <h2>Achats enregistrés</h2>
            <span class="table-count">
                <c:out value="${achats.size()}" default="0"/> achat(s)
            </span>
        </div>

        <div class="table-container">

            <table class="data-table">

                <thead>
                    <tr>
                        <th>N°</th>
                        <th>Date</th>
                        <th>Client</th>
                        <th>Vendeur</th>
                        <th>Total</th>
                        <th>Statut</th>
                        <th>Actions</th>
                    </tr>
                </thead>

                <tbody>

                    <c:choose>

                        <c:when test="${not empty achats}">

                            <c:forEach var="a" items="${achats}">

                                <tr>
                                    <td><strong>#${a.id}</strong></td>

                                    <td>
                                        <c:out value="${a.dateAchat}" default="-"/>
                                    </td>

                                    <td>
                                        <c:out value="${a.clientNom}" default="-"/>
                                    </td>

                                    <td>
                                        <c:out value="${a.utilisateurNom}" default="-"/>
                                    </td>

                                    <td>
                                        <strong>${a.total} FCFA</strong>
                                    </td>

                                    <td>
                                        <span class="badge badge-actif">${a.statut}</span>
                                    </td>

                                                                        <td class="actions-cell">
                                        <a href="${pageContext.request.contextPath}/achats/detail?id=${a.id}"
                                           class="btn-small btn-edit">
                                            Voir
                                        </a>

                                        <a href="${pageContext.request.contextPath}/tickets/imprimer?id=${a.id}"
                                           class="btn-small"
                                           style="background:#1e3a5f;color:white;"
                                           target="_blank">
                                            🖨️
                                        </a>
                                    </td>                              
                                </tr>

                            </c:forEach>

                        </c:when>

                        <c:otherwise>

                            <tr>
                                <td colspan="7" class="empty-row">
                                    Aucun achat enregistré pour le moment.
                                </td>
                            </tr>

                        </c:otherwise>

                    </c:choose>

                </tbody>

            </table>

        </div>

    </section>

</main>

<%@ include file="footer.jsp" %>
</body>
</html>