<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    request.setAttribute("titrePage", "Ajouter un produit" );
%>

<%@ include file="header.jsp" %>

<%@ include file="menu.jsp" %>

<main class="page-content">

    <section class="page-title-section">

        <h1>Ajouter un produit</h1>

        <p>
            Enregistrez un nouveau produit dans la boutique.
        </p>

    </section>


    <%-- Message d'erreur transmis par ProduitServlet --%>
    <% if ("erreur".equals(request.getParameter("message"))) { %>

        <div class="alert-error">
            Une erreur est survenue lors de l'enregistrement du produit.
            Vérifiez les informations saisies.
        </div>

    <% } %>


    <section class="form-card">

        <form
            action="${pageContext.request.contextPath}/produits/ajouter"
            method="post"
            id="ajoutProduitForm">

            <div class="form-group">

                <label for="nom">
                    Nom du produit
                </label>

                <input
                    type="text"
                    id="nom"
                    name="nom"
                    class="form-control"
                    placeholder="Exemple : Riz 25 kg"
                    minlength="2"
                    maxlength="150"
                    required>

            </div>


            <div class="form-row">

                <div class="form-group">

                    <label for="prixAchat">
                        Prix d'achat
                    </label>

                    <input
                        type="number"
                        id="prixAchat"
                        name="prixAchat"
                        class="form-control"
                        placeholder="Exemple : 12000"
                        min="0"
                        step="0.01"
                        required>

                </div>


                <div class="form-group">

                    <label for="prixVente">
                        Prix de vente
                    </label>

                    <input
                        type="number"
                        id="prixVente"
                        name="prixVente"
                        class="form-control"
                        placeholder="Exemple : 15000"
                        min="0"
                        step="0.01"
                        required>

                </div>

            </div>


            <div class="form-group">

                <label for="stock">
                    Stock initial
                </label>

                <input
                    type="number"
                    id="stock"
                    name="stock"
                    class="form-control"
                    placeholder="Exemple : 20"
                    min="0"
                    step="1"
                    required>

            </div>


            <div class="form-actions">

                <button
                    type="submit"
                    class="btn-primary">
                    Enregistrer le produit
                </button>

                <a
                    href="${pageContext.request.contextPath}/produits/rechercher"
                    class="btn-secondary">
                    Annuler
                </a>

            </div>

        </form>

    </section>

</main>

<%@ include file="footer.jsp" %>
