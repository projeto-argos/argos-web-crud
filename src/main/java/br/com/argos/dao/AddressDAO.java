package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Address;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddressDAO {

    // INSERT
    public void insert(Address address) throws SQLException {
        String sql = "INSERT INTO endereco (cep, logradouro, numero, bairro, cidade, estado, observacoes, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, address.getZipCode());
            stmt.setString(2, address.getStreet());
            stmt.setInt(3, address.getNumber());
            stmt.setString(4, address.getNeighborhood());
            stmt.setString(5, address.getCity());
            stmt.setString(6, address.getState());
            stmt.setString(7, address.getNotes());
            stmt.setBoolean(8, address.isActive());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserting address: " + e.getMessage());
            throw e;
        }
    }

    // FIND BY ID
    public Address findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM endereco WHERE id_endereco = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAddress(rs);
                }
            }
        }
        return null;
    }

    // FIND ALL
    public List<Address> findAll() throws SQLException {
        String sql = "SELECT * FROM endereco ORDER BY cidade";
        List<Address> addresses = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                addresses.add(mapAddress(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error listing addresses: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return addresses;
    }

    // UPDATE
    public void update(Address address) throws SQLException {
        String sql = "UPDATE endereco SET cep = ?, logradouro = ?, numero = ?, bairro = ?, " +
                "cidade = ?, estado = ?, observacoes = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_endereco = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, address.getZipCode());
            stmt.setString(2, address.getStreet());
            stmt.setInt(3, address.getNumber());
            stmt.setString(4, address.getNeighborhood());
            stmt.setString(5, address.getCity());
            stmt.setString(6, address.getState());
            stmt.setString(7, address.getNotes());
            stmt.setBoolean(8, address.isActive());
            stmt.setObject(9, address.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating address: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM endereco WHERE id_endereco = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting address: " + e.getMessage());
            throw e;
        }
    }

    // MAP RESULT SET
    private Address mapAddress(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_endereco", UUID.class);
        String zipCode = rs.getString("cep");
        String street = rs.getString("logradouro");
        int number = rs.getInt("numero");
        String neighborhood = rs.getString("bairro");
        String city = rs.getString("cidade");
        String state = rs.getString("estado");
        String notes = rs.getString("observacoes");
        boolean active = rs.getBoolean("ativo");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("atualizado_em");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Address(id, zipCode, street, number, neighborhood, city, state, notes, updatedAt, active);
    }
}