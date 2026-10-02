package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.exceptions.DataAccessException;
import br.com.argos.interfaces.GenericDAO;
import br.com.argos.model.Herd;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Classe de acesso a dados (DAO).
 * Acesso ao banco para a tabela Herd (Rebanho).
 * Implementa as operações de CRUD e métodos personalizados.
 */
public class HerdDAO implements GenericDAO<Herd, UUID> {

    // CREATE
    @Override
    public void insert(Herd herd) {
        String sql = "INSERT INTO herds (property_id, name, breed, original_head_count, active, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, herd.getPropertyId());
            stmt.setString(2, herd.getName());
            stmt.setString(3, herd.getBreed());
            stmt.setObject(4, herd.getOriginalHeadCount());
            stmt.setBoolean(5, herd.isActive());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting herd: " + e);
        }
    }

    // READ
    @Override
    public Herd findById(UUID id) {
        String sql = "SELECT * FROM herds WHERE id_herd = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapHerd(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error searching for herd: " + id, e);
        }

    }

    @Override
    public List<Herd> findAll() {
        String sql = "SELECT * FROM herds ORDER BY name";
        List<Herd> herds = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                herds.add(mapHerd(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing herds: " + e);
        }

        return herds;
    }

    // UPDATE
    @Override
    public void update(Herd herd) {
        String sql = "UPDATE herds SET property_id = ?, name = ?, breed = ?, original_head_count = ?, active = ?, updated_at = now() " +
                "WHERE id_herd = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, herd.getPropertyId());
            stmt.setString(2, herd.getName());
            stmt.setString(3, herd.getBreed());
            stmt.setObject(4, herd.getOriginalHeadCount());
            stmt.setBoolean(5, herd.isActive());
            stmt.setObject(6, herd.getIdHerd());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating herd: " + e);
        }
    }

    // DELETE
    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM herds WHERE id_herd = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting herd: " + id, e);
        }
    }

    // MAPPER
    private Herd mapHerd(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_herd", UUID.class);
        UUID propertyId = rs.getObject("property_id", UUID.class);
        String name = rs.getString("name");
        String breed = rs.getString("breed");
        Integer originalHeadCount = rs.getObject("original_head_count", Integer.class);
        boolean active = rs.getBoolean("active");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Herd(id, propertyId, originalHeadCount, name, breed, updatedAt, active);
    }
}