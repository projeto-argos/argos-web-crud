package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Application;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApplicationDAO {

    // INSERT
    public void insert(Application application) throws SQLException {
        String sql = "INSERT INTO applications (user_id, medication_batch_id, batch_id, dosage, unit_of_measure, " +
                "applied_limb, applied_site, administration_route, objective, notes, date_time, active, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, application.getIdUser());
            stmt.setObject(2, application.getIdMedicationBatch());
            stmt.setObject(3, application.getIdBatch());
            stmt.setBigDecimal(4, application.getDosage());
            stmt.setString(5, application.getUnitOfMeasure());
            stmt.setString(6, application.getAppliedLimb());
            stmt.setString(7, application.getAppliedSite());
            stmt.setString(8, application.getAdministrationRoute());
            stmt.setString(9, application.getObjective());
            stmt.setString(10, application.getNotes());

            if (application.getDateTime() != null) {
                stmt.setTimestamp(11, Timestamp.valueOf(application.getDateTime()));
            } else {
                stmt.setNull(11, Types.TIMESTAMP);
            }

            stmt.setBoolean(12, application.isActive());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserting application: " + e.getMessage());
            throw e;
        }
    }

    // FIND BY ID
    public Application findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM applications WHERE id_application = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapApplication(rs);
                }
            }
        }
        return null;
    }

    // FIND ALL
    public List<Application> findAll() throws SQLException {
        String sql = "SELECT * FROM applications ORDER BY date_time DESC";
        List<Application> applications = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                applications.add(mapApplication(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing applications: " + e.getMessage());
            throw e;
        }

        return applications;
    }

    // UPDATE
    public void update(Application application) throws SQLException {
        String sql = "UPDATE applications SET user_id = ?, medication_batch_id = ?, batch_id = ?, dosage = ?, " +
                "unit_of_measure = ?, applied_limb = ?, applied_site = ?, administration_route = ?, " +
                "objective = ?, notes = ?, date_time = ?, active = ?, updated_at = now() WHERE id_application = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, application.getIdUser());
            stmt.setObject(2, application.getIdMedicationBatch());
            stmt.setObject(3, application.getIdBatch());
            stmt.setBigDecimal(4, application.getDosage());
            stmt.setString(5, application.getUnitOfMeasure());
            stmt.setString(6, application.getAppliedLimb());
            stmt.setString(7, application.getAppliedSite());
            stmt.setString(8, application.getAdministrationRoute());
            stmt.setString(9, application.getObjective());
            stmt.setString(10, application.getNotes());

            if (application.getDateTime() != null) {
                stmt.setTimestamp(11, Timestamp.valueOf(application.getDateTime()));
            } else {
                stmt.setNull(11, Types.TIMESTAMP);
            }

            stmt.setBoolean(12, application.isActive());
            stmt.setObject(13, application.getIdApplication());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating application: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM applications WHERE id_application = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting application: " + e.getMessage());
            throw e;
        }
    }

    // MAP RESULT SET
    private Application mapApplication(ResultSet rs) throws SQLException {
        UUID idApplication = rs.getObject("id_application", UUID.class);
        UUID idUser = rs.getObject("user_id", UUID.class);
        UUID idMedicationBatch = rs.getObject("medication_batch_id", UUID.class);
        UUID idBatch = rs.getObject("batch_id", UUID.class);
        BigDecimal dosage = rs.getBigDecimal("dosage");
        String unitOfMeasure = rs.getString("unit_of_measure");
        String appliedLimb = rs.getString("applied_limb");
        String appliedSite = rs.getString("applied_site");
        String administrationRoute = rs.getString("administration_route");
        String objective = rs.getString("objective");
        String notes = rs.getString("notes");

        LocalDateTime dateTime = null;
        Timestamp tsDateTime = rs.getTimestamp("date_time");
        if (tsDateTime != null) {
            dateTime = tsDateTime.toLocalDateTime();
        }

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        boolean active = rs.getBoolean("active");

        return new Application(idApplication, idUser, idMedicationBatch, idBatch, dosage,
                unitOfMeasure, appliedLimb, appliedSite, administrationRoute, objective,
                notes, dateTime, updatedAt, active);
    }
}