package br.com.argos.connection;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory{

    // CRIA LUGAR PRO .ENV
    private static final Properties env = new Properties();

    // ENCONTRA E ARMAZENA O .ENV
    static {
        try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream(".env")) {
            if (input != null) {
                env.load(input);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading the .env file", e);
        }
    }

    // FAZ A CONEXÃO COM O BANCO DE DADOS
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                env.getProperty("DB_URL"),
                env.getProperty("DB_USER"),
                env.getProperty("DB_PASSWORD")
        );
    }
}