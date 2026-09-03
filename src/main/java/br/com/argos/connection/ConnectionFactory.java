package br.com.argos.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final Properties env = new Properties();

    static {
        try (InputStream input =
                     ConnectionFactory.class
                             .getClassLoader()
                             .getResourceAsStream(".env")) {

            if (input == null) {
                throw new RuntimeException(
                        "Arquivo .env não encontrado no classpath."
                );
            }

            env.load(input);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Não foi possível carregar o arquivo .env",
                    e
            );
        }

        // Força o carregamento e auto-registro do driver do PostgreSQL.
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "Driver do PostgreSQL não encontrado no classpath.",
                    e
            );
        }
    }

    public static Connection getConnection() throws SQLException {

        String url = env.getProperty("DB_URL");
        String user = env.getProperty("DB_USER");
        String password = env.getProperty("DB_PASSWORD");

        if (url == null || user == null || password == null) {
            throw new RuntimeException(
                    "DB_URL, DB_USER ou DB_PASSWORD não encontrados no .env"
            );
        }

        return DriverManager.getConnection(
                url,
                user,
                password
        );
    }
}