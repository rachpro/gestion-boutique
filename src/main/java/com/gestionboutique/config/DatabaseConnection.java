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
 * <p>La configuration est chargée depuis le fichier
 * {@code db.properties} situé dans le classpath.</p>
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
     * Construit la configuration HikariCP à partir des propriétés.
     */
    private static HikariConfig buildHikariConfig(Properties properties) {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(properties.getProperty("db.url"));
        config.setUsername(properties.getProperty("db.user"));
        config.setPassword(properties.getProperty("db.password"));
        config.setDriverClassName(properties.getProperty("db.driver"));

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