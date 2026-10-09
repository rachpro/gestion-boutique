<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historique des livraisons - Gestion Boutique</title>
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<main class="page-content">

    <section class="page-title-section">
        <h1>Historique des livraisons</h1>
        <p>Consultez les livraisons reçues des grossistes.</p>
    </section>

    <c:if test="${not empty messageSucces}">
        <div class="alert-success">${messageSucces}</div>
    </c:if>

    <c:if test="${not empty messageErreur}">
        <div class="alert-error">${messageErreur}</div>
    </c:if>


    <section class="search-card">

        <form action="${pageContext.request.contextPath}/livraisons/historique"
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

                <a href="${pageContext.request.contextPath}/livraisons/historique"
                   class="btn-secondary">Réinitialiser</a>

                <a href="${pageContext.request.contextPath}/livraisons/nouvelle"
                   class="btn-primary">+ Nouvelle livraison</a>
            </div>

        </form>

    </section>


    <section class="table-card">

        <div class="table-card-header">
            <h2>Livraisons enregistrées</h2>
            <span class="table-count">
                <c:out value="${livraisons.size()}" default="0"/> livraison(s)
            </span>
        </div>


        <div class="table-container">

            <table class="data-table">

                <thead>
                    <tr>
                        <th>N°</th>
                        <th>Date</th>
                        <th>Grossiste</th>
                        <th>Référence</th>
                        <th>Réceptionné par</th>
                        <th>Total</th>
                        <th>Actions</th>
                    </tr>
                </thead>

                <tbody>

                    <c:choose>

                        <c:when test="${not empty livraisons}">

                            <c:forEach var="l" items="${livraisons}">

                                <tr>
                                    <td><strong>#${l.id}</strong></td>

                                    <td>
                                        <c:out value="${l.dateLivraison}" default="-"/>
                                    </td>

                                    <td>
                                        <c:out value="${l.grossisteNom}" default="-"/>
                                    </td>

                                    <td>
                                        <c:out value="${l.reference}" default="-"/>
                                    </td>

                                    <td>
                                        <c:out value="${l.utilisateurNom}" default="-"/>
                                    </td>

                                    <td>
                                        <strong>${l.total} FCFA</strong>
                                    </td>

                                    <td class="actions-cell">
                                        <a href="${pageContext.request.contextPath}/livraisons/detail?id=${l.id}"
                                           class="btn-small btn-edit">
                                            Voir le détail
                                        </a>
                                    </td>
                                </tr>

                            </c:forEach>

                        </c:when>

                        <c:otherwise>

                            <tr>
                                <td colspan="7" class="empty-row">
                                    Aucune livraison trouvée.
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