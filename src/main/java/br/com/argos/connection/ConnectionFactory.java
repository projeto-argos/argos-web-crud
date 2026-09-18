package br.com.argos.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final Properties ENV = new Properties();

    static {
        try (InputStream input = ConnectionFactory.class
                .getClassLoader()
                .getResourceAsStream(".env")) {

            if (input == null) {
                throw new RuntimeException("The .env file was not found in resources.");
            }

            ENV.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load .env file.", e);
        }

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL driver not found.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = ENV.getProperty("DB_URL");
        String user = ENV.getProperty("DB_USER");
        String password = ENV.getProperty("DB_PASSWORD");

        if (url == null || user == null || password == null) {
            throw new RuntimeException("Missing DB_URL, DB_USER, or DB_PASSWORD in .env file.");
        }

        return DriverManager.getConnection(url, user, password);
    }
}