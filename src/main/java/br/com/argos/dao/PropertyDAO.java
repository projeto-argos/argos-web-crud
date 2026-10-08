package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.exceptions.DataAccessException;
import br.com.argos.interfaces.GenericDAO;
import br.com.argos.model.Property;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PropertyDAO implements GenericDAO<Property, UUID> {

    @Override
    public void insert(Property property) {
        String sql = "INSERT INTO property (name, cnpj, phone, active, id_user, id_address, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getName());
            stmt.setString(2, property.getCnpj());
            stmt.setString(3, property.getPhone());
            stmt.setBoolean(4, property.isActive());
            stmt.setObject(5, property.getUserId());
            stmt.setObject(6, property.getAddressId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting property: ", e);
        }
    }

    @Override
    public Property findById(UUID id) {
        String sql = "SELECT * FROM property WHERE id_property = ?";

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
        String sql = "SELECT * FROM property ORDER BY name";
        List<Property> properties = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                properties.add(mapProperty(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing properties: " + e);
        }

        return properties;
    }

    @Override
    public void update(Property property) {
        String sql = "UPDATE property SET name = ?, cnpj = ?, phone = ?, active = ?, id_user = ?, id_address = ?, updated_at = now() " +
                "WHERE id_property = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getName());
            stmt.setString(2, property.getCnpj());
            stmt.setString(3, property.getPhone());
            stmt.setBoolean(4, property.isActive());
            stmt.setObject(5, property.getUserId());
            stmt.setObject(6, property.getAddressId());
            stmt.setObject(7, property.getIdProperty());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating property: " + e);
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM property WHERE id_property = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting property: " + id, e);
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

        return new Property(id, userId, addressId, name, cnpj, phone, updatedAt, active);
    }
}