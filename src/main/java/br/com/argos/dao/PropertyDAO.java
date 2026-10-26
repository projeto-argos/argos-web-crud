
package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.exceptions.DataAccessException;
import br.com.argos.interfaces.GenericDAO;
import br.com.argos.interfaces.IPropertyDAO;
import br.com.argos.model.Property;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PropertyDAO implements GenericDAO<Property, UUID>, IPropertyDAO {

//    CREATE
    @Override
    public void insert(Property property) {
        String sql = """
                INSERT INTO property
                    (name, cnpj, phone, id_address, updated_at)
                VALUES (?, ?, ?, ?, now())
                RETURNING id_property
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getName());
            stmt.setString(2, property.getCnpj());
            stmt.setString(3, property.getPhone());
            stmt.setObject(4, property.getAddressId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    property.setIdProperty(
                            rs.getObject("id_property", UUID.class));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error inserting property", e);
        }
    }

//    FIND BY ID
    @Override
    public Property findById(UUID id) {
        String sql = """
                SELECT *
                FROM property
                WHERE id_property = ? AND active = true
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? mapProperty(rs) : null;
            }

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error searching for property: " + id, e);
        }
    }

//    FIND ALL
    @Override
    public List<Property> findAll() {
        String sql = """
                SELECT *
                FROM property
                WHERE active = true
                ORDER BY name
                """;

        List<Property> properties = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                properties.add(mapProperty(rs));
            }

            return properties;

        } catch (SQLException e) {
            throw new DataAccessException("Error listing properties", e);
        }
    }

//      FIND BY NAME
    @Override
    public List<Property> findByName(String name) {
        String sql = """
                SELECT *
                FROM property
                WHERE name ILIKE ? AND active = true
                ORDER BY name
                """;

        List<Property> properties = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    properties.add(mapProperty(rs));
                }
            }

            return properties;

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error searching properties by name: " + name, e);
        }
    }

//    FIND BY USER
    @Override
    public List<Property> findByUser(UUID userId) {
        String sql = """
                SELECT p.*
                FROM property p
                INNER JOIN user_property up
                    ON up.id_property = p.id_property
                WHERE up.id_user = ?
                  AND p.active = true
                ORDER BY p.name
                """;

        List<Property> properties = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    properties.add(mapProperty(rs));
                }
            }

            return properties;

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error searching properties by user: " + userId, e);
        }
    }

//    UPDATE
    @Override
    public void update(Property property) {
        String sql = """
                UPDATE property
                SET name = ?,
                    cnpj = ?,
                    phone = ?,
                    id_address = ?,
                    updated_at = now()
                WHERE id_property = ? AND active = true
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getName());
            stmt.setString(2, property.getCnpj());
            stmt.setString(3, property.getPhone());
            stmt.setObject(4, property.getAddressId());
            stmt.setObject(5, property.getIdProperty());

            if (stmt.executeUpdate() == 0) {
                throw new DataAccessException(
                        "Property not found: " + property.getIdProperty());
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error updating property", e);
        }
    }

//    SOFT DELETE
    @Override
    public void delete(UUID id) {
        String sql = """
                UPDATE property
                SET active = false, updated_at = now()
                WHERE id_property = ? AND active = true
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            if (stmt.executeUpdate() == 0) {
                throw new DataAccessException("Property not found: " + id);
            }

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error deleting property: " + id, e);
        }
    }

//    DELETE BY USER
    @Override
    public int deleteByUser(UUID userId) {
        String sql = """
                UPDATE property
                SET active = false, updated_at = now()
                WHERE active = true
                  AND id_property IN (
                      SELECT id_property
                      FROM user_property
                      WHERE id_user = ?
                  )
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, userId);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error deleting properties by user: " + userId, e);
        }
    }

//    MAPPER
    private Property mapProperty(ResultSet rs) throws SQLException {
        UUID idProperty = rs.getObject("id_property", UUID.class);
        UUID addressId = rs.getObject("id_address", UUID.class);

        String name = rs.getString("name");
        String cnpj = rs.getString("cnpj");
        String phone = rs.getString("phone");
        boolean active = rs.getBoolean("active");

        Timestamp timestamp = rs.getTimestamp("updated_at");
        java.time.LocalDateTime updatedAt =
                timestamp == null ? null : timestamp.toLocalDateTime();

        List<UUID> userIds = new ArrayList<>();

        return new Property(idProperty, addressId, userIds, name, phone, cnpj, updatedAt, active);
    }
}