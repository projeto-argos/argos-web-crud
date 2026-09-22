package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Medication;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MedicationDAO {

    // CREATE
    public void insert(Medication medication) throws SQLException {
        String sql = "INSERT INTO medications (supplier_id, trade_name, active_ingredient, " +
                "therapeutic_category, unit_of_measure, active, withdrawal_period_days, dose, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, medication.getSupplierId());
            stmt.setString(2, medication.getTradeName());
            stmt.setString(3, medication.getActiveIngredient());
            stmt.setString(4, medication.getTherapeuticCategory());
            stmt.setString(5, medication.getUnitOfMeasure());
            stmt.setBoolean(6, medication.isActive());
            stmt.setInt(7, medication.getWithdrawalPeriodDays());
            stmt.setBigDecimal(8, medication.getDose());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserting medication: " + e.getMessage());
            throw e;
        }
    }

    // READ
    public Medication findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM medications WHERE id_medication = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapMedication(rs);
                }
            }
        }
        return null;
    }

    public List<Medication> findAll() throws SQLException {
        String sql = "SELECT * FROM medications ORDER BY trade_name";
        List<Medication> medications = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                medications.add(mapMedication(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing medications: " + e.getMessage());
            throw e;
        }

        return medications;
    }

    // UPDATE
    public void update(Medication medication) throws SQLException {
        String sql = "UPDATE medications SET supplier_id = ?, trade_name = ?, active_ingredient = ?, " +
                "therapeutic_category = ?, unit_of_measure = ?, active = ?, " +
                "withdrawal_period_days = ?, dose = ?, updated_at = now() WHERE id_medication = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, medication.getSupplierId());
            stmt.setString(2, medication.getTradeName());
            stmt.setString(3, medication.getActiveIngredient());
            stmt.setString(4, medication.getTherapeuticCategory());
            stmt.setString(5, medication.getUnitOfMeasure());
            stmt.setBoolean(6, medication.isActive());
            stmt.setInt(7, medication.getWithdrawalPeriodDays());
            stmt.setBigDecimal(8, medication.getDose());
            stmt.setObject(9, medication.getIdMedication());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating medication: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM medications WHERE id_medication = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting medication: " + e.getMessage());
            throw e;
        }
    }

    // MAPPER
    private Medication mapMedication(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_medication", UUID.class);
        UUID supplierId = rs.getObject("supplier_id", UUID.class);
        String tradeName = rs.getString("trade_name");
        String activeIngredient = rs.getString("active_ingredient");
        String therapeuticCategory = rs.getString("therapeutic_category");
        String unitOfMeasure = rs.getString("unit_of_measure");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        boolean active = rs.getBoolean("active");
        int withdrawalPeriodDays = rs.getInt("withdrawal_period_days");
        BigDecimal dose = rs.getBigDecimal("dose");

        return new Medication(id, supplierId, tradeName, activeIngredient, therapeuticCategory,
                unitOfMeasure, updatedAt, active, withdrawalPeriodDays, dose);
    }
}