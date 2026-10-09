<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Nouvelle livraison - Gestion Boutique</title>
</head>
<body>

<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>

<main class="page-container">

    <header class="page-header">
        <h1>Nouvelle livraison</h1>
        <p>Enregistrez une livraison reçue d'un grossiste.</p>
    </header>

    <c:if test="${not empty messageErreur}">
        <div class="alert-error">${messageErreur}</div>
    </c:if>

    <form method="post"
          action="${pageContext.request.contextPath}/livraisons/nouvelle"
          class="form-card"
          id="formLivraison">

        <div class="form-row">
            <div class="form-group">
                <label for="grossisteId">Grossiste *</label>
                <select id="grossisteId" name="grossisteId" class="form-control" required>
                    <option value="">-- Sélectionner un grossiste --</option>
                    <c:forEach var="g" items="${grossistes}">
                        <c:if test="${g.actif}">
                            <option value="${g.id}">${g.nom} (${g.telephone})</option>
                        </c:if>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="reference">Référence (bon de livraison)</label>
                <input type="text" id="reference" name="reference" class="form-control"
                       maxlength="50" placeholder="Ex : BL-2026-001">
            </div>
        </div>

        <h3 style="margin-top:30px;">Produits livrés</h3>

        <table class="data-table" id="tableLignes">
            <thead>
                <tr>
                    <th>Produit</th>
                    <th style="width:120px;">Quantité</th>
                    <th style="width:130px;">Prix d'achat</th>
                    <th style="width:130px;">Sous-total</th>
                    <th style="width:80px;"></th>
                </tr>
            </thead>
            <tbody id="lignesBody"></tbody>
        </table>

        <div style="margin-top:10px;">
            <button type="button" class="btn-secondary" onclick="ajouterLigne()">+ Ajouter une ligne</button>
        </div>

        <div style="margin-top:20px; text-align:right; font-size:1.2rem;">
            <strong>Total : <span id="totalAffiche">0.00</span> FCFA</strong>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/livraisons/historique"
               class="btn-secondary">Annuler</a>
            <button type="submit" class="btn-primary">Enregistrer la livraison</button>
        </div>

    </form>

</main>

<script>
    const produits = [
        <c:forEach var="p" items="${produits}" varStatus="st">
        {
            id: ${p.id},
            nom: "<c:out value='${p.nom}'/>",
            prix: ${p.prixAchat},
            stock: ${p.stock}
        }<c:if test="${!st.last}">,</c:if>
        </c:forEach>
    ];

    function ajouterLigne() {
        const tbody = document.getElementById('lignesBody');
        const tr = document.createElement('tr');

        const tdProduit = document.createElement('td');
        const select = document.createElement('select');
        select.name = 'produitId';
        select.className = 'form-control';
        select.required = true;
        select.innerHTML = '<option value="">-- Choisir --</option>';
        produits.forEach(p => {
            const opt = document.createElement('option');
            opt.value = p.id;
            opt.textContent = p.nom;
            opt.dataset.prix = p.prix;
            select.appendChild(opt);
        });
        tdProduit.appendChild(select);

        const tdQte = document.createElement('td');
        const inputQte = document.createElement('input');
        inputQte.type = 'number';
        inputQte.name = 'quantite';
        inputQte.className = 'form-control';
        inputQte.min = '1';
        inputQte.value = '1';
        inputQte.required = true;
        tdQte.appendChild(inputQte);

        const tdPrix = document.createElement('td');
        const spanPrix = document.createElement('span');
        spanPrix.textContent = '0.00';
        tdPrix.appendChild(spanPrix);

        const tdST = document.createElement('td');
        const spanST = document.createElement('span');
        spanST.textContent = '0.00';
        tdST.appendChild(spanST);

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

    window.addEventListener('DOMContentLoaded', ajouterLigne);
</script>

<%@ include file="footer.jsp" %>
</body>
</html>