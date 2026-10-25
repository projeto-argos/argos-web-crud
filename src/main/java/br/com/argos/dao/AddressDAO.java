
package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.exceptions.DataAccessException;
import br.com.argos.interfaces.GenericDAO;
import br.com.argos.model.Address;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddressDAO implements GenericDAO<Address, UUID> {

    // CREATE
    @Override
    public void insert(Address address) {
        String sql = "INSERT INTO ADDRESS " +
                "(number, complement, street, city, state, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, now())";

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

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error inserting address: ", e);
        }
    }

    // READ BY ID
    @Override
    public Address findById(UUID id) {
        String sql = "SELECT * FROM ADDRESS " +
                "WHERE id_address = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAddress(rs);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error searching for address: " + id, e);
        }
    }

    // READ ALL ACTIVE ADDRESSES
    @Override
    public List<Address> findAll() {
        String sql = "SELECT * FROM ADDRESS " +
                "WHERE active = true ORDER BY street";

        List<Address> addresses = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                addresses.add(mapAddress(rs));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error listing addresses: ", e);
        }

        return addresses;
    }

    // UPDATE
    @Override
    public void update(Address address) {
        String sql = "UPDATE ADDRESS SET number = ?, complement = ?, " +
                "street = ?, city = ?, state = ?, updated_at = now() " +
                "WHERE id_address = ? AND active = true";

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
            stmt.setObject(6, address.getIdAddress());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException(
                        "Address not found: " + address.getIdAddress());
            }

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error updating address: " + address.getIdAddress(), e);
        }
    }

    // SOFT DELETE
    @Override
    public void delete(UUID id) {
        String sql = "UPDATE ADDRESS SET active = FALSE, updated_at = now() " +
                "WHERE id_address = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Address not found: " + id);
            }

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error deleting address: " + id, e);
        }
    }

    // MAPPER
    private Address mapAddress(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_address", UUID.class);

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
