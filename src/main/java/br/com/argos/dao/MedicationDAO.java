package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Medication;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MedicationDAO {

    // INSERT
    public void insert(Medication medication) throws SQLException {
        String sql = "INSERT INTO medicamento (nome_comercial, dose, principio_ativo, carencia_indicada_dias, " +
                "categoria_terapeutica, id_fornecedor, ativo, unidade_medida, indicacao, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medication.getTradeName());
            stmt.setDouble(2, medication.getDose());
            stmt.setString(3, medication.getActiveIngredient());
            stmt.setInt(4, medication.getGracePeriodDays());
            stmt.setString(5, medication.getTherapeuticCategory());
            stmt.setObject(6, medication.getSupplierId());
            stmt.setBoolean(7, medication.isActive());
            stmt.setString(8, medication.getUnitOfMeasure());
            stmt.setString(9, medication.getIndication());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserting medication: " + e.getMessage());
            throw e;
        }
    }

    // FIND BY ID
    public Medication findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM medicamento WHERE id_medicamento = ?";

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

    // FIND ALL
    public List<Medication> findAll() throws SQLException {
        String sql = "SELECT * FROM medicamento ORDER BY nome_comercial";
        List<Medication> medications = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                medications.add(mapMedication(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing medications: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return medications;
    }

    // UPDATE
    public void update(Medication medication) throws SQLException {
        String sql = "UPDATE medicamento SET nome_comercial = ?, dose = ?, principio_ativo = ?, " +
                "carencia_indicada_dias = ?, categoria_terapeutica = ?, id_fornecedor = ?, ativo = ?, " +
                "unidade_medida = ?, indicacao = ?, atualizado_em = now() WHERE id_medicamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medication.getTradeName());
            stmt.setDouble(2, medication.getDose());
            stmt.setString(3, medication.getActiveIngredient());
            stmt.setInt(4, medication.getGracePeriodDays());
            stmt.setString(5, medication.getTherapeuticCategory());
            stmt.setObject(6, medication.getSupplierId());
            stmt.setBoolean(7, medication.isActive());
            stmt.setString(8, medication.getUnitOfMeasure());
            stmt.setString(9, medication.getIndication());
            stmt.setObject(10, medication.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating medication: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM medicamento WHERE id_medicamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting medication: " + e.getMessage());
            throw e;
        }
    }

    // MAP RESULT SET
    private Medication mapMedication(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_medicamento", UUID.class);
        String tradeName = rs.getString("nome_comercial");
        double dose = rs.getDouble("dose");
        String activeIngredient = rs.getString("principio_ativo");
        int gracePeriodDays = rs.getInt("carencia_indicada_dias");
        String therapeuticCategory = rs.getString("categoria_terapeutica");
        UUID supplierId = rs.getObject("id_fornecedor", UUID.class);
        boolean active = rs.getBoolean("ativo");
        String unitOfMeasure = rs.getString("unidade_medida");
        String indication = rs.getString("indicacao");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("atualizado_em");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Medication(id, tradeName, dose, activeIngredient, gracePeriodDays,
                therapeuticCategory, updatedAt, supplierId, active, unitOfMeasure, indication);
    }
}