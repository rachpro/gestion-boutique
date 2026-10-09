<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajouter un utilisateur - Gestion Boutique</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<main class="page-container">

    <header class="page-header">
        <h1>Ajouter un utilisateur</h1>
        <p>Créez un nouveau compte pour un membre de votre équipe.</p>
    </header>

    <c:if test="${not empty messageSucces}">
        <div class="alert-success">${messageSucces}</div>
    </c:if>

    <c:if test="${not empty messageErreur}">
        <div class="alert-error">${messageErreur}</div>
    </c:if>

    <form method="post"
          action="${pageContext.request.contextPath}/utilisateurs/nouveau"
          class="form-card"
          id="ajoutUtilisateurForm">

        <div class="form-row">
            <div class="form-group">
                <label for="nom">Nom *</label>
                <input type="text" id="nom" name="nom" class="form-control"
                       placeholder="Ex : Dupont"
                       minlength="2" maxlength="80" required
                       value="${param.nom != null ? param.nom : ''}">
            </div>

            <div class="form-group">
                <label for="prenom">Prénom *</label>
                <input type="text" id="prenom" name="prenom" class="form-control"
                       placeholder="Ex : Jean"
                       minlength="2" maxlength="80" required
                       value="${param.prenom != null ? param.prenom : ''}">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="matricule">Matricule *</label>
                <input type="text" id="matricule" name="matricule" class="form-control"
                       placeholder="Ex : U001"
                       minlength="2" maxlength="50" required
                       value="${param.matricule != null ? param.matricule : ''}">
            </div>

            <div class="form-group">
                <label for="sexe">Sexe *</label>
                <select id="sexe" name="sexe" class="form-control" required>
                    <option value="">-- Sélectionner --</option>
                    <option value="M" ${param.sexe == 'M' ? 'selected' : ''}>Masculin</option>
                    <option value="F" ${param.sexe == 'F' ? 'selected' : ''}>Féminin</option>
                </select>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="dateNaissance">Date de naissance *</label>
                <input type="date" id="dateNaissance" name="dateNaissance"
                       class="form-control" required
                       value="${param.dateNaissance != null ? param.dateNaissance : ''}">
            </div>

            <div class="form-group">
                <label for="identifiant">Identifiant de connexion *</label>
                <input type="text" id="identifiant" name="identifiant" class="form-control"
                       placeholder="Ex : jdupont"
                       minlength="4" maxlength="80" required
                       autocomplete="username"
                       value="${param.identifiant != null ? param.identifiant : ''}">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="motDePasse">Mot de passe *</label>
                <input type="password" id="motDePasse" name="motDePasse"
                       class="form-control"
                       placeholder="Minimum 6 caractères"
                       minlength="6" required
                       autocomplete="new-password">
            </div>

            <div class="form-group">
                <label for="confirmationMotDePasse">Confirmer le mot de passe *</label>
                <input type="password" id="confirmationMotDePasse"
                       name="confirmationMotDePasse"
                       class="form-control"
                       placeholder="Répétez le mot de passe"
                       minlength="6" required
                       autocomplete="new-password">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="role">Rôle *</label>
                <select id="role" name="role" class="form-control" required>
                    <c:forEach var="r" items="${roles}">
                        <option value="${r}" ${param.role == r.toString() ? 'selected' : ''}>
                            ${r.libelle}
                        </option>
                    </c:forEach>
                </select>
                <small class="form-hint">
                    <strong>Administrateur</strong> : accès complet (gestion utilisateurs incluse).<br>
                    <strong>Vendeur</strong> : accès aux ventes, produits, achats et livraisons.
                </small>
            </div>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/utilisateurs/liste"
               class="btn-secondary">Annuler</a>
            <button type="submit" class="btn-primary">
                Enregistrer l'utilisateur
            </button>
        </div>

    </form>

</main>

<%@ include file="footer.jsp" %>

<script src="${pageContext.request.contextPath}/js/validation.js"></script>
<script>
    // Vérifie que les deux mots de passe sont identiques
    document.getElementById('ajoutUtilisateurForm')
        .addEventListener('submit', function (event) {
            const mdp = document.getElementById('motDePasse').value;
            const confirm = document.getElementById('confirmationMotDePasse').value;
            if (mdp !== confirm) {
                event.preventDefault();
                alert('Les deux mots de passe ne correspondent pas.');
            }
        });
</script>

</body>
</html>