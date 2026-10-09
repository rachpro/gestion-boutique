<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    request.setAttribute("titrePage", "Ticket de caisse");
%>

<%@ include file="header.jsp" %>

<main class="ticket-page">

    <section class="ticket-container">

        <div class="ticket-header">

            <h1>Gestion Boutique</h1>

            <p>
                Ticket de caisse
            </p>

            <p>
                Merci pour votre achat.
            </p>

        </div>


        <div class="ticket-information">

            <div>
                <strong>Ticket numéro :</strong>
                ${achat.id}
            </div>

            <div>
                <strong>Date :</strong>
                ${achat.date}
            </div>

            <div>
                <strong>Client :</strong>
                ${achat.client.nom}
                ${achat.client.prenom}
            </div>

        </div>


        <table class="ticket-table">

            <thead>

                <tr>
                    <th>Produit</th>
                    <th>Qté</th>
                    <th>Prix unitaire</th>
                    <th>Total</th>
                </tr>

            </thead>

            <tbody>

                <c:forEach
                    var="ligne"
                    items="${achat.lignes}">

                    <tr>

                        <td>
                            ${ligne.produit.nom}
                        </td>

                        <td>
                            ${ligne.quantite}
                        </td>

                        <td>
                            ${ligne.prixUnitaire}
                        </td>

                        <td>
                            ${ligne.total}
                        </td>

                    </tr>

                </c:forEach>

            </tbody>

            <tfoot>

                <tr>

                    <td
                        colspan="3"
                        class="ticket-total-label">
                        Total général
                    </td>

                    <td class="ticket-total-value">
                        ${achat.total}
                    </td>

                </tr>

            </tfoot>

        </table>


        <div class="ticket-footer">

            <p>
                Merci et à bientôt.
            </p>

            <p>
                Gestion Boutique
            </p>

        </div>


        <div class="ticket-actions no-print">

            <button
                type="button"
                class="btn-primary"
                onclick="window.print();">
                Imprimer le ticket
            </button>

            <a
                href="${pageContext.request.contextPath}/achats/historique"
                class="btn-secondary">
                Retour à l'historique
            </a>

        </div>

    </section>

</main>

<%@ include file="footer.jsp" %>
