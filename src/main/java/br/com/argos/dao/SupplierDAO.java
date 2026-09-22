package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Supplier;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SupplierDAO {

    // CREATE
    public void insert(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO suppliers (address_id, full_name, cnpj, phone, email, active, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, supplier.getAddressId());
            stmt.setString(2, supplier.getFullName());
            stmt.setString(3, supplier.getCnpj());
            stmt.setString(4, supplier.getPhone());
            stmt.setString(5, supplier.getEmail());
            stmt.setBoolean(6, supplier.isActive());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting supplier: " + e.getMessage());
            throw e;
        }
    }

    // READ
    public Supplier findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM suppliers WHERE id_supplier = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapSupplier(rs);
                }
            }
        }
        return null;
    }

    public List<Supplier> findAll() throws SQLException {
        String sql = "SELECT * FROM suppliers ORDER BY full_name";
        List<Supplier> suppliers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                suppliers.add(mapSupplier(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing suppliers: " + e.getMessage());
            throw e;
        }

        return suppliers;
    }

    // UPDATE
    public void update(Supplier supplier) throws SQLException {
        String sql = "UPDATE suppliers SET address_id = ?, full_name = ?, cnpj = ?, phone = ?, email = ?, active = ?, updated_at = now() " +
                "WHERE id_supplier = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, supplier.getAddressId());
            stmt.setString(2, supplier.getFullName());
            stmt.setString(3, supplier.getCnpj());
            stmt.setString(4, supplier.getPhone());
            stmt.setString(5, supplier.getEmail());
            stmt.setBoolean(6, supplier.isActive());
            stmt.setObject(7, supplier.getIdSupplier());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating supplier: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM suppliers WHERE id_supplier = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
            throw e;
        }
    }

    // MAPPER
    private Supplier mapSupplier(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_supplier", UUID.class);
        UUID addressId = rs.getObject("address_id", UUID.class);
        String fullName = rs.getString("full_name");
        String cnpj = rs.getString("cnpj");
        String phone = rs.getString("phone");
        String email = rs.getString("email");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        boolean active = rs.getBoolean("active");

        return new Supplier(id, addressId, fullName, cnpj, phone, email, updatedAt, active);
    }
}