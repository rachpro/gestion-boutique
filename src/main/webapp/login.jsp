<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Connexion - Gestion Boutique</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/style.css">
</head>

<body>

    <main class="login-page">

        <section class="login-card">

            <header class="login-header">

                <div class="login-logo">
                    GB
                </div>

                <h1>Gestion Boutique</h1>

                <p>
                    Connectez-vous à votre espace de gestion
                </p>

            </header>


            <%--
                Gestion des messages d'erreur :
                - en priorité, l'attribut "messageErreur" (envoyé par LoginServlet via forward)
                - en secours, le paramètre "erreur" (query string)
            --%>

            <%
                String messageErreur = (String) request.getAttribute("messageErreur");
                String erreurParam   = request.getParameter("erreur");

                if (messageErreur != null && !messageErreur.isBlank()) {
            %>

                <div class="alert-error">
                    <%= messageErreur %>
                </div>

            <%
                } else if ("1".equals(erreurParam) || "connexion".equals(erreurParam)) {
            %>

                <div class="alert-error">
                    Vous devez être connecté pour accéder à cette page.
                </div>

            <%
                }
            %>


            <%--
                Message de succès après déconnexion.
            --%>

            <%
                String message = request.getParameter("message");

                if ("deconnexion".equals(message)) {
            %>

                <div class="alert-success">
                    Vous avez été déconnecté avec succès.
                </div>

            <%
                }
            %>


            <form action="${pageContext.request.contextPath}/login"
                  method="post">

                <div class="form-group">

                    <label for="identifiant">
                        Identifiant
                    </label>

                    <input
                        type="text"
                        id="identifiant"
                        name="identifiant"
                        class="form-control"
                        placeholder="Entrez votre identifiant"
                        required
                        autocomplete="username"
                        value="${identifiant != null ? identifiant : ''}">

                </div>


                <div class="form-group">

                    <label for="motDePasse">
                        Mot de passe
                    </label>

                    <input
                        type="password"
                        id="motDePasse"
                        name="motDePasse"
                        class="form-control"
                        placeholder="Entrez votre mot de passe"
                        required
                        autocomplete="current-password">

                </div>


                <button type="submit"
                        class="btn-primary">
                    Se connecter
                </button>

            </form>


            <footer class="login-footer">
                Application de gestion de boutique
            </footer>

        </section>

    </main>

    <script src="${pageContext.request.contextPath}/js/validation.js"></script>

</body>

</html>