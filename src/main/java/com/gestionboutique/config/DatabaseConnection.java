package com.gestionboutique.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitaire centralisant la connexion à la base de données.
 *
 * <p>Utilise HikariCP pour gérer un pool de connexions performant
 * et réutilisable par l'ensemble des DAO de l'application.</p>
 *
 * <p><b>Deux modes de configuration :</b></p>
 * <ul>
 *     <li><b>Mode cloud</b> (Railway, Render, Heroku...) : lit les variables
 *         d'environnement {@code PGHOST}, {@code PGPORT}, {@code PGDATABASE},
 *         {@code PGUSER}, {@code PGPASSWORD}.</li>
 *     <li><b>Mode local</b> : lit le fichier {@code db.properties} du classpath.</li>
 * </ul>
 *
 * <p>Le mode cloud est prioritaire s'il est détecté.</p>
 */
public final class DatabaseConnection {

    private static final Logger LOGGER =
            Logger.getLogger(DatabaseConnection.class.getName());

    private static final String CONFIG_FILE = "db.properties";

    private static final HikariDataSource DATA_SOURCE;

    /*
     * Bloc d'initialisation statique :
     * exécuté une seule fois, au premier accès à la classe.
     */
    static {
        try {
            Properties properties = loadProperties();
            HikariConfig hikariConfig = buildHikariConfig(properties);
            DATA_SOURCE = new HikariDataSource(hikariConfig);

            LOGGER.info("Pool de connexions HikariCP initialisé avec succès.");

        } catch (IOException exception) {
            LOGGER.log(
                    Level.SEVERE,
                    "Impossible de charger le fichier de configuration : "
                            + CONFIG_FILE,
                    exception
            );
            throw new ExceptionInInitializerError(exception);
        }
    }

    /*
     * Constructeur privé : classe utilitaire non instanciable.
     */
    private DatabaseConnection() {
        throw new UnsupportedOperationException(
                "Classe utilitaire : ne pas instancier."
        );
    }

    /**
     * Charge le fichier {@code db.properties} depuis le classpath.
     */
    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();

        try (InputStream inputStream = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (inputStream == null) {
                throw new IOException(
                        "Fichier introuvable dans le classpath : " + CONFIG_FILE
                );
            }

            properties.load(inputStream);
        }

        return properties;
    }

    /**
     * Construit la configuration HikariCP.
     *
     * <p>Priorité aux variables d'environnement (cloud) si {@code PGHOST}
     * est défini. Sinon, utilise le fichier {@code db.properties} (local).</p>
     */
    private static HikariConfig buildHikariConfig(Properties properties) {
        HikariConfig config = new HikariConfig();

        // =========================================================
        // Détection du mode cloud (Railway, Render, Heroku...)
        // =========================================================
        String envHost = System.getenv("PGHOST");
        String envPort = System.getenv("PGPORT");
        String envDb   = System.getenv("PGDATABASE");
        String envUser = System.getenv("PGUSER");
        String envPass = System.getenv("PGPASSWORD");

        if (envHost != null && !envHost.isBlank()) {
            // ---------- MODE CLOUD ----------
            String jdbcUrl = "jdbc:postgresql://" + envHost + ":" + envPort + "/" + envDb;

            config.setJdbcUrl(jdbcUrl);
            config.setUsername(envUser);
            config.setPassword(envPass);
            config.setDriverClassName("org.postgresql.Driver");

            LOGGER.info("HikariCP : configuration CLOUD (variables d'environnement).");
            LOGGER.info("HikariCP : URL = " + jdbcUrl);
            LOGGER.info("HikariCP : USER = " + envUser);

        } else {
            // ---------- MODE LOCAL ----------
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.user"));
            config.setPassword(properties.getProperty("db.password"));
            config.setDriverClassName(properties.getProperty("db.driver"));

            LOGGER.info("HikariCP : configuration LOCALE (db.properties).");
        }

        // =========================================================
        // Paramètres du pool (communs aux deux modes)
        // =========================================================
        config.setMaximumPoolSize(
                Integer.parseInt(properties.getProperty("db.pool.maximumPoolSize", "10"))
        );
        config.setMinimumIdle(
                Integer.parseInt(properties.getProperty("db.pool.minimumIdle", "2"))
        );
        config.setConnectionTimeout(
                Long.parseLong(properties.getProperty("db.pool.connectionTimeout", "30000"))
        );
        config.setIdleTimeout(
                Long.parseLong(properties.getProperty("db.pool.idleTimeout", "600000"))
        );
        config.setMaxLifetime(
                Long.parseLong(properties.getProperty("db.pool.maxLifetime", "1800000"))
        );
        config.setPoolName(
                properties.getProperty("db.pool.poolName", "GestionBoutiquePool")
        );

        return config;
    }

    /**
     * Récupère une connexion depuis le pool.
     *
     * <p><b>Important</b> : la connexion doit être fermée
     * (via try-with-resources) pour être restituée au pool.</p>
     */
    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    /**
     * Ferme le pool de connexions.
     * À appeler lors de l'arrêt de l'application (contexte Servlet).
     */
    public static void shutdown() {
        if (DATA_SOURCE != null && !DATA_SOURCE.isClosed()) {
            DATA_SOURCE.close();
            LOGGER.info("Pool de connexions HikariCP fermé.");
        }
    }
}