package br.com.argos.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final HikariDataSource HIKARI_DATA_SOURCE;

    static {
        Properties env = new Properties();

        // Carrega o arquivo .env a partir da pasta src/main/resources
        try (InputStream input = ConnectionFactory.class
                .getClassLoader()
                .getResourceAsStream(".env")) {

            if (input == null) {
                throw new IllegalStateException("Arquivo .env não encontrado no classpath (src/main/resources).");
            }
            env.load(input);

        } catch (Exception e) {
            throw new ExceptionInInitializerError("Erro ao carregar configurações do .env: " + e.getMessage());
        }

        HikariConfig config = new HikariConfig();

        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(env.getProperty("DB_URL"));
        config.setUsername(env.getProperty("DB_USER"));
        config.setPassword(env.getProperty("DB_PASSWORD"));

        // Tamanho do Pool
        config.setMinimumIdle(3);
        config.setMaximumPoolSize(10);

        // Timeouts
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(60000);

        config.addDataSourceProperty("tcpKeepAlive", "true");

        config.addDataSourceProperty("reWriteBatchedInserts", "true");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        HIKARI_DATA_SOURCE = new HikariDataSource(config);
    }

    private ConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException {
        return HIKARI_DATA_SOURCE.getConnection();
    }

    public static void closePool() {
        if (HIKARI_DATA_SOURCE != null && !HIKARI_DATA_SOURCE.isClosed()) {
            HIKARI_DATA_SOURCE.close();
        }
    }
}