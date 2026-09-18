package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Batch;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BatchDAO {

    // INSERT
    public void insert(Batch batch) throws SQLException {
        String sql = "INSERT INTO lote (categoria, id_rebanho, ativo, quantidade_original_cabecas, codigo_lote, data_abertura, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, batch.getCategory());
            stmt.setObject(2, batch.getHerdId());
            stmt.setBoolean(3, batch.isActive());
            stmt.setInt(4, batch.getOriginalHeadCount());
            stmt.setString(5, batch.getCode());

            if (batch.getOpenDate() != null) {
                stmt.setDate(6, java.sql.Date.valueOf(batch.getOpenDate()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserting batch: " + e.getMessage());
            throw e;
        }
    }

    // FIND BY ID
    public Batch findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM lote WHERE id_lote = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapBatch(rs);
                }
            }
        }
        return null;
    }

    // FIND ALL
    public List<Batch> findAll() throws SQLException {
        String sql = "SELECT * FROM lote ORDER BY categoria ASC";
        List<Batch> batches = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                batches.add(mapBatch(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing batches: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return batches;
    }

    // UPDATE
    public void update(Batch batch) throws SQLException {
        String sql = "UPDATE lote SET categoria = ?, id_rebanho = ?, ativo = ?, quantidade_original_cabecas = ?, codigo_lote = ?, data_abertura = ?, atualizado_em = now() " +
                "WHERE id_lote = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, batch.getCategory());
            stmt.setObject(2, batch.getHerdId());
            stmt.setBoolean(3, batch.isActive());
            stmt.setInt(4, batch.getOriginalHeadCount());
            stmt.setString(5, batch.getCode());

            if (batch.getOpenDate() != null) {
                stmt.setDate(6, java.sql.Date.valueOf(batch.getOpenDate()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }

            stmt.setObject(7, batch.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating batch: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM lote WHERE id_lote = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting batch: " + e.getMessage());
            throw e;
        }
    }

    // MAP RESULT SET
    private Batch mapBatch(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_lote", UUID.class);
        String category = rs.getString("categoria");
        UUID herdId = rs.getObject("id_rebanho", UUID.class);
        boolean active = rs.getBoolean("ativo");
        int originalHeadCount = rs.getInt("quantidade_original_cabecas");
        String code = rs.getString("codigo_lote");

        LocalDate openDate = null;
        java.sql.Date sqlDate = rs.getDate("data_abertura");
        if (sqlDate != null) {
            openDate = sqlDate.toLocalDate();
        }

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("atualizado_em");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Batch(id, category, updatedAt, herdId, active, originalHeadCount, code, openDate);
    }
}