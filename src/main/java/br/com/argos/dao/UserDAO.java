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
public class UserDAO implements GenericDAO<User,UUID> {

    // CREATE
    @Override
    public void insert(User user) {
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
            throw new DataAccessException("Error inserting user: ", e);
        }
    }

    // READ
    @Override
    public User findById(UUID id) {
        String sql = "SELECT * FROM users WHERE id_user = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs, false);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Error searching for user: " + id, e);
        }

    }

    //    FIND USER BY EMAIL
    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs, true);
                }
            }
            return null;

        } catch (SQLException sqle){
            throw new DataAccessException("Error searching for user by email: " + email, sqle);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users WHERE active = true ORDER BY full_name";
        List<User> users = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapUser(rs, false));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error listing users: ", e);
        }

        return users;
    }

    // UPDATE
    @Override
    public void update(User user)  {
        String sql = "UPDATE users SET full_name = ?, phone = ?, email = ?, cpf = ?, role = ?, birth_date = ?, updated_at = now() " +
                "WHERE id_user = ? AND active = true";

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

            stmt.setObject(7, user.getIdUser());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("User not found " + user.getIdUser());
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error updating user: ", e);
        }
    }

//    UPDATE PASSWORD
    @Override
    public void updatePassword(UUID id, String password) {
        String sql = "UPDATE users SET password = ?, updated_at = now() WHERE id_user = ? AND active = true";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, password);
            stmt.setObject(2, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("User not found " + id);
            }

        } catch (SQLException sqle) {
            throw new DataAccessException("Error updating user password: ", sqle);
        }
    }

    // DELETE
    @Override
    public void delete(UUID id){
        String sql = "UPDATE users SET active = FALSE, updated_at = now() WHERE id_user = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("User not found " + id);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error deactivating user: " + id, e);
        }
    }

    // MAPPER
    private User mapUser(ResultSet rs, boolean hasPassword) throws SQLException {
        UUID id = rs.getObject("id_user", UUID.class);
        String fullName = rs.getString("full_name");
        String cpf = rs.getString("cpf");
        String email = rs.getString("email");
        String phone = rs.getString("phone");
        String role = rs.getString("role");
        String password = hasPassword ? rs.getString("password") : null;

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