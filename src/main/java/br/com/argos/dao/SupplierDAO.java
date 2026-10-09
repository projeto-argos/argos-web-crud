package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.exceptions.DataAccessException;
import br.com.argos.interfaces.GenericDAO;
import br.com.argos.model.Supplier;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SupplierDAO implements GenericDAO<Supplier, UUID> {

    @Override
    public void insert(Supplier supplier) {
        String sql = "INSERT INTO suppliers (full_name, cnpj, phone, email, active, id_address, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getFullName());
            stmt.setString(2, supplier.getCnpj());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getEmail());
            stmt.setBoolean(5, supplier.isActive());
            stmt.setObject(6, supplier.getAddressId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting supplier: " + e.getMessage(), e);
        }
    }

    @Override
    public Supplier findById(UUID id) {
        String sql = "SELECT * FROM suppliers WHERE id_supplier = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapSupplier(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DataAccessException("Error searching for supplier: " + id, e);
        }
    }

    @Override
    public List<Supplier> findAll() {
        String sql = "SELECT * FROM suppliers ORDER BY full_name";
        List<Supplier> suppliers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                suppliers.add(mapSupplier(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing suppliers: " + e.getMessage(), e);
        }

        return suppliers;
    }

    @Override
    public void update(Supplier supplier) {
        String sql = "UPDATE suppliers SET full_name = ?, cnpj = ?, phone = ?, email = ?, active = ?, id_address = ?, updated_at = now() " +
                "WHERE id_supplier = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getFullName());
            stmt.setString(2, supplier.getCnpj());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getEmail());
            stmt.setBoolean(5, supplier.isActive());
            stmt.setObject(6, supplier.getAddressId());
            stmt.setObject(7, supplier.getIdSupplier());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating supplier: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM suppliers WHERE id_supplier = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting supplier: " + id, e);
        }
    }

    private Supplier mapSupplier(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_supplier", UUID.class);
        UUID addressId = rs.getObject("id_address", UUID.class);
        String fullName = rs.getString("full_name");
        String cnpj = rs.getString("cnpj");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        boolean active = rs.getBoolean("active");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Supplier(id, addressId, fullName, cnpj, phone, email, updatedAt, active);
    }
}