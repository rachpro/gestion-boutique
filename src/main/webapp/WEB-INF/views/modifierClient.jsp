<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Modifier un client - Gestion Boutique</title>
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<main class="page-container">

    <header class="page-header">
        <h1>Modifier un client</h1>
        <p>Mettez à jour les informations du client.</p>
    </header>

    <c:if test="${not empty messageErreur}">
        <div class="alert-error">${messageErreur}</div>
    </c:if>

    <form method="post"
          action="${pageContext.request.contextPath}/clients/modifier"
          class="form-card">

        <input type="hidden" name="id" value="${client.id}">

        <div class="form-row">
            <div class="form-group">
                <label for="nom">Nom *</label>
                <input type="text" id="nom" name="nom" class="form-control"
                       value="${client.nom}" required maxlength="80">
            </div>
            <div class="form-group">
                <label for="prenom">Prénom *</label>
                <input type="text" id="prenom" name="prenom" class="form-control"
                       value="${client.prenom}" required maxlength="80">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="telephone">Téléphone *</label>
                <input type="text" id="telephone" name="telephone" class="form-control"
                       value="${client.telephone}" required maxlength="20">
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" class="form-control"
                       value="${client.email}" maxlength="120">
            </div>
        </div>

        <div class="form-group">
            <label for="adresse">Adresse</label>
            <input type="text" id="adresse" name="adresse" class="form-control"
                   value="${client.adresse}" maxlength="255">
        </div>

        <div class="form-group">
            <label>
                <input type="checkbox" name="actif" value="true"
                       ${client.actif ? 'checked' : ''}>
                Client actif
            </label>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/clients/liste"
               class="btn-secondary">Annuler</a>
            <button type="submit" class="btn-primary">Enregistrer les modifications</button>
        </div>

    </form>

</main>

<%@ include file="footer.jsp" %>
</body>
</html>