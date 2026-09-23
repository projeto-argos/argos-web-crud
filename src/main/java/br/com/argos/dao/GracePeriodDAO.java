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
        String sql = "INSERT INTO grace_periods (animal_id, notes, start_date, end_date, active, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, gracePeriod.getAnimalId());
            stmt.setString(2, gracePeriod.getNotes());

            if (gracePeriod.getStartDate() != null) {
                stmt.setDate(3, Date.valueOf(gracePeriod.getStartDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            if (gracePeriod.getEndDate() != null) {
                stmt.setDate(4, Date.valueOf(gracePeriod.getEndDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setBoolean(5, gracePeriod.isActive());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserting grace period: " + e.getMessage());
            throw e;
        }
    }

    // FIND BY ID
    public GracePeriod findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM grace_periods WHERE id_grace_period = ?";

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
        String sql = "SELECT * FROM grace_periods ORDER BY end_date DESC";
        List<GracePeriod> gracePeriods = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                gracePeriods.add(mapGracePeriod(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing grace periods: " + e.getMessage());
            throw e;
        }

        return gracePeriods;
    }

    // UPDATE
    public void update(GracePeriod gracePeriod) throws SQLException {
        String sql = "UPDATE grace_periods SET animal_id = ?, notes = ?, start_date = ?, " +
                "end_date = ?, active = ?, updated_at = now() WHERE id_grace_period = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, gracePeriod.getAnimalId());
            stmt.setString(2, gracePeriod.getNotes());

            if (gracePeriod.getStartDate() != null) {
                stmt.setDate(3, Date.valueOf(gracePeriod.getStartDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            if (gracePeriod.getEndDate() != null) {
                stmt.setDate(4, Date.valueOf(gracePeriod.getEndDate()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setBoolean(5, gracePeriod.isActive());
            stmt.setObject(6, gracePeriod.getIdGracePeriod());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating grace period: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM grace_periods WHERE id_grace_period = ?";

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
        UUID idGracePeriod = rs.getObject("id_grace_period", UUID.class);
        UUID animalId = rs.getObject("animal_id", UUID.class);
        String notes = rs.getString("notes");

        LocalDate startDate = null;
        Date sqlStartDate = rs.getDate("start_date");
        if (sqlStartDate != null) {
            startDate = sqlStartDate.toLocalDate();
        }

        LocalDate endDate = null;
        Date sqlEndDate = rs.getDate("end_date");
        if (sqlEndDate != null) {
            endDate = sqlEndDate.toLocalDate();
        }

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        boolean active = rs.getBoolean("active");

        return new GracePeriod(idGracePeriod, animalId, notes, startDate, endDate, updatedAt, active);
    }
}