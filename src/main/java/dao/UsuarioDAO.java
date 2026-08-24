package dao;

//IMPORTS

import model.Usuario;
import br.com.argos.connection.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UsuarioDAO {

    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (cnpj, nome, telefone, email, cpf, cargo, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getCnpj());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getCpf());
            stmt.setString(6, usuario.getCargo());
            stmt.setBoolean(7, usuario.isAtivo());

            stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Erro ao inserir usuário no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }
    }

    public Usuario buscarPorId(UUID id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }
        return null;
    }

    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT * FROM usuario ORDER BY nome";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }
        return usuarios;
    }

    public void atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET cnpj = ?, nome = ?, telefone = ?, email = ?, cpf = ?, cargo = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getCnpj());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getCpf());
            stmt.setString(6, usuario.getCargo());
            stmt.setBoolean(7, usuario.isAtivo());
            stmt.setObject(8, usuario.getIdUsuario());

            stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Erro ao atualizar usuário no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }
    }

    public void deletar(UUID id) throws SQLException {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Erro ao deletar usuário no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getObject("id_usuario", UUID.class));
        u.setCnpj(rs.getString("cnpj"));
        u.setNome(rs.getString("nome"));
        u.setTelefone(rs.getString("telefone"));
        u.setEmail(rs.getString("email"));
        u.setCpf(rs.getString("cpf"));
        u.setCargo(rs.getString("cargo"));
        u.setAtivo(rs.getBoolean("ativo"));
        Timestamp timestamp = rs.getTimestamp("atualizado_em");
        if (timestamp != null){
            u.setAtualizadoEm(timestamp.toLocalDateTime());
        }

        return u;
    }
}