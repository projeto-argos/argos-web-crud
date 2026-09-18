package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Herd;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HerdDAO {

    // INSERT
    public void insert(Herd herd) throws SQLException {
        String sql = "INSERT INTO rebanho (nome, raca, finalidade, id_propriedade, quantidade_original_cabecas, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, herd.getName());
            stmt.setString(2, herd.getBreed());
            stmt.setString(3, herd.getPurpose());
            stmt.setObject(4, herd.getPropertyId());
            stmt.setInt(5, herd.getOriginalHeadCount());
            stmt.setBoolean(6, herd.isActive());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting herd: " + e.getMessage());
            throw e;
        }
    }

    // FIND BY ID
    public Herd findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM rebanho WHERE id_rebanho = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapHerd(rs);
                }
            }
        }
        return null;
    }

    // FIND ALL
    public List<Herd> findAll() throws SQLException {
        String sql = "SELECT * FROM rebanho ORDER BY nome";
        List<Herd> herds = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                herds.add(mapHerd(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing herds: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return herds;
    }

    // UPDATE
    public void update(Herd herd) throws SQLException {
        String sql = "UPDATE rebanho SET nome = ?, raca = ?, finalidade = ?, quantidade_original_cabecas = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_rebanho = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, herd.getName());
            stmt.setString(2, herd.getBreed());
            stmt.setString(3, herd.getPurpose());
            stmt.setInt(4, herd.getOriginalHeadCount());
            stmt.setBoolean(5, herd.isActive());
            stmt.setObject(6, herd.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating herd: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM rebanho WHERE id_rebanho = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting herd: " + e.getMessage());
            throw e;
        }
    }

    // MAP RESULT SET
    private Herd mapHerd(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_rebanho", UUID.class);
        String name = rs.getString("nome");
        String breed = rs.getString("raca");
        String purpose = rs.getString("finalidade");
        UUID propertyId = rs.getObject("id_propriedade", UUID.class);
        boolean active = rs.getBoolean("ativo");
        int originalHeadCount = rs.getInt("quantidade_original_cabecas");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("atualizado_em");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Herd(id, breed, purpose, name, updatedAt, propertyId, active, originalHeadCount);
    }
}