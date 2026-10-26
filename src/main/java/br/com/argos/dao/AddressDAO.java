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

//    CREATE
    @Override
    public void insert(Address address) {
        String sql = "INSERT INTO ADDRESS " +
                "(street, number, city, state, zip_code, complement, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, address.getStreet());

            if (address.getNumber() != null) {
                stmt.setInt(2, address.getNumber());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setString(3, address.getCity());
            stmt.setString(4, address.getState());
            stmt.setString(5, address.getZipCode());
            stmt.setString(6, address.getComplement());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error inserting address.", e);
        }
    }

//    FIND BY ID
    @Override
    public Address findById(UUID id) {
        String sql = "SELECT * FROM ADDRESS " +
                "WHERE id_address = ? AND active = TRUE";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAddress(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error finding address by ID.", e);
        }
    }

//    FIND ALL
    @Override
    public List<Address> findAll() {
        String sql = "SELECT * FROM ADDRESS " +
                "WHERE active = TRUE ORDER BY street";

        List<Address> addresses = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                addresses.add(mapAddress(rs));
            }

            return addresses;

        } catch (SQLException e) {
            throw new DataAccessException("Error listing addresses.", e);
        }
    }

//    UPDATE
    @Override
    public void update(Address address) {
        String sql = "UPDATE ADDRESS SET " +
                "street = ?, number = ?, city = ?, state = ?, " +
                "zip_code = ?, complement = ?, updated_at = now() " +
                "WHERE id_address = ? AND active = TRUE";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, address.getStreet());

            if (address.getNumber() != null) {
                stmt.setInt(2, address.getNumber());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setString(3, address.getCity());
            stmt.setString(4, address.getState());
            stmt.setString(5, address.getZipCode());
            stmt.setString(6, address.getComplement());
            stmt.setObject(7, address.getIdAddress());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException(
                        "Address not found: " + address.getIdAddress()
                );
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error updating address.", e);
        }
    }

//    SOFT DELETE
    @Override
    public void delete(UUID id) {
        String sql = "UPDATE ADDRESS SET " +
                "active = FALSE, updated_at = now() " +
                "WHERE id_address = ? AND active = TRUE";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Address not found: " + id);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting address.", e);
        }
    }

//    MAPPER
    private Address mapAddress(ResultSet rs) throws SQLException {
        UUID idAddress = rs.getObject("id_address", UUID.class);

        int dbNumber = rs.getInt("number");
        Integer number = rs.wasNull() ? null : dbNumber;

        String street = rs.getString("street");
        String city = rs.getString("city");
        String state = rs.getString("state");
        String zipCode = rs.getString("zip_code");
        String complement = rs.getString("complement");

        Timestamp timestamp = rs.getTimestamp("updated_at");
        LocalDateTime updatedAt = timestamp != null
                ? timestamp.toLocalDateTime()
                : null;

        boolean active = rs.getBoolean("active");

        return new Address(idAddress, number, complement, street, city, state, zipCode, updatedAt, active);
    }
}
