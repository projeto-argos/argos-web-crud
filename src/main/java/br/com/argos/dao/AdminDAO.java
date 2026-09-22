package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Admin;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminDAO {

    // CREATE
    public void insert(Admin admin) throws SQLException {
        String sql = "INSERT INTO admins (full_name, cpf, phone, email, password, active, updated_at) " +
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

    // READ
    public Admin findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM admins WHERE id_admin = ?";

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

    public List<Admin> findAll() throws SQLException {
        String sql = "SELECT * FROM admins ORDER BY full_name";
        List<Admin> admins = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                admins.add(mapAdmin(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing admins: " + e.getMessage());
            throw e;
        }

        return admins;
    }

    // UPDATE
    public void update(Admin admin) throws SQLException {
        String sql = "UPDATE admins SET full_name = ?, cpf = ?, phone = ?, email = ?, password = ?, active = ?, updated_at = now() " +
                "WHERE id_admin = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getFullName());
            stmt.setString(2, admin.getCpf());
            stmt.setString(3, admin.getPhone());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getPassword());
            stmt.setBoolean(6, admin.isActive());
            stmt.setObject(7, admin.getIdAdmin());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating admin: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM admins WHERE id_admin = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting admin: " + e.getMessage());
            throw e;
        }
    }

    // MAPPER
    private Admin mapAdmin(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_admin", UUID.class);
        String fullName = rs.getString("full_name");
        String cpf = rs.getString("cpf");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        String password = rs.getString("password");
        boolean active = rs.getBoolean("active");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Admin(id, fullName, cpf, email, phone, password, updatedAt, active);
    }
}