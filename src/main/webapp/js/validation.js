/*
 * =========================================================
 * VALIDATION DES FORMULAIRES
 * Application Gestion Boutique
 * =========================================================
 */


/*
 * Cette fonction est exécutée lorsque le navigateur
 * a complètement chargé la page HTML.
 */
document.addEventListener("DOMContentLoaded", function () {

    /*
     * Recherche le premier formulaire présent dans la page.
     *
     * Dans login.jsp, il s'agit du formulaire de connexion.
     */
    const formulaire = document.querySelector("form");


    /*
     * Si aucun formulaire n'est trouvé, on arrête le script.
     *
     * Cette vérification évite une erreur JavaScript
     * si le fichier est utilisé sur une page qui ne contient
     * pas encore de formulaire.
     */
    if (!formulaire) {
        return;
    }


    /*
     * Recherche le champ contenant l'identifiant.
     *
     * Nous utilisons l'attribut name="identifiant"
     * présent dans login.jsp.
     */
    const champIdentifiant =
        formulaire.querySelector('[name="identifiant"]');


    /*
     * Recherche le champ contenant le mot de passe.
     */
    const champMotDePasse =
        formulaire.querySelector('[name="motDePasse"]');


    /*
     * Si l'un des deux champs est absent,
     * le script ne peut pas fonctionner correctement.
     */
    if (!champIdentifiant || !champMotDePasse) {
        return;
    }


    /*
     * Cette fonction est exécutée lorsque l'utilisateur
     * essaie d'envoyer le formulaire.
     */
    formulaire.addEventListener("submit", function (event) {

        /*
         * On supprime les espaces situés au début et à la fin
         * de l'identifiant.
         *
         * Nous ne faisons pas cela pour le mot de passe,
         * car les espaces peuvent éventuellement faire partie
         * d'un mot de passe.
         */
        champIdentifiant.value =
            champIdentifiant.value.trim();


        /*
         * On réinitialise les anciens messages personnalisés.
         */
        champIdentifiant.setCustomValidity("");
        champMotDePasse.setCustomValidity("");


        /*
         * Vérification de l'identifiant vide.
         */
        if (champIdentifiant.value.length === 0) {

            champIdentifiant.setCustomValidity(
                "Veuillez saisir votre identifiant."
            );
        }

        /*
         * Vérification de la longueur minimale
         * de l'identifiant.
         */
        else if (champIdentifiant.value.length < 3) {

            champIdentifiant.setCustomValidity(
                "L'identifiant doit contenir au moins 3 caractères."
            );
        }


        /*
         * Vérification du mot de passe vide.
         */
        if (champMotDePasse.value.length === 0) {

            champMotDePasse.setCustomValidity(
                "Veuillez saisir votre mot de passe."
            );
        }

        /*
         * Vérification de la longueur minimale
         * du mot de passe.
         */
        else if (champMotDePasse.value.length < 6) {

            champMotDePasse.setCustomValidity(
                "Le mot de passe doit contenir au moins 6 caractères."
            );
        }


        /*
         * checkValidity() vérifie si les champs respectent
         * les règles définies dans le formulaire.
         */
        if (!formulaire.checkValidity()) {

            /*
             * Empêche l'envoi du formulaire vers le backend
             * si une donnée est incorrecte.
             */
            event.preventDefault();

            /*
             * Affiche au navigateur le premier message d'erreur.
             */
            formulaire.reportValidity();
        }

        /*
         * Si toutes les vérifications sont correctes,
         * aucun event.preventDefault() n'est exécuté.
         *
         * Le formulaire peut donc être envoyé normalement
         * vers la Servlet /login.
         */
    });


    /*
     * Lorsque l'utilisateur recommence à saisir un identifiant,
     * on supprime l'ancien message d'erreur personnalisé.
     */
    champIdentifiant.addEventListener("input", function () {
        champIdentifiant.setCustomValidity("");
    });


    /*
     * Lorsque l'utilisateur recommence à saisir un mot de passe,
     * on supprime l'ancien message d'erreur personnalisé.
     */
    champMotDePasse.addEventListener("input", function () {
        champMotDePasse.setCustomValidity("");
    });

});
