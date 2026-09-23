package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Address;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddressDAO {

    // CREATE
    public void insert(Address address) throws SQLException {
        String sql = "INSERT INTO addresses (number, complement, street, city, state, updated_at, active) " +
                "VALUES (?, ?, ?, ?, ?, now(), ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (address.getNumber() != null) {
                stmt.setInt(1, address.getNumber());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, address.getComplement());


            stmt.setString(3, address.getStreet());
            stmt.setString(4, address.getCity());
            stmt.setString(5, address.getState());
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setBoolean(7, address.isActive());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error inserting address: " + e.getMessage());
            throw e;
        }
    }

    // READ
    public Address findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM addresses WHERE id_address = ?";

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

    public List<Address> findAll() throws SQLException {
        String sql = "SELECT * FROM addresses ORDER BY street";
        List<Address> addresses = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                addresses.add(mapAddress(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error listing addresses: " + e.getMessage());
            throw e;
        }
        return addresses;
    }

    // UPDATE
    public void update(Address address) throws SQLException {
        String sql = "UPDATE addresses SET number = ?, complement = ?, street = ?, city = ?, " +
                "state = ?, updated_at = now(), active = ? WHERE id_address = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (address.getNumber() != null) {
                stmt.setInt(1, address.getNumber());
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.setString(2, address.getComplement());


            stmt.setString(3, address.getStreet());
            stmt.setString(4, address.getCity());
            stmt.setObject(5, address.getState());
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setBoolean(7, address.isActive());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating address: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM addresses WHERE id_address = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting address: " + e.getMessage());
            throw e;
        }
    }

    // MAPPER
    private Address mapAddress(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_address", UUID.class);
        // Tratamento seguro para Integer que pode ser nulo no banco
        Integer number = null;
        int dbNumber = rs.getInt("number");
        if (!rs.wasNull()) {
            number = dbNumber;
        }
        String complement = rs.getString("complement");
        String street = rs.getString("street");
        String city = rs.getString("city");
        String state = rs.getString("state");
        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        boolean active = rs.getBoolean("active");

        return new Address(id, number, complement, street, city, state, updatedAt, active);
    }
}