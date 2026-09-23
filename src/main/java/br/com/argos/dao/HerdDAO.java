package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Herd;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HerdDAO {

    // CREATE
    public void insert(Herd herd) throws SQLException {
        String sql = "INSERT INTO herds (property_id, name, breed, description, active, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, herd.getPropertyId());
            stmt.setString(2, herd.getName());
            stmt.setString(3, herd.getBreed());
//            stmt.setString(4, herd.get());
            stmt.setBoolean(5, herd.isActive());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting herd: " + e.getMessage());
            throw e;
        }
    }

    // READ
    public Herd findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM herds WHERE id_herd = ?";

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

    public List<Herd> findAll() throws SQLException {
        String sql = "SELECT * FROM herds ORDER BY name";
        List<Herd> herds = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                herds.add(mapHerd(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing herds: " + e.getMessage());
            throw e;
        }

        return herds;
    }

    // UPDATE
    public void update(Herd herd) throws SQLException {
        String sql = "UPDATE herds SET property_id = ?, name = ?, breed = ?, description = ?, active = ?, updated_at = now() " +
                "WHERE id_herd = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, herd.getPropertyId());
            stmt.setString(2, herd.getName());
            stmt.setString(3, herd.getBreed());
            stmt.setString(4, herd.getDescription());
            stmt.setBoolean(5, herd.isActive());
            stmt.setObject(6, herd.getIdHerd());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating herd: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM herds WHERE id_herd = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting herd: " + e.getMessage());
            throw e;
        }
    }

    // MAPPER
    private Herd mapHerd(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_herd", UUID.class);
        UUID propertyId = rs.getObject("property_id", UUID.class);
        String name = rs.getString("name");
        String breed = rs.getString("breed");
        String description = rs.getString("description");
        boolean active = rs.getBoolean("active");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Herd(id, propertyId, name, breed, description, updatedAt, active);
    }
}