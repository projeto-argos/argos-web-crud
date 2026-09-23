package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Animal;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AnimalDAO {

    // INSERT
    public void insert(Animal animal) throws SQLException {
        String sql = "INSERT INTO animals (batch_id, weight, ear_tag, notes, exception_reason, " +
                "exception_start_date, exception_end_date, birth_date, active, cleared_for_slaughter, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, animal.getBatchId());
            stmt.setBigDecimal(2, animal.getWeight());
            stmt.setString(3, animal.getEarTag());
            stmt.setString(4, animal.getNotes());
            stmt.setString(5, animal.getExceptionReason());

            if (animal.getExceptionStartDate() != null) {
                stmt.setDate(6, Date.valueOf(animal.getExceptionStartDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            if (animal.getExceptionEndDate() != null) {
                stmt.setDate(7, Date.valueOf(animal.getExceptionEndDate()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            if (animal.getBirthDate() != null) {
                stmt.setDate(8, Date.valueOf(animal.getBirthDate()));
            } else {
                stmt.setNull(8, Types.DATE);
            }

            stmt.setBoolean(9, animal.isActive());
            stmt.setBoolean(10, animal.isClearedForSlaughter());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting animal: " + e.getMessage());
            throw e;
        }
    }

    // FIND BY ID
    public Animal findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM animals WHERE id_animal = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAnimal(rs);
                }
            }
        }
        return null;
    }

    // FIND ALL
    public List<Animal> findAll() throws SQLException {
        String sql = "SELECT * FROM animals ORDER BY ear_tag";
        List<Animal> animals = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                animals.add(mapAnimal(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing animals: " + e.getMessage());
            throw e;
        }

        return animals;
    }

    // UPDATE
    public void update(Animal animal) throws SQLException {
        String sql = "UPDATE animals SET batch_id = ?, weight = ?, ear_tag = ?, notes = ?, " +
                "exception_reason = ?, exception_start_date = ?, exception_end_date = ?, " +
                "birth_date = ?, active = ?, cleared_for_slaughter = ?, updated_at = now() " +
                "WHERE id_animal = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, animal.getBatchId());
            stmt.setBigDecimal(2, animal.getWeight());
            stmt.setString(3, animal.getEarTag());
            stmt.setString(4, animal.getNotes());
            stmt.setString(5, animal.getExceptionReason());

            if (animal.getExceptionStartDate() != null) {
                stmt.setDate(6, Date.valueOf(animal.getExceptionStartDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            if (animal.getExceptionEndDate() != null) {
                stmt.setDate(7, Date.valueOf(animal.getExceptionEndDate()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            if (animal.getBirthDate() != null) {
                stmt.setDate(8, Date.valueOf(animal.getBirthDate()));
            } else {
                stmt.setNull(8, Types.DATE);
            }

            stmt.setBoolean(9, animal.isActive());
            stmt.setBoolean(10, animal.isClearedForSlaughter());
            stmt.setObject(11, animal.getIdAnimal());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating animal: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM animals WHERE id_animal = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting animal: " + e.getMessage());
            throw e;
        }
    }

    // MAP RESULT SET
    private Animal mapAnimal(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_animal", UUID.class);
        UUID batchId = rs.getObject("batch_id", UUID.class);
        BigDecimal weight = rs.getBigDecimal("weight");
        String earTag = rs.getString("ear_tag");
        String notes = rs.getString("notes");
        String exceptionReason = rs.getString("exception_reason");

        LocalDate exceptionStartDate = null;
        Date sqlExceptionStartDate = rs.getDate("exception_start_date");
        if (sqlExceptionStartDate != null) {
            exceptionStartDate = sqlExceptionStartDate.toLocalDate();
        }

        LocalDate exceptionEndDate = null;
        Date sqlExceptionEndDate = rs.getDate("exception_end_date");
        if (sqlExceptionEndDate != null) {
            exceptionEndDate = sqlExceptionEndDate.toLocalDate();
        }

        LocalDate birthDate = null;
        Date sqlBirthDate = rs.getDate("birth_date");
        if (sqlBirthDate != null) {
            birthDate = sqlBirthDate.toLocalDate();
        }

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        boolean active = rs.getBoolean("active");
        boolean clearedForSlaughter = rs.getBoolean("cleared_for_slaughter");

        return new Animal(id, batchId, weight, earTag, notes, exceptionReason,
                exceptionStartDate, exceptionEndDate, birthDate, updatedAt, active, clearedForSlaughter);
    }
}