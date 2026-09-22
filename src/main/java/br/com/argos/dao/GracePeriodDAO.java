package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.GracePeriod;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GracePeriodDAO {

    // INSERT
    public void insert(GracePeriod gracePeriod) throws SQLException {
        String sql = "INSERT INTO periodo_carencia (data_inicio_carencia, data_fim_carencia, observacoes, id_animal, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (gracePeriod.getStartDate() != null) {
                stmt.setDate(1, Date.valueOf(gracePeriod.getStartDate()));
            } else {
                stmt.setNull(1, Types.DATE);
            }

            if (gracePeriod.getEndDate() != null) {
                stmt.setDate(2, Date.valueOf(gracePeriod.getEndDate()));
            } else {
                stmt.setNull(2, Types.DATE);
            }

            stmt.setString(3, gracePeriod.getNotes());
            stmt.setObject(4, gracePeriod.getAnimalId());
            stmt.setBoolean(5, gracePeriod.isActive());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserting grace period: " + e.getMessage());
            throw e;
        }
    }

    // FIND BY ID
    public GracePeriod findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM periodo_carencia WHERE id_carencia = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapGracePeriod(rs);
                }
            }
        }
        return null;
    }

    // FIND ALL
    public List<GracePeriod> findAll() throws SQLException {
        String sql = "SELECT * FROM periodo_carencia ORDER BY data_fim_carencia DESC";
        List<GracePeriod> gracePeriods = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                gracePeriods.add(mapGracePeriod(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing grace periods: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return gracePeriods;
    }

    // UPDATE
    public void update(GracePeriod gracePeriod) throws SQLException {
        String sql = "UPDATE periodo_carencia SET data_inicio_carencia = ?, data_fim_carencia = ?, " +
                "observacoes = ?, id_animal = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_carencia = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (gracePeriod.getStartDate() != null) {
                stmt.setDate(1, Date.valueOf(gracePeriod.getStartDate()));
            } else {
                stmt.setNull(1, Types.DATE);
            }

            if (gracePeriod.getEndDate() != null) {
                stmt.setDate(2, Date.valueOf(gracePeriod.getEndDate()));
            } else {
                stmt.setNull(2, Types.DATE);
            }

            stmt.setString(3, gracePeriod.getNotes());
            stmt.setObject(4, gracePeriod.getAnimalId());
            stmt.setBoolean(5, gracePeriod.isActive());
            stmt.setObject(6, gracePeriod.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating grace period: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM periodo_carencia WHERE id_carencia = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting grace period: " + e.getMessage());
            throw e;
        }
    }

    // MAP RESULT SET
    private GracePeriod mapGracePeriod(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_carencia", UUID.class);

        LocalDate startDate = null;
        Date sqlStartDate = rs.getDate("data_inicio_carencia");
        if (sqlStartDate != null) {
            startDate = sqlStartDate.toLocalDate();
        }

        LocalDate endDate = null;
        Date sqlEndDate = rs.getDate("data_fim_carencia");
        if (sqlEndDate != null) {
            endDate = sqlEndDate.toLocalDate();
        }

        String notes = rs.getString("observacoes");
        UUID animalId = rs.getObject("id_animal", UUID.class);
        boolean active = rs.getBoolean("ativo");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("atualizado_em");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new GracePeriod(id, startDate, endDate, notes, animalId, updatedAt, active);
    }
}