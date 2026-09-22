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

    // CREATE
    public void insert(Batch batch) throws SQLException {
        String sql = "INSERT INTO batches (herd_id, category, opening_date, active, updated_at) " +
                "VALUES (?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, batch.getHerdId());
            stmt.setString(2, batch.getCategory());

            if (batch.getOpeningDate() != null) {
                stmt.setDate(3, Date.valueOf(batch.getOpeningDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            stmt.setBoolean(4, batch.isActive());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserting batch: " + e.getMessage());
            throw e;
        }
    }

    // READ
    public Batch findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM batches WHERE id_batch = ?";

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

    public List<Batch> findAll() throws SQLException {
        String sql = "SELECT * FROM batches ORDER BY category ASC";
        List<Batch> batches = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                batches.add(mapBatch(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing batches: " + e.getMessage());
            throw e;
        }

        return batches;
    }

    // UPDATE
    public void update(Batch batch) throws SQLException {
        String sql = "UPDATE batches SET herd_id = ?, category = ?, opening_date = ?, active = ?, updated_at = now() " +
                "WHERE id_batch = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, batch.getHerdId());
            stmt.setString(2, batch.getCategory());

            if (batch.getOpeningDate() != null) {
                stmt.setDate(3, Date.valueOf(batch.getOpeningDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            stmt.setBoolean(4, batch.isActive());
            stmt.setObject(5, batch.getIdBatch());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating batch: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM batches WHERE id_batch = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting batch: " + e.getMessage());
            throw e;
        }
    }

    // MAPPER
    private Batch mapBatch(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_batch", UUID.class);
        UUID herdId = rs.getObject("herd_id", UUID.class);
        String category = rs.getString("category");

        LocalDate openingDate = null;
        Date sqlDate = rs.getDate("opening_date");
        if (sqlDate != null) {
            openingDate = sqlDate.toLocalDate();
        }

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        boolean active = rs.getBoolean("active");

        return new Batch(id, herdId, category, openingDate, updatedAt, active);
    }
}