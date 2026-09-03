package br.com.argos.dao;

//IMPORTS

import br.com.argos.model.Admin;
import br.com.argos.connection.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public void inserir(Admin admin) throws SQLException {
        String sql = "INSERT INTO admin (cpf, nome, telefone, email, ativo) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getCpf());
            stmt.setString(2, admin.getNome());
            stmt.setString(3, admin.getTelefone());
            stmt.setString(4, admin.getEmail());
            stmt.setBoolean(5, admin.isAtivo());

            stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Erro ao inserir admin no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }
    }

    public Admin buscarPorId(String cpf) throws SQLException {
        String sql = "SELECT * FROM admin WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearAdmin(rs);
                }
            }
        }
        return null;
    }

    public List<Admin> listarTodos() throws SQLException {
        String sql = "SELECT * FROM admin ORDER BY nome";
        List<Admin> administradores = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                administradores.add(mapearAdmin(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar admin: " + e.getMessage());
            e.printStackTrace(); // imprime a exception inteira
            throw e;
        }

        return administradores;
    }

    public void atualizar(Admin admin) throws SQLException {
        String sql = "UPDATE admin SET nome = ?, telefone = ?, email = ?, ativo = ? " +
                "WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getNome());
            stmt.setString(2, admin.getTelefone());
            stmt.setString(3, admin.getEmail());
            stmt.setBoolean(4, admin.isAtivo());
            stmt.setString(5, admin.getCpf());

            stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Erro ao atualizar admin no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }
    }

    public void deletar(String cpf) throws SQLException {
        String sql = "DELETE FROM admin WHERE cpf = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar admin no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }
    }

    private Admin mapearAdmin(ResultSet rs) throws SQLException {
        String cpf = rs.getString("cpf");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String telefone = rs.getString("telefone");
        boolean ativo = rs.getBoolean("ativo");

        // Lembrar --> A ordem precisa bater exatamente com a ordem do construtor no Model
        return new Admin(cpf, nome, email, telefone, ativo);
    }
}