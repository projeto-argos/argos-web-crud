package br.com.argos.connection;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final Properties env = new Properties();

    static {
        try (FileReader reader = new FileReader("src/main/resources/.env")) {
            env.load(reader);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar o arquivo .env", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = env.getProperty("DB_URL");
        String user = env.getProperty("DB_USER");
        String password = env.getProperty("DB_PASSWORD");

        return DriverManager.getConnection(url, user, password);
    }
}