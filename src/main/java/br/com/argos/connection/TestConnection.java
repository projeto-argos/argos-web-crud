package br.com.argos.connection;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {

        // TESTAR A CONEXÃO COM O BANCO DE DADOS
        try (Connection conn = ConnectionFactory.getConnection()) {
            System.out.println("✅ Database connection successful!");
        } catch (Exception e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}