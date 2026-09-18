package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.MedicationStock;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MedicationStockDAO {

    // INSERT
    public void insert(MedicationStock stock) throws SQLException {
        String sql = "INSERT INTO estoque_medicamento (quantidade_disponivel, local_armazenamento, ativo, " +
                "id_lote_medicamento, minimo_estoque, maximo_estoque, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, stock.getAvailableQuantity());
            stmt.setString(2, stock.getStorageLocation());
            stmt.setBoolean(3, stock.isActive());
            stmt.setObject(4, stock.getMedicationBatchId());
            stmt.setDouble(5, stock.getMinimumStock());
            stmt.setDouble(6, stock.getMaximumStock());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserting medication stock: " + e.getMessage());
            throw e;
        }
    }

    // FIND BY ID
    public MedicationStock findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM estoque_medicamento WHERE id_estoque_medicamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapMedicationStock(rs);
                }
            }
        }
        return null;
    }

    // FIND ALL
    public List<MedicationStock> findAll() throws SQLException {
        String sql = "SELECT * FROM estoque_medicamento ORDER BY local_armazenamento";
        List<MedicationStock> stocks = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                stocks.add(mapMedicationStock(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error listing medication stock: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return stocks;
    }

    // UPDATE
    public void update(MedicationStock stock) throws SQLException {
        String sql = "UPDATE estoque_medicamento SET quantidade_disponivel = ?, local_armazenamento = ?, " +
                "ativo = ?, id_lote_medicamento = ?, minimo_estoque = ?, maximo_estoque = ?, " +
                "atualizado_em = now() WHERE id_estoque_medicamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, stock.getAvailableQuantity());
            stmt.setString(2, stock.getStorageLocation());
            stmt.setBoolean(3, stock.isActive());
            stmt.setObject(4, stock.getMedicationBatchId());
            stmt.setDouble(5, stock.getMinimumStock());
            stmt.setDouble(6, stock.getMaximumStock());
            stmt.setObject(7, stock.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating medication stock: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM estoque_medicamento WHERE id_estoque_medicamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting medication stock: " + e.getMessage());
            throw e;
        }
    }

    // MAP RESULT SET
    private MedicationStock mapMedicationStock(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_estoque_medicamento", UUID.class);
        double availableQuantity = rs.getDouble("quantidade_disponivel");
        String storageLocation = rs.getString("local_armazenamento");
        boolean active = rs.getBoolean("ativo");
        UUID medicationBatchId = rs.getObject("id_lote_medicamento", UUID.class);
        double minimumStock = rs.getDouble("minimo_estoque");
        double maximumStock = rs.getDouble("maximo_estoque");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("atualizado_em");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new MedicationStock(id, availableQuantity, storageLocation, active,
                medicationBatchId, minimumStock, maximumStock, updatedAt);
    }
}