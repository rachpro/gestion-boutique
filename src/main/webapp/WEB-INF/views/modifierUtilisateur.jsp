<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Modifier un utilisateur - Gestion Boutique</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<main class="page-container">

    <header class="page-header">
        <h1>Modifier un utilisateur</h1>
        <p>Modifiez les informations et le rôle de l'utilisateur.</p>
    </header>

    <c:if test="${not empty messageErreur}">
        <div class="alert-error">${messageErreur}</div>
    </c:if>

    <form method="post"
          action="${pageContext.request.contextPath}/utilisateurs/modifier"
          class="form-card">

        <input type="hidden" name="id" value="${utilisateur.id}">

        <div class="form-row">
            <div class="form-group">
                <label for="nom">Nom *</label>
                <input type="text" id="nom" name="nom"
                       class="form-control"
                       value="${utilisateur.nom}" required maxlength="80">
            </div>

            <div class="form-group">
                <label for="prenom">Prénom *</label>
                <input type="text" id="prenom" name="prenom"
                       class="form-control"
                       value="${utilisateur.prenom}" required maxlength="80">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="matricule">Matricule *</label>
                <input type="text" id="matricule" name="matricule"
                       class="form-control"
                       value="${utilisateur.matricule}" required maxlength="50">
            </div>

            <div class="form-group">
                <label for="sexe">Sexe *</label>
                <select id="sexe" name="sexe" class="form-control" required>
                    <option value="M" ${utilisateur.sexe == 'M' ? 'selected' : ''}>Masculin</option>
                    <option value="F" ${utilisateur.sexe == 'F' ? 'selected' : ''}>Féminin</option>
                </select>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="dateNaissance">Date de naissance *</label>
                <input type="date" id="dateNaissance" name="dateNaissance"
                       class="form-control"
                       value="${utilisateur.dateNaissance}" required>
            </div>

            <div class="form-group">
                <label for="identifiant">Identifiant *</label>
                <input type="text" id="identifiant" name="identifiant"
                       class="form-control"
                       value="${utilisateur.identifiant}" required minlength="4" maxlength="80">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="motDePasse">Nouveau mot de passe</label>
                <input type="password" id="motDePasse" name="motDePasse"
                       class="form-control"
                       placeholder="Laisser vide pour ne pas changer"
                       minlength="6">
                <small class="form-hint">Minimum 6 caractères. Laisser vide pour conserver le mot de passe actuel.</small>
            </div>

            <div class="form-group">
                <label for="role">Rôle *</label>
                <select id="role" name="role" class="form-control" required>
                    <c:forEach var="r" items="${roles}">
                        <option value="${r}" ${utilisateur.role == r ? 'selected' : ''}>
                            ${r.libelle}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label>
                <input type="checkbox" name="actif" value="true"
                       ${utilisateur.actif ? 'checked' : ''}>
                Compte actif
            </label>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/utilisateurs/liste"
               class="btn-secondary">Annuler</a>
            <button type="submit" class="btn-primary">Enregistrer les modifications</button>
        </div>

    </form>

</main>

<%@ include file="footer.jsp" %>
</body>
</html>