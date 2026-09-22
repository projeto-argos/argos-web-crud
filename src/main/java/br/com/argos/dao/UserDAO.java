package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDAO {

    // CREATE
    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO users (full_name, phone, email, cpf, role, birth_date, password, active, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getCpf());
            stmt.setString(5, user.getRole());

            if (user.getBirthDate() != null) {
                stmt.setDate(6, Date.valueOf(user.getBirthDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            stmt.setString(7, user.getPassword());
            stmt.setBoolean(8, user.isActive());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
            throw e;
        }
    }

    // READ
    public User findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id_user = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }
        return null;
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM users ORDER BY full_name";
        List<User> users = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing users: " + e.getMessage());
            throw e;
        }

        return users;
    }

    // UPDATE
    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET full_name = ?, phone = ?, email = ?, cpf = ?, role = ?, birth_date = ?, password = ?, active = ?, updated_at = now() " +
                "WHERE id_user = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getCpf());
            stmt.setString(5, user.getRole());

            if (user.getBirthDate() != null) {
                stmt.setDate(6, Date.valueOf(user.getBirthDate()));
            } else {
                stmt.setNull(6, Types.DATE);
            }

            stmt.setString(7, user.getPassword());
            stmt.setBoolean(8, user.isActive());
            stmt.setObject(9, user.getIdUser());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            throw e;
        }
    }

    // DELETE
    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM users WHERE id_user = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            throw e;
        }
    }

    // MAPPER
    private User mapUser(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_user", UUID.class);
        String fullName = rs.getString("full_name");
        String cpf = rs.getString("cpf");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        String role = rs.getString("role");
        String password = rs.getString("password");

        LocalDate birthDate = null;
        Date sqlDate = rs.getDate("birth_date");
        if (sqlDate != null) {
            birthDate = sqlDate.toLocalDate();
        }

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        boolean active = rs.getBoolean("active");

        return new User(id, fullName, cpf, email, phone, role, password, birthDate, updatedAt, active);
    }
}