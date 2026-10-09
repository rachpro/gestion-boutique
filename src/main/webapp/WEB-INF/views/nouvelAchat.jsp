<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Nouvel achat - Gestion Boutique</title>
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<main class="page-container">

    <header class="page-header">
        <h1>Nouvel achat</h1>
        <p>Enregistrez un achat client avec plusieurs produits.</p>
    </header>

    <c:if test="${not empty messageErreur}">
        <div class="alert-error">${messageErreur}</div>
    </c:if>

    <form method="post"
          action="${pageContext.request.contextPath}/achats/nouveau"
          class="form-card"
          id="formAchat">

        <div class="form-group">
            <label for="clientId">Client *</label>
            <select id="clientId" name="clientId" class="form-control" required>
                <option value="">-- Sélectionner un client --</option>
                <c:forEach var="cl" items="${clients}">
                    <c:if test="${cl.actif}">
                        <option value="${cl.id}">${cl.prenom} ${cl.nom} (${cl.telephone})</option>
                    </c:if>
                </c:forEach>
            </select>
        </div>

        <h3 style="margin-top:30px;">Produits achetés</h3>

        <table class="data-table" id="tableLignes">
            <thead>
                <tr>
                    <th>Produit</th>
                    <th style="width:120px;">Quantité</th>
                    <th style="width:130px;">Prix unitaire</th>
                    <th style="width:130px;">Sous-total</th>
                    <th style="width:80px;"></th>
                </tr>
            </thead>
            <tbody id="lignesBody">
                <!-- lignes ajoutées par JS -->
            </tbody>
        </table>

        <div style="margin-top:10px;">
            <button type="button" class="btn-secondary" onclick="ajouterLigne()">+ Ajouter une ligne</button>
        </div>

        <div style="margin-top:20px; text-align:right; font-size:1.2rem;">
            <strong>Total : <span id="totalAffiche">0.00</span> FCFA</strong>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/achats/historique"
               class="btn-secondary">Annuler</a>
            <button type="submit" class="btn-primary">Enregistrer l'achat</button>
        </div>

    </form>

</main>

<script>
    // Données produits disponibles (JSON généré côté serveur)
    const produits = [
        <c:forEach var="p" items="${produits}" varStatus="st">
        {
            id: ${p.id},
            nom: "<c:out value='${p.nom}'/>",
            prix: ${p.prixVente},
            stock: ${p.stock}
        }<c:if test="${!st.last}">,</c:if>
        </c:forEach>
    ];

    function ajouterLigne() {
        const tbody = document.getElementById('lignesBody');
        const tr = document.createElement('tr');

        // Cellule produit
        const tdProduit = document.createElement('td');
        const select = document.createElement('select');
        select.name = 'produitId';
        select.className = 'form-control';
        select.required = true;
        select.innerHTML = '<option value="">-- Choisir --</option>';

        produits.forEach(p => {
            const opt = document.createElement('option');
            opt.value = p.id;
            opt.textContent = p.nom + ' (stock: ' + p.stock + ')';
            opt.dataset.prix = p.prix;
            select.appendChild(opt);
        });
        tdProduit.appendChild(select);

        // Cellule quantité
        const tdQte = document.createElement('td');
        const inputQte = document.createElement('input');
        inputQte.type = 'number';
        inputQte.name = 'quantite';
        inputQte.className = 'form-control';
        inputQte.min = '1';
        inputQte.value = '1';
        inputQte.required = true;
        tdQte.appendChild(inputQte);

        // Cellule prix unitaire (lecture seule, calculée)
        const tdPrix = document.createElement('td');
        const spanPrix = document.createElement('span');
        spanPrix.textContent = '0.00';
        tdPrix.appendChild(spanPrix);

        // Cellule sous-total
        const tdST = document.createElement('td');
        const spanST = document.createElement('span');
        spanST.textContent = '0.00';
        tdST.appendChild(spanST);

        // Cellule supprimer
        const tdBtn = document.createElement('td');
        const btn = document.createElement('button');
        btn.type = 'button';
        btn.textContent = '×';
        btn.className = 'btn-small btn-deactivate';
        btn.onclick = function () { tr.remove(); recalculer(); };
        tdBtn.appendChild(btn);

        tr.appendChild(tdProduit);
        tr.appendChild(tdQte);
        tr.appendChild(tdPrix);
        tr.appendChild(tdST);
        tr.appendChild(tdBtn);

        tbody.appendChild(tr);

        // Écouteurs
        select.addEventListener('change', function () {
            const opt = select.options[select.selectedIndex];
            spanPrix.textContent = (opt.dataset.prix ? parseFloat(opt.dataset.prix).toFixed(2) : '0.00');
            recalculer();
        });
        inputQte.addEventListener('input', recalculer);

        recalculer();
    }

    function recalculer() {
        let total = 0;
        document.querySelectorAll('#lignesBody tr').forEach(tr => {
            const select = tr.querySelector('select[name=produitId]');
            const qteInput = tr.querySelector('input[name=quantite]');
            const opt = select.options[select.selectedIndex];

            const prix = parseFloat(opt?.dataset?.prix || 0);
            const qte = parseInt(qteInput.value || 0);

            const st = prix * qte;
            tr.children[2].textContent = prix.toFixed(2);
            tr.children[3].textContent = st.toFixed(2);

            total += st;
        });
        document.getElementById('totalAffiche').textContent = total.toFixed(2);
    }

    // Ajoute une première ligne automatiquement
    window.addEventListener('DOMContentLoaded', ajouterLigne);
</script>

<%@ include file="footer.jsp" %>
</body>
</html>