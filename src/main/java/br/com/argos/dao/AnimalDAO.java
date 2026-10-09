package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.exceptions.DataAccessException;
import br.com.argos.interfaces.GenericDAO;
import br.com.argos.model.Animal;
import br.com.argos.interfaces.IAnimalDAO;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AnimalDAO implements GenericDAO<Animal, UUID>, IAnimalDAO {

    @Override
    public void insert(Animal animal) {
        String sql = "INSERT INTO animal (ear_tag, weight, birth_date, exception_reason, " +
                "exception_start_date, exception_end_date, cleared_for_slaughter, notes, " +
                "id_batch, id_origin_batch, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, animal.getEarTag());

            if(animal.getWeight() != null){
                stmt.setBigDecimal(2, animal.getWeight());
            } else {
                stmt.setNull(2, Types.NUMERIC);
            }

            if (animal.getBirthDate() != null) {
                stmt.setDate(3, Date.valueOf(animal.getBirthDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            stmt.setString(4, animal.getExceptionReason());

            if (animal.getExceptionStartDate() != null) {
                stmt.setDate(5, Date.valueOf(animal.getExceptionStartDate()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            if (animal.getExceptionEndDate() != null) {
                stmt.setDate(6, Date.valueOf(animal.getExceptionEndDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            stmt.setBoolean(7, animal.isClearedForSlaughter());
            stmt.setString(8, animal.getNotes());
            stmt.setObject(9, animal.getBatchId());

            if (animal.getOriginBatchId() != null) {
                stmt.setObject(10, animal.getOriginBatchId());
            } else {
                stmt.setNull(10, Types.OTHER);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error inserting animal: ", e);
        }
    }

    @Override
    public Animal findById(UUID id) {
        String sql = "SELECT * FROM animal WHERE id_animal = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAnimal(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error searching for animal: " + id, e);
        }
    }

    @Override
    public Animal findByEarTag(String earTag) {
        String sql = "SELECT * FROM animal WHERE ear_tag = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, earTag);

            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    return mapAnimal(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("Error searching for animal by eartag: " + earTag, e);
        }
    }

    @Override
    public List<Animal> findByBatch(UUID idBatch) {
        String sql = "SELECT * FROM animal WHERE id_batch = ? AND active = true";
        List<Animal> animals = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, idBatch);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    animals.add(mapAnimal(rs));
                }
            }
            return animals;

        } catch (SQLException e) {
            throw new DataAccessException("Error searching for animal by batch id: " + idBatch, e);
        }
    }

    @Override
    public List<Animal> findAll() {
        String sql = "SELECT * FROM animal WHERE active = true ORDER BY ear_tag";
        List<Animal> animals = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                animals.add(mapAnimal(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing animals: ", e);
        }

        return animals;
    }

    @Override
    public void update(Animal animal) {
        String sql = "UPDATE animal SET ear_tag = ?, weight = ?, birth_date = ?, exception_reason = ?, " +
                "exception_start_date = ?, exception_end_date = ?, cleared_for_slaughter = ?, notes = ?, " +
                "active = ?, id_batch = ?, id_origin_batch = ?, updated_at = now() " +
                "WHERE id_animal = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, animal.getEarTag());

            if(animal.getWeight() != null){
                stmt.setBigDecimal(2, animal.getWeight());
            } else {
                stmt.setNull(2, Types.NUMERIC);
            }

            if (animal.getBirthDate() != null) {
                stmt.setDate(3, Date.valueOf(animal.getBirthDate()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            stmt.setString(4, animal.getExceptionReason());

            if (animal.getExceptionStartDate() != null) {
                stmt.setDate(5, Date.valueOf(animal.getExceptionStartDate()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            if (animal.getExceptionEndDate() != null) {
                stmt.setDate(6, Date.valueOf(animal.getExceptionEndDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            stmt.setBoolean(7, animal.isClearedForSlaughter());
            stmt.setString(8, animal.getNotes());
            stmt.setBoolean(9, animal.isActive());
            stmt.setObject(10, animal.getBatchId());

            if (animal.getOriginBatchId() != null) {
                stmt.setObject(11, animal.getOriginBatchId());
            } else {
                stmt.setNull(11, Types.OTHER);
            }

            stmt.setObject(12, animal.getIdAnimal());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Animal not found " + animal.getIdAnimal());
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error updating animal: ", e);
        }
    }

//    SOFT DELETE
    @Override
    public void delete(UUID id) {
        String sql = "UPDATE animal SET active = FALSE, updated_at = now() WHERE id_animal = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Animal not found " + id);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting animal: " + id, e);
        }
    }

    // SOFT DELETE por brinco
    @Override
    public void deleteByEarTag(String earTag) {
        String sql = "UPDATE animal SET active = FALSE, updated_at = now() WHERE ear_tag = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, earTag);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Animal not found with ear tag " + earTag);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting animal by ear tag: " + earTag, e);
        }
    }

    private Animal mapAnimal(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_animal", UUID.class);
        UUID batchId = rs.getObject("id_batch", UUID.class);
        UUID originBatchId = rs.getObject("id_origin_batch", UUID.class);
        String earTag = rs.getString("ear_tag");
        BigDecimal weight = rs.getBigDecimal("weight");
        String exceptionReason = rs.getString("exception_reason");
        String notes = rs.getString("notes");

        LocalDate birthDate = null;
        Date sqlBirthDate = rs.getDate("birth_date");
        if (sqlBirthDate != null) {
            birthDate = sqlBirthDate.toLocalDate();
        }

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

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        boolean clearedForSlaughter = rs.getBoolean("cleared_for_slaughter");
        boolean active = rs.getBoolean("active");

        return new Animal(id, batchId, weight, originBatchId, earTag, notes, exceptionReason,
                exceptionStartDate, exceptionEndDate, birthDate, updatedAt, active, clearedForSlaughter);
    }
}