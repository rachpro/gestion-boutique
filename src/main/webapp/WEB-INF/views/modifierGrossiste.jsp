<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Modifier un grossiste - Gestion Boutique</title>
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<main class="page-container">

    <header class="page-header">
        <h1>Modifier un grossiste</h1>
        <p>Mettez à jour les informations du grossiste.</p>
    </header>

    <c:if test="${not empty messageErreur}">
        <div class="alert-error">${messageErreur}</div>
    </c:if>

    <form method="post"
          action="${pageContext.request.contextPath}/grossistes/modifier"
          class="form-card">

        <input type="hidden" name="id" value="${grossiste.id}">

        <div class="form-row">
            <div class="form-group">
                <label for="nom">Nom / Raison sociale *</label>
                <input type="text" id="nom" name="nom" class="form-control"
                       value="${grossiste.nom}" required maxlength="150">
            </div>
            <div class="form-group">
                <label for="telephone">Téléphone *</label>
                <input type="text" id="telephone" name="telephone" class="form-control"
                       value="${grossiste.telephone}" required maxlength="20">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" class="form-control"
                       value="${grossiste.email}" maxlength="120">
            </div>
            <div class="form-group">
                <label for="contactNom">Nom du contact</label>
                <input type="text" id="contactNom" name="contactNom" class="form-control"
                       value="${grossiste.contactNom}" maxlength="80">
            </div>
        </div>

        <div class="form-group">
            <label for="adresse">Adresse</label>
            <input type="text" id="adresse" name="adresse" class="form-control"
                   value="${grossiste.adresse}" maxlength="255">
        </div>

        <div class="form-group">
            <label>
                <input type="checkbox" name="actif" value="true"
                       ${grossiste.actif ? 'checked' : ''}>
                Grossiste actif
            </label>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/grossistes/liste"
               class="btn-secondary">Annuler</a>
            <button type="submit" class="btn-primary">Enregistrer les modifications</button>
        </div>

    </form>

</main>

<%@ include file="footer.jsp" %>
</body>
</html>