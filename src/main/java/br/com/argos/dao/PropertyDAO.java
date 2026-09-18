package br.com.argos.dao;

import br.com.argos.model.Property;
import br.com.argos.connection.ConnectionFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PropertyDAO {

    // INSERT
    public void insert(Property property) throws SQLException {
        String sql = "INSERT INTO propriedade (nome, telefone, ativo, id_usuario, cnpj, id_endereco, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getName());
            stmt.setString(2, property.getPhone());
            stmt.setBoolean(3, property.isActive());
            stmt.setObject(4, property.getUserId());
            stmt.setString(5, property.getCnpj());
            stmt.setObject(6, property.getAddressId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting property: " + e.getMessage());
            throw e;
        }
    }

    // READ
    public Property findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM propriedade WHERE id_propriedade = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapProperty(rs);
                }
            }
        }
        return null;
    }

    public List<Property> findAll() throws SQLException {
        String sql = "SELECT * FROM propriedade ORDER BY nome";
        List<Property> properties = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                properties.add(mapProperty(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing properties: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return properties;
    }

    // UPDATE
    public void update(Property property) throws SQLException {
        String sql = "UPDATE propriedade SET nome = ?, telefone = ?, ativo = ?, id_usuario = ?, cnpj = ?, id_endereco = ?, atualizado_em = now() " +
                "WHERE id_propriedade = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, property.getName());
            stmt.setString(2, property.getPhone());
            stmt.setBoolean(3, property.isActive());
            stmt.setObject(4, property.getUserId());
            stmt.setString(5, property.getCnpj());
            stmt.setObject(6, property.getAddressId());
            stmt.setObject(7, property.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating property: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM propriedade WHERE id_propriedade = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting property: " + e.getMessage());
            throw e;
        }
    }

    private Property mapProperty(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_propriedade", UUID.class);
        String name = rs.getString("nome");
        String phone = rs.getString("telefone");
        boolean active = rs.getBoolean("ativo");
        UUID userId = rs.getObject("id_usuario", UUID.class);
        String cnpj = rs.getString("cnpj");
        UUID addressId = rs.getObject("id_endereco", UUID.class);

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("atualizado_em");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Property(id, updatedAt, phone, name, userId, active, cnpj, addressId);
    }
}