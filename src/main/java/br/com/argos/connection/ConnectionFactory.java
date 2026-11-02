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

        try (InputStream input = ConnectionFactory.class
                .getClassLoader()
                .getResourceAsStream(".env")) {

            if (input != null) {
                env.load(input);
            }

        } catch (Exception e) {
            throw new ExceptionInInitializerError("Erro ao carregar configurações do .env: " + e.getMessage());
        }

        String dbUrl = readSetting("DB_URL", env);
        String dbUser = readSetting("DB_USER", env);
        String dbPassword = readSetting("DB_PASSWORD", env);

        if (dbUrl == null || dbUser == null || dbPassword == null) {
            throw new IllegalStateException(
                    "Configure DB_URL, DB_USER e DB_PASSWORD (variáveis de ambiente ou arquivo .env).");
        }

        HikariConfig config = new HikariConfig();

        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(dbUrl);
        config.setUsername(dbUser);
        config.setPassword(dbPassword);

//       1 conexão fica aberta e o pool cresce até DB_POOL_MAX só quando precisa.
        config.setMaximumPoolSize(readPoolMax(env));
        config.setMinimumIdle(1);
        config.setIdleTimeout(60000);

        // Timeouts e Segurança
        config.setConnectionTimeout(30000);
        config.setMaxLifetime(1800000);
        config.setKeepaliveTime(120000);
        config.setLeakDetectionThreshold(60000);

        config.setPoolName("HikariTomcatPool");

        config.addDataSourceProperty("tcpKeepAlive", "true");
        config.addDataSourceProperty("reWriteBatchedInserts", "true");

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        HIKARI_DATA_SOURCE = new HikariDataSource(config);
    }

    private ConnectionFactory() {
    }

    /** Lê a configuração da variável de ambiente; se não existir, usa o valor do arquivo .env. */
    private static String readSetting(String key, Properties env) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = env.getProperty(key);
        }
        return (value == null || value.isBlank()) ? null : value;
    }

    /** Tamanho máximo do pool: variável DB_POOL_MAX (1 a 8); se faltar ou for inválida, 3. */
    private static int readPoolMax(Properties env) {
        String value = readSetting("DB_POOL_MAX", env);
        try {
            int max = value == null ? 3 : Integer.parseInt(value.trim());
            return Math.max(1, Math.min(max, 8));
        } catch (NumberFormatException e) {
            return 3;
        }
    }

    public static void init(){}

    public static Connection getConnection() throws SQLException {
        return HIKARI_DATA_SOURCE.getConnection();
    }

    public static void closePool() {
        if (HIKARI_DATA_SOURCE != null && !HIKARI_DATA_SOURCE.isClosed()) {
            HIKARI_DATA_SOURCE.close();
        }
    }
}