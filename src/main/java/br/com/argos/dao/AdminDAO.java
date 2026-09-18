package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Admin;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminDAO {

    // INSERT
    public void insert(Admin admin) throws SQLException {
        String sql = "INSERT INTO admin (nome_completo, cpf, telefone, email, senha, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getFullName());
            stmt.setString(2, admin.getCpf());
            stmt.setString(3, admin.getPhone());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getPassword());
            stmt.setBoolean(6, admin.isActive());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting admin: " + e.getMessage());
            throw e;
        }
    }

    // FIND BY ID
    public Admin findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM admin WHERE id_admin = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAdmin(rs);
                }
            }
        }
        return null;
    }

    // FIND ALL
    public List<Admin> findAll() throws SQLException {
        String sql = "SELECT * FROM admin ORDER BY nome_completo";
        List<Admin> admins = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                admins.add(mapAdmin(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing admins: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return admins;
    }

    // UPDATE
    public void update(Admin admin) throws SQLException {
        String sql = "UPDATE admin SET nome_completo = ?, telefone = ?, email = ?, senha = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_admin = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getFullName());
            stmt.setString(2, admin.getPhone());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getPassword());
            stmt.setBoolean(5, admin.isActive());
            stmt.setObject(6, admin.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating admin: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM admin WHERE id_admin = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting admin: " + e.getMessage());
            throw e;
        }
    }

    // MAP RESULT SET
    private Admin mapAdmin(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_admin", UUID.class);
        String cpf = rs.getString("cpf");
        String fullName = rs.getString("nome_completo");
        String email = rs.getString("email");
        String phone = rs.getString("telefone");
        String password = rs.getString("senha");
        boolean active = rs.getBoolean("ativo");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("atualizado_em");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Admin(id, cpf, fullName, email, phone, password, active, updatedAt);
    }
}