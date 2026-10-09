package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.exceptions.DataAccessException;
import br.com.argos.interfaces.GenericDAO;
import br.com.argos.interfaces.IPropertyDAO;
import br.com.argos.model.Property;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PropertyDAO implements GenericDAO<Property, UUID>, IPropertyDAO {

    @Override
    public void insert(Property property) {
        String sql = "INSERT INTO PROPERTY (name, cnpj, phone, id_user, id_address, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getName());
            stmt.setString(2, property.getCnpj());
            stmt.setString(3, property.getPhone());
            stmt.setObject(4, property.getUserId());

            if (property.getAddressId() != null) {
                stmt.setObject(5, property.getAddressId());
            } else {
                stmt.setNull(5, Types.OTHER);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error inserting property: ", e);
        }
    }

    @Override
    public Property findById(UUID id) {
        String sql = "SELECT * FROM PROPERTY WHERE id_property = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapProperty(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error searching for property: " + id, e);
        }
    }

    @Override
    public List<Property> findAll() {
        String sql = "SELECT * FROM PROPERTY WHERE active = true ORDER BY name";
        List<Property> properties = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                properties.add(mapProperty(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing properties: ", e);
        }

        return properties;
    }

    @Override
    public List<Property> findByName(String name) {
        String sql = "SELECT * FROM PROPERTY WHERE name ILIKE ? AND active = true ORDER BY name";
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
            throw new DataAccessException("Error searching for property by name: " + name, e);
        }
    }

    @Override
    public List<Property> findByUser(UUID userId) {
        String sql = "SELECT * FROM PROPERTY WHERE id_user = ? AND active = true ORDER BY name";
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
            throw new DataAccessException("Error searching for properties by user: " + userId, e);
        }
    }

    @Override
    public void update(Property property) {
        String sql = "UPDATE PROPERTY SET name = ?, cnpj = ?, phone = ?, id_user = ?, id_address = ?, " +
                "updated_at = now() WHERE id_property = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getName());
            stmt.setString(2, property.getCnpj());
            stmt.setString(3, property.getPhone());
            stmt.setObject(4, property.getUserId());

            if (property.getAddressId() != null) {
                stmt.setObject(5, property.getAddressId());
            } else {
                stmt.setNull(5, Types.OTHER);
            }

            stmt.setObject(6, property.getIdProperty());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Property not found " + property.getIdProperty());
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error updating property: ", e);
        }
    }

    // SOFT DELETE
    @Override
    public void delete(UUID id) {
        String sql = "UPDATE PROPERTY SET active = FALSE, updated_at = now() " +
                "WHERE id_property = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Property not found " + id);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting property: " + id, e);
        }
    }

    // SOFT DELETE de todas as propriedades de um usuário
    @Override
    public int deleteByUser(UUID userId) {
        String sql = "UPDATE PROPERTY SET active = FALSE, updated_at = now() " +
                "WHERE id_user = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, userId);
            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting properties by user: " + userId, e);
        }
    }

    private Property mapProperty(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_property", UUID.class);
        UUID userId = rs.getObject("id_user", UUID.class);
        UUID addressId = rs.getObject("id_address", UUID.class);
        String name = rs.getString("name");
        String cnpj = rs.getString("cnpj");
        String phone = rs.getString("phone");
        boolean active = rs.getBoolean("active");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Property(id, userId, addressId, name, phone, cnpj, updatedAt, active);
    }
}