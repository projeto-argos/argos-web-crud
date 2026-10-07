package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.exceptions.DataAccessException;
import br.com.argos.interfaces.GenericDAO;
import br.com.argos.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Classe de acesso a dados (DAO).
 * Acesso ao banco para a tabela User (Usuário).
 * Implementa as operações de CRUD e métodos personalizados.
 */
public class UserDAO implements GenericDAO<User, UUID> {

    // CREATE
    @Override
    public void insert(User user) {
        String sql = "INSERT INTO users (full_name, phone, cpf, email, role, password, birth_date, active, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getCpf());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getPassword());

            if (user.getBirthDate() != null) {
                stmt.setDate(7, Date.valueOf(user.getBirthDate()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            stmt.setBoolean(8, user.isActive());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error inserting user: " + e);
        }
    }

    // READ
    @Override
    public User findById(UUID id) {
        String sql = "SELECT * FROM users WHERE id_user = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error searching for user: " + id, e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY full_name";
        List<User> users = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing users: " + e);
        }

        return users;
    }

    // UPDATE
    @Override
    public void update(User user)  {
        String sql = "UPDATE users SET full_name = ?, phone = ?, cpf = ?, email = ?, role = ?, password = ?, birth_date = ?, active = ?, updated_at = now() " +
                "WHERE id_user = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getCpf());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getRole());
            stmt.setString(6, user.getPassword());

            if (user.getBirthDate() != null) {
                stmt.setDate(7, Date.valueOf(user.getBirthDate()));
            } else {
                stmt.setNull(7, Types.DATE);
            }

            stmt.setBoolean(8, user.isActive());
            stmt.setObject(9, user.getIdUser());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error updating user: " + e);
        }
    }

    // DELETE
    @Override
    public void delete(UUID id){
        String sql = "DELETE FROM users WHERE id_user = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting user: " + id, e);
        }
    }

    // MAPPER
    private User mapUser(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_user", UUID.class);
        String fullName = rs.getString("full_name");
        String phone = rs.getString("phone");
        String cpf = rs.getString("cpf");
        String email = rs.getString("email");
        String role = rs.getString("role");
        String password = rs.getString("password");

        LocalDate birthDate = null;
        Date sqlDate = rs.getDate("birth_date");
        if (sqlDate != null) {
            birthDate = sqlDate.toLocalDate();
        }

        boolean active = rs.getBoolean("active");

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        return new User(id, fullName, cpf, email, phone, role, password, birthDate, updatedAt, active);
    }
}