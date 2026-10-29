package br.com.argos.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try {
            for (int i = 1; i <= 3; i++) {
                long t0 = System.nanoTime();

                try (Connection c = ConnectionFactory.getConnection()) {
                    long t1 = System.nanoTime();

                    try (PreparedStatement ps = c.prepareStatement("SELECT 1");
                         ResultSet rs = ps.executeQuery()) {
                        rs.next();
                    }
                    long t2 = System.nanoTime();

                    System.out.printf("Tentativa %d | pegar conexão: %d ms | SELECT 1: %d ms%n",
                            i, (t1 - t0) / 1_000_000, (t2 - t1) / 1_000_000);
                }
            }
        } catch (SQLException sqle){
            System.err.println("Error trying to connect: " +  sqle.getMessage());
            sqle.printStackTrace();
        } finally {
            ConnectionFactory.closePool();
        }
    }
}