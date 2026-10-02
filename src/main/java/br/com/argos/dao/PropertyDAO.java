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

/**
 * Classe de acesso a dados (DAO).
 * Acesso ao banco para a tabela Property (Propriedade).
 * Implementa as operações de CRUD e métodos personalizados.
 */
public class PropertyDAO implements GenericDAO<Property, UUID> {

    // CREATE
    @Override
    public void insert(Property property) {
        String sql = "INSERT INTO properties (name, phone, cnpj, email, active, user_id, address_id, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getName());
            stmt.setString(2, property.getPhone());
            stmt.setString(3, property.getCnpj());
            stmt.setString(4, property.getEmail());
            stmt.setBoolean(5, property.isActive());
            stmt.setObject(6, property.getUserId());
            stmt.setObject(7, property.getAddressId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting property: " + e);
        }
    }

//    Find columns by ID
    @Override
    public Property findById(UUID id) {
        String sql = "SELECT * FROM properties WHERE id_property = ?";

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

//    Find all
    @Override
    public List<Property> findAll() {
        String sql = "SELECT * FROM properties ORDER BY name";
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

    // UPDATE
    @Override
    public void update(Property property) {
        String sql = "UPDATE properties SET name = ?, phone = ?, cnpj = ?, email = ?, active = ?, user_id = ?, address_id = ?, updated_at = now() " +
                "WHERE id_property = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getName());
            stmt.setString(2, property.getPhone());
            stmt.setString(3, property.getCnpj());
            stmt.setString(4, property.getEmail());
            stmt.setBoolean(5, property.isActive());
            stmt.setObject(6, property.getUserId());
            stmt.setObject(7, property.getAddressId());
            stmt.setObject(8, property.getIdProperty());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating property: " + e);

        }
    }

    // DELETE
    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM properties WHERE id_property = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting property: " + id, e);
        }
    }

    // MAPPER
    private Property mapProperty(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_property", UUID.class);
        UUID userId = rs.getObject("user_id", UUID.class);
        UUID addressId = rs.getObject("address_id", UUID.class);
        String name = rs.getString("name");
        String phone = rs.getString("phone");
        String cnpj = rs.getString("cnpj");
        String email = rs.getString("email");
        boolean active = rs.getBoolean("active");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Property(id, userId, addressId, name, phone, cnpj, email, updatedAt, active);
    }
}