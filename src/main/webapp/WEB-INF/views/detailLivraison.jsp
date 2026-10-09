<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Détail de la livraison - Gestion Boutique</title>
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<main class="page-container">

    <header class="page-header">
        <h1>Livraison #${livraison.id}</h1>
        <p>Détail complet de la livraison reçue.</p>
    </header>

    <div class="form-card">

        <div class="form-row">
            <div class="form-group">
                <label>Grossiste</label>
                <input type="text" class="form-control" value="${livraison.grossisteNom}" readonly>
            </div>
            <div class="form-group">
                <label>Réceptionné par</label>
                <input type="text" class="form-control" value="${livraison.utilisateurNom}" readonly>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label>Date</label>
                <input type="text" class="form-control" value="${livraison.dateLivraison}" readonly>
            </div>
            <div class="form-group">
                <label>Référence</label>
                <input type="text" class="form-control"
                       value="${livraison.reference != null ? livraison.reference : '-'}" readonly>
            </div>
        </div>

        <h3 style="margin-top:30px;">Produits livrés</h3>

        <table class="data-table">
            <thead>
                <tr>
                    <th>Produit</th>
                    <th>Quantité</th>
                    <th>Prix d'achat</th>
                    <th>Sous-total</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="l" items="${livraison.lignes}">
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
                    <td><strong>${livraison.total} FCFA</strong></td>
                </tr>
            </tfoot>
        </table>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/livraisons/historique"
               class="btn-secondary">Retour à l'historique</a>
        </div>

    </div>

</main>

<%@ include file="footer.jsp" %>
</body>
</html>