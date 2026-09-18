package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Application;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ApplicationDAO {

    // INSERT
    public void insert(Application application) throws SQLException {
        String sql = "INSERT INTO aplicacao (dose, unidade_medida, membro_aplicado, local_aplicado, via_administracao, " +
                "objetivo, observacoes, data_hora, ativo, id_usuario, id_lote_medicamento, id_lote, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, application.getDose());
            stmt.setString(2, application.getUnitOfMeasure());
            stmt.setString(3, application.getAppliedLimb());
            stmt.setString(4, application.getAppliedLocation());
            stmt.setString(5, application.getAdministrationRoute());
            stmt.setString(6, application.getObjective());
            stmt.setString(7, application.getNotes());

            if (application.getDateTime() != null) {
                stmt.setTimestamp(8, Timestamp.valueOf(application.getDateTime()));
            } else {
                stmt.setNull(8, Types.TIMESTAMP);
            }

            stmt.setBoolean(9, application.isActive());
            stmt.setObject(10, application.getUserId());
            stmt.setObject(11, application.getMedicationBatchId());
            stmt.setObject(12, application.getBatchId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserting application: " + e.getMessage());
            throw e;
        }
    }

    // FIND BY ID
    public Application findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM aplicacao WHERE id_aplicacao = ?";

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
        String sql = "SELECT * FROM aplicacao ORDER BY data_hora";
        List<Application> applications = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                applications.add(mapApplication(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing applications: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return applications;
    }

    // UPDATE
    public void update(Application application) throws SQLException {
        String sql = "UPDATE aplicacao SET dose = ?, unidade_medida = ?, membro_aplicado = ?, local_aplicado = ?, " +
                "via_administracao = ?, objetivo = ?, observacoes = ?, data_hora = ?, ativo = ?, id_usuario = ?, " +
                "id_lote_medicamento = ?, id_lote = ?, atualizado_em = now() WHERE id_aplicacao = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, application.getDose());
            stmt.setString(2, application.getUnitOfMeasure());
            stmt.setString(3, application.getAppliedLimb());
            stmt.setString(4, application.getAppliedLocation());
            stmt.setString(5, application.getAdministrationRoute());
            stmt.setString(6, application.getObjective());
            stmt.setString(7, application.getNotes());

            if (application.getDateTime() != null) {
                stmt.setTimestamp(8, Timestamp.valueOf(application.getDateTime()));
            } else {
                stmt.setNull(8, Types.TIMESTAMP);
            }

            stmt.setBoolean(9, application.isActive());
            stmt.setObject(10, application.getUserId());
            stmt.setObject(11, application.getMedicationBatchId());
            stmt.setObject(12, application.getBatchId());
            stmt.setObject(13, application.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating application: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM aplicacao WHERE id_aplicacao = ?";

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
        UUID id = rs.getObject("id_aplicacao", UUID.class);
        double dose = rs.getDouble("dose");
        String unitOfMeasure = rs.getString("unidade_medida");
        String appliedLimb = rs.getString("membro_aplicado");
        String appliedLocation = rs.getString("local_aplicado");
        String administrationRoute = rs.getString("via_administracao");
        String objective = rs.getString("objetivo");
        String notes = rs.getString("observacoes");
        boolean active = rs.getBoolean("ativo");

        UUID userId = rs.getObject("id_usuario", UUID.class);
        UUID medicationBatchId = rs.getObject("id_lote_medicamento", UUID.class);
        UUID batchId = rs.getObject("id_lote", UUID.class);

        LocalDateTime dateTime = null;
        Timestamp tsDateTime = rs.getTimestamp("data_hora");
        if (tsDateTime != null) {
            dateTime = tsDateTime.toLocalDateTime();
        }

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("atualizado_em");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Application(id, dose, unitOfMeasure, appliedLimb, appliedLocation,
                administrationRoute, objective, notes, dateTime, updatedAt, active,
                userId, medicationBatchId, batchId);
    }
}