package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Animal;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AnimalDAO {

    // INSERT
    public void insert(Animal animal) throws SQLException {
        String sql = "INSERT INTO animal (peso, observacoes, brinco, id_lote, ativo, data_nascimento, " +
                "data_inicio_excecao, data_fim_excecao, motivo_excecao, liberado_abate, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, animal.getWeight());
            stmt.setString(2, animal.getNotes());
            stmt.setString(3, animal.getTag());
            stmt.setObject(4, animal.getBatchId());
            stmt.setBoolean(5, animal.isActive());

            if (animal.getBirthDate() != null) {
                stmt.setDate(6, Date.valueOf(animal.getBirthDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            if (animal.getExceptionStartDate() != null) {
                stmt.setDate(7, Date.valueOf(animal.getExceptionStartDate()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            if (animal.getExceptionEndDate() != null) {
                stmt.setDate(8, Date.valueOf(animal.getExceptionEndDate()));
            } else {
                stmt.setNull(8, Types.DATE);
            }

            stmt.setString(9, animal.getExceptionReason());
            stmt.setBoolean(10, animal.isReleasedForSlaughter());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting animal: " + e.getMessage());
            throw e;
        }
    }

    // FIND BY ID
    public Animal findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM animal WHERE id_animal = ?";

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
        String sql = "SELECT * FROM animal ORDER BY brinco";
        List<Animal> animals = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                animals.add(mapAnimal(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing animals: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return animals;
    }

    // UPDATE
    public void update(Animal animal) throws SQLException {
        String sql = "UPDATE animal SET peso = ?, observacoes = ?, brinco = ?, id_lote = ?, ativo = ?, " +
                "data_nascimento = ?, data_inicio_excecao = ?, data_fim_excecao = ?, motivo_excecao = ?, " +
                "liberado_abate = ?, atualizado_em = now() WHERE id_animal = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, animal.getWeight());
            stmt.setString(2, animal.getNotes());
            stmt.setString(3, animal.getTag());
            stmt.setObject(4, animal.getBatchId());
            stmt.setBoolean(5, animal.isActive());

            if (animal.getBirthDate() != null) {
                stmt.setDate(6, Date.valueOf(animal.getBirthDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            if (animal.getExceptionStartDate() != null) {
                stmt.setDate(7, Date.valueOf(animal.getExceptionStartDate()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            if (animal.getExceptionEndDate() != null) {
                stmt.setDate(8, Date.valueOf(animal.getExceptionEndDate()));
            } else {
                stmt.setNull(8, Types.DATE);
            }

            stmt.setString(9, animal.getExceptionReason());
            stmt.setBoolean(10, animal.isReleasedForSlaughter());
            stmt.setObject(11, animal.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating animal: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM animal WHERE id_animal = ?";

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
        double weight = rs.getDouble("peso");

        LocalDate birthDate = null;
        Date sqlBirthDate = rs.getDate("data_nascimento");
        if (sqlBirthDate != null) {
            birthDate = sqlBirthDate.toLocalDate();
        }

        String notes = rs.getString("observacoes");
        String tag = rs.getString("brinco");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("atualizado_em");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        UUID batchId = rs.getObject("id_lote", UUID.class);
        boolean active = rs.getBoolean("ativo");

        LocalDate exceptionStartDate = null;
        Date sqlExceptionStartDate = rs.getDate("data_inicio_excecao");
        if (sqlExceptionStartDate != null) {
            exceptionStartDate = sqlExceptionStartDate.toLocalDate();
        }

        LocalDate exceptionEndDate = null;
        Date sqlExceptionEndDate = rs.getDate("data_fim_excecao");
        if (sqlExceptionEndDate != null) {
            exceptionEndDate = sqlExceptionEndDate.toLocalDate();
        }

        String exceptionReason = rs.getString("motivo_excecao");
        boolean releasedForSlaughter = rs.getBoolean("liberado_abate");

        return new Animal(id, weight, birthDate, notes, tag, updatedAt, batchId, active,
                exceptionStartDate, exceptionEndDate, exceptionReason, releasedForSlaughter);
    }
}