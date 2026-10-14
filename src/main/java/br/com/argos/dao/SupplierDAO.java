package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.exceptions.DataAccessException;
import br.com.argos.interfaces.GenericDAO;
import br.com.argos.interfaces.ISupplierDAO;
import br.com.argos.model.Supplier;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SupplierDAO implements GenericDAO<Supplier, UUID>, ISupplierDAO {

    @Override
    public void insert(Supplier supplier) {
        String sql = "INSERT INTO SUPPLIER (full_name, cnpj, phone, email, id_address, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getFullName());
            stmt.setString(2, supplier.getCnpj());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getEmail());

            if (supplier.getAddressId() != null) {
                stmt.setObject(5, supplier.getAddressId());
            } else {
                stmt.setNull(5, Types.OTHER);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error inserting supplier: ", e);
        }
    }

    @Override
    public Supplier findById(UUID id) {
        String sql = "SELECT * FROM SUPPLIER WHERE id_supplier = ? AND active = true";

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
        String sql = "SELECT * FROM SUPPLIER WHERE active = true ORDER BY full_name";
        List<Supplier> suppliers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                suppliers.add(mapSupplier(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing suppliers: ", e);
        }

        return suppliers;
    }

    @Override
    public List<Supplier> findByName(String name) {
        String sql = "SELECT * FROM SUPPLIER WHERE full_name ILIKE ? AND active = true ORDER BY full_name";
        List<Supplier> suppliers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    suppliers.add(mapSupplier(rs));
                }
            }
            return suppliers;

        } catch (SQLException e) {
            throw new DataAccessException("Error searching for supplier by name: " + name, e);
        }
    }

    @Override
    public Supplier findByEmail(String email) {
        String sql = "SELECT * FROM SUPPLIER WHERE email = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapSupplier(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error searching for supplier by email: " + email, e);
        }
    }

    @Override
    public void update(Supplier supplier) {
        String sql = "UPDATE SUPPLIER SET full_name = ?, cnpj = ?, phone = ?, email = ?, id_address = ?, " +
                "updated_at = now() WHERE id_supplier = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getFullName());
            stmt.setString(2, supplier.getCnpj());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getEmail());

            if (supplier.getAddressId() != null) {
                stmt.setObject(5, supplier.getAddressId());
            } else {
                stmt.setNull(5, Types.OTHER);
            }

            stmt.setObject(6, supplier.getIdSupplier());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Supplier not found " + supplier.getIdSupplier());
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error updating supplier: ", e);
        }
    }

    // SOFT DELETE
    @Override
    public void delete(UUID id) {
        String sql = "UPDATE SUPPLIER SET active = FALSE, updated_at = now() WHERE id_supplier = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Supplier not found " + id);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting supplier: " + id, e);
        }
    }

    // SOFT DELETE por CNPJ
    @Override
    public void deleteByCnpj(String cnpj) {
        String sql = "UPDATE SUPPLIER SET active = FALSE, updated_at = now() WHERE cnpj = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cnpj);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Supplier not found with CNPJ " + cnpj);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error deleting supplier by CNPJ: " + cnpj, e);
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