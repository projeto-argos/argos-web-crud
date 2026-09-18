package br.com.argos.dao;

import br.com.argos.model.User;
import br.com.argos.connection.ConnectionFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDAO {

    public void insert(User user) throws SQLException {
        String sql = "INSERT INTO usuario (nome_completo, telefone, email, cpf, cargo, data_nascimento, senha, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getCpf());
            stmt.setString(5, user.getRole());

            if (user.getBirthDate() != null) {
                stmt.setDate(6, java.sql.Date.valueOf(user.getBirthDate()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }

            stmt.setString(7, user.getPassword());
            stmt.setBoolean(8, user.isActive());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting user: " + e.getMessage());
            throw e;
        }
    }

    public User findById(UUID id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";

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
        String sql = "SELECT * FROM usuario ORDER BY nome_completo";
        List<User> users = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listing users: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return users;
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE usuario SET nome_completo = ?, telefone = ?, email = ?, cpf = ?, cargo = ?, data_nascimento = ?, senha = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getPhone());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getCpf());
            stmt.setString(5, user.getRole());

            if (user.getBirthDate() != null) {
                stmt.setDate(6, java.sql.Date.valueOf(user.getBirthDate()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }

            stmt.setString(7, user.getPassword());
            stmt.setBoolean(8, user.isActive());
            stmt.setObject(9, user.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            throw e;
        }
    }

    public void delete(UUID id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            throw e;
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_usuario", UUID.class);
        String phone = rs.getString("telefone");
        String fullName = rs.getString("nome_completo");
        String cpf = rs.getString("cpf");
        String email = rs.getString("email");
        String role = rs.getString("cargo");
        String password = rs.getString("senha");

        LocalDate birthDate = null;
        java.sql.Date sqlDate = rs.getDate("data_nascimento");
        if (sqlDate != null) {
            birthDate = sqlDate.toLocalDate();
        }

        LocalDateTime updatedAt = null;
        Timestamp tsUpdatedAt = rs.getTimestamp("atualizado_em");
        if (tsUpdatedAt != null) {
            updatedAt = tsUpdatedAt.toLocalDateTime();
        }

        boolean active = rs.getBoolean("ativo");

        return new User(id, phone, fullName, cpf, email, role, birthDate, password, updatedAt, active);
    }
}