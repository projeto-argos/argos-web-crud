package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.exceptions.DataAccessException;
import br.com.argos.interfaces.GenericDAO;
import br.com.argos.model.Admin;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminDAO implements GenericDAO<Admin, UUID> {

    // CREATE
    @Override
    public void insert(Admin admin)  {
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
            throw new DataAccessException("Error inserting admin: ", e);
        }
    }

    // READ
    @Override
    public Admin findById(UUID id) {
        String sql = "SELECT * FROM admins WHERE id_admin = ? and active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAdmin(rs, false);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error searching for admin " + id, e);
        }

    }

//    FIND ADMIN BY EMAIL
    @Override
    public Admin findByEmail(String email) {
        String sql = "SELECT * FROM admins WHERE email = ?";

        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapAdmin(rs, true);
                }
            }
            return null;

        } catch (SQLException sqle){
            throw new DataAccessException("Error searching for admin by email" + email, sqle);
        }
    }

    @Override
    public List<Admin> findAll() {
        String sql = "SELECT * FROM admins WHERE active = true ORDER BY full_name";
        List<Admin> admins = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                admins.add(mapAdmin(rs, false));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing admins: ", e);
        }

        return admins;
    }

    // UPDATE
    @Override
    public void update(Admin admin) {
        String sql = "UPDATE admins SET full_name = ?, cpf = ?, phone = ?, email = ?, updated_at = now() " +
                "WHERE id_admin = ? and active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getFullName());
            stmt.setString(2, admin.getCpf());
            stmt.setString(3, admin.getPhone());
            stmt.setString(4, admin.getEmail());
            stmt.setObject(5, admin.getIdAdmin());

           int rowsAffected = stmt.executeUpdate();

           if (rowsAffected == 0) {
               throw new DataAccessException("Admin not found " + admin);
           }

        } catch (SQLException e) {
            throw new DataAccessException("Error updating admin: ", e);

        }
    }

    //    UPDATE PASSWORD
    @Override
    public void updatePassword(UUID id, String password) {
        String sql = "UPDATE admins SET password = ?, updated_at = now() WHERE id_admin = ?";

        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, password);
            stmt.setObject(2, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("User not found " + id);
            }

        } catch (SQLException sqle) {
            throw new DataAccessException("Error updating admin password: ", sqle);
        }
    }

    // SOFT DELETE
    @Override
    public void delete(UUID id) {
        String sql = "UPDATE admins SET active = FALSE, updated_at = now() WHERE id_admin = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Admin not found " + id);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error deactivating admin: " + id, e);

        }
    }

    // MAPPER
    private Admin mapAdmin(ResultSet rs, boolean hasPassword) throws SQLException {
        UUID id = rs.getObject("id_admin", UUID.class);
        String fullName = rs.getString("full_name");
        String cpf = rs.getString("cpf");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        String password = hasPassword ? rs.getString("password") : null;
        boolean active = rs.getBoolean("active");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new Admin(id, fullName, cpf, email, phone, password, updatedAt, active);
    }
}