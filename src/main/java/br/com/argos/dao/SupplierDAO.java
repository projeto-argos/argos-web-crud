package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Supplier;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SupplierDAO {

    // INSERT
    public void insert(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO fornecedor (cnpj, nome_completo, telefone, email, id_endereco, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getCnpj());
            stmt.setString(2, supplier.getFullName());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getEmail());
            stmt.setObject(5, supplier.getAddressId());
            stmt.setBoolean(6, supplier.isActive());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting supplier: " + e.getMessage());
            throw e;
        }
    }

    // FIND BY ID
    public Supplier findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM fornecedor WHERE id_fornecedor = ?";

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

    // FIND ALL
    public List<Supplier> findAll() throws SQLException {
        String sql = "SELECT * FROM fornecedor ORDER BY nome_completo";
        List<Supplier> suppliers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                suppliers.add(mapSupplier(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error listing suppliers: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return suppliers;
    }

    // UPDATE
    public void update(Supplier supplier) throws SQLException {
        String sql = "UPDATE fornecedor SET cnpj = ?, nome_completo = ?, telefone = ?, email = ?, id_endereco = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_fornecedor = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, supplier.getCnpj());
            stmt.setString(2, supplier.getFullName());
            stmt.setString(3, supplier.getPhone());
            stmt.setString(4, supplier.getEmail());
            stmt.setObject(5, supplier.getAddressId());
            stmt.setBoolean(6, supplier.isActive());
            stmt.setObject(7, supplier.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating supplier: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM fornecedor WHERE id_fornecedor = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
            throw e;
        }
    }

    // MAP RESULT SET
    private Supplier mapSupplier(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_fornecedor", UUID.class);
        String cnpj = rs.getString("cnpj");
        String fullName = rs.getString("nome_completo");
        String phone = rs.getString("telefone");
        String email = rs.getString("email");
        UUID addressId = rs.getObject("id_endereco", UUID.class);

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("atualizado_em");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        boolean active = rs.getBoolean("ativo");

        return new Supplier(id, cnpj, fullName, phone, email, addressId, updatedAt, active);
    }
}