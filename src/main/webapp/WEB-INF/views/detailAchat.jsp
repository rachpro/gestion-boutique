<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Détail de l'achat - Gestion Boutique</title>
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<main class="page-container">

    <header class="page-header">
        <h1>Achat #${achat.id}</h1>
        <p>Détail complet de la transaction.</p>
    </header>

    <div class="form-card">

        <div class="form-row">
            <div class="form-group">
                <label>Client</label>
                <input type="text" class="form-control" value="${achat.clientNom}" readonly>
            </div>
            <div class="form-group">
                <label>Vendeur</label>
                <input type="text" class="form-control" value="${achat.utilisateurNom}" readonly>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label>Date</label>
                <input type="text" class="form-control" value="${achat.dateAchat}" readonly>
            </div>
            <div class="form-group">
                <label>Statut</label>
                <input type="text" class="form-control" value="${achat.statut}" readonly>
            </div>
        </div>

        <h3 style="margin-top:30px;">Produits</h3>

        <table class="data-table">
            <thead>
                <tr>
                    <th>Produit</th>
                    <th>Quantité</th>
                    <th>Prix unitaire</th>
                    <th>Sous-total</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="l" items="${achat.lignes}">
                    <tr>
                        <td>${l.produitNom}</td>
                        <td>${l.quantite}</td>
                        <td>${l.prixUnitaire} FCFA</td>
                        <td><strong>${l.sousTotal} FCFA</strong></td>
                    </tr>
                </c:forEach>
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="3" style="text-align:right;"><strong>TOTAL :</strong></td>
                    <td><strong>${achat.total} FCFA</strong></td>
                </tr>
            </tfoot>
        </table>

                <div class="form-actions">
            <a href="${pageContext.request.contextPath}/achats/historique"
               class="btn-secondary">Retour à l'historique</a>

            <a href="${pageContext.request.contextPath}/tickets/imprimer?id=${achat.id}"
               class="btn-primary"
               target="_blank">
                🖨️ Imprimer le ticket
            </a>
        </div>
    </div>

</main>

<%@ include file="footer.jsp" %>
</body>
</html>