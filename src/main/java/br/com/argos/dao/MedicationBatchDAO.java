package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.MedicationBatch;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MedicationBatchDAO {

    // INSERT
    public void insert(MedicationBatch medicationBatch) throws SQLException {
        String sql = "INSERT INTO lote_medicamento (data_validade, status, local_armazenamento, data_fabricacao, " +
                "lote_fabricacao, data_entrega, qtd_disponivel, id_medicamento, id_fornecedor, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (medicationBatch.getExpirationDate() != null) {
                stmt.setDate(1, Date.valueOf(medicationBatch.getExpirationDate()));
            } else {
                stmt.setNull(1, Types.DATE);
            }

            stmt.setString(2, medicationBatch.getStatus());
            stmt.setString(3, medicationBatch.getStorageLocation());

            if (medicationBatch.getManufacturingDate() != null) {
                stmt.setDate(4, Date.valueOf(medicationBatch.getManufacturingDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setString(5, medicationBatch.getBatchNumber());

            if (medicationBatch.getDeliveryDate() != null) {
                stmt.setDate(6, Date.valueOf(medicationBatch.getDeliveryDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            stmt.setInt(7, medicationBatch.getAvailableQuantity());
            stmt.setObject(8, medicationBatch.getMedicationId());
            stmt.setObject(9, medicationBatch.getSupplierId());
            stmt.setBoolean(10, medicationBatch.isActive());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserting medication batch: " + e.getMessage());
            throw e;
        }
    }

    // FIND BY ID
    public MedicationBatch findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM lote_medicamento WHERE id_lote_medicamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapMedicationBatch(rs);
                }
            }
        }
        return null;
    }

    // FIND ALL
    public List<MedicationBatch> findAll() throws SQLException {
        String sql = "SELECT * FROM lote_medicamento ORDER BY data_validade";
        List<MedicationBatch> batches = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                batches.add(mapMedicationBatch(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error listing medication batches: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return batches;
    }

    // UPDATE
    public void update(MedicationBatch medicationBatch) throws SQLException {
        String sql = "UPDATE lote_medicamento SET data_validade = ?, status = ?, local_armazenamento = ?, " +
                "data_fabricacao = ?, lote_fabricacao = ?, data_entrega = ?, qtd_disponivel = ?, " +
                "id_medicamento = ?, id_fornecedor = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_lote_medicamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (medicationBatch.getExpirationDate() != null) {
                stmt.setDate(1, Date.valueOf(medicationBatch.getExpirationDate()));
            } else {
                stmt.setNull(1, Types.DATE);
            }

            stmt.setString(2, medicationBatch.getStatus());
            stmt.setString(3, medicationBatch.getStorageLocation());

            if (medicationBatch.getManufacturingDate() != null) {
                stmt.setDate(4, Date.valueOf(medicationBatch.getManufacturingDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setString(5, medicationBatch.getBatchNumber());

            if (medicationBatch.getDeliveryDate() != null) {
                stmt.setDate(6, Date.valueOf(medicationBatch.getDeliveryDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            stmt.setInt(7, medicationBatch.getAvailableQuantity());
            stmt.setObject(8, medicationBatch.getMedicationId());
            stmt.setObject(9, medicationBatch.getSupplierId());
            stmt.setBoolean(10, medicationBatch.isActive());
            stmt.setObject(11, medicationBatch.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating medication batch: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM lote_medicamento WHERE id_lote_medicamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting medication batch: " + e.getMessage());
            throw e;
        }
    }

    // MAP RESULT SET
    private MedicationBatch mapMedicationBatch(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_lote_medicamento", UUID.class);
        String status = rs.getString("status");
        String storageLocation = rs.getString("local_armazenamento");
        String batchNumber = rs.getString("lote_fabricacao");
        int availableQuantity = rs.getInt("qtd_disponivel");
        UUID medicationId = rs.getObject("id_medicamento", UUID.class);
        UUID supplierId = rs.getObject("id_fornecedor", UUID.class);
        boolean active = rs.getBoolean("ativo");

        LocalDate expirationDate = null;
        Date sqlExpirationDate = rs.getDate("data_validade");
        if (sqlExpirationDate != null) {
            expirationDate = sqlExpirationDate.toLocalDate();
        }

        LocalDate manufacturingDate = null;
        Date sqlManufacturingDate = rs.getDate("data_fabricacao");
        if (sqlManufacturingDate != null) {
            manufacturingDate = sqlManufacturingDate.toLocalDate();
        }

        LocalDate deliveryDate = null;
        Date sqlDeliveryDate = rs.getDate("data_entrega");
        if (sqlDeliveryDate != null) {
            deliveryDate = sqlDeliveryDate.toLocalDate();
        }

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("atualizado_em");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new MedicationBatch(id, expirationDate, status, storageLocation, manufacturingDate,
                batchNumber, deliveryDate, availableQuantity, updatedAt, medicationId, supplierId, active);
    }
}