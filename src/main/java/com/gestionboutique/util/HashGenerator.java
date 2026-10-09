package com.gestionboutique.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Petit utilitaire pour générer un hash BCrypt.
 * À exécuter une fois pour créer un utilisateur admin en SQL.
 */
public class HashGenerator {

    public static void main(String[] args) {

        String motDePasse = "admin123";

        String hash = BCrypt.withDefaults()
                .hashToString(12, motDePasse.toCharArray());

        System.out.println("=================================================");
        System.out.println("Mot de passe : " + motDePasse);
        System.out.println("Hash BCrypt  : " + hash);
        System.out.println("=================================================");
    }
}