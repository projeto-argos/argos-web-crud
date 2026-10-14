package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.exceptions.DataAccessException;
import br.com.argos.interfaces.GenericDAO;
import br.com.argos.interfaces.IHerdDAO;
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
public class HerdDAO implements GenericDAO<Herd, UUID>, IHerdDAO {

    // CREATE
    @Override
    public void insert(Herd herd) {
        String sql = "INSERT INTO HERD (name, breed, purpose, head_count, id_property, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, herd.getName());
            stmt.setString(2, herd.getBreed());
            stmt.setString(3, herd.getPurpose());
            stmt.setInt(4, herd.getHeadCount());
            stmt.setObject(5, herd.getPropertyId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting herd: ", e);
        }
    }

    // READ
    @Override
    public Herd findById(UUID id) {
        String sql = "SELECT * FROM HERD WHERE id_herd = ? AND active = true";

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
        String sql = "SELECT * FROM HERD WHERE active = true ORDER BY name";
        List<Herd> herds = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                herds.add(mapHerd(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing herds: ", e);
        }

        return herds;
    }

    @Override
    public List<Herd> findByName(String name) {
        String sql = "SELECT * FROM HERD WHERE name ILIKE ? AND active = true ORDER BY name";
        List<Herd> herds = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    herds.add(mapHerd(rs));
                }
            }
            return herds;

        } catch (SQLException e) {
            throw new DataAccessException("Error searching for herd by name: " + name, e);
        }
    }

    @Override
    public List<Herd> findByProperty(UUID propertyId) {
        String sql = "SELECT * FROM HERD WHERE id_property = ? AND active = true ORDER BY name";
        List<Herd> herds = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, propertyId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    herds.add(mapHerd(rs));
                }
            }
            return herds;

        } catch (SQLException e) {
            throw new DataAccessException("Error searching for herds by property: " + propertyId, e);
        }
    }

    // UPDATE
    @Override
    public void update(Herd herd) {
        String sql = "UPDATE HERD SET name = ?, breed = ?, purpose = ?, head_count = ?, id_property = ?, " +
                "updated_at = now() WHERE id_herd = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, herd.getName());
            stmt.setString(2, herd.getBreed());
            stmt.setString(3, herd.getPurpose());
            stmt.setInt(4, herd.getHeadCount());
            stmt.setObject(5, herd.getPropertyId());
            stmt.setObject(6, herd.getIdHerd());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Herd not found " + herd.getIdHerd());
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error updating herd: ", e);
        }
    }

    // SOFT DELETE
    @Override
    public void delete(UUID id) {
        String sql = "UPDATE HERD SET active = FALSE, updated_at = now() WHERE id_herd = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Herd not found " + id);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting herd: " + id, e);
        }
    }

    // SOFT DELETE de todos os rebanhos de uma propriedade
    @Override
    public int deleteByProperty(UUID propertyId) {
        String sql = "UPDATE HERD SET active = FALSE, updated_at = now() WHERE id_property = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, propertyId);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting herds by property: " + propertyId, e);
        }
    }

    // MAPPER
    private Herd mapHerd(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_herd", UUID.class);
        UUID propertyId = rs.getObject("id_property", UUID.class);
        String name = rs.getString("name");
        String breed = rs.getString("breed");
        String purpose = rs.getString("purpose");
        Integer headCount = rs.getObject("head_count", Integer.class);
        boolean active = rs.getBoolean("active");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Herd(id, propertyId,headCount, purpose, name, breed, updatedAt, active);
    }
}