package br.com.argos.dao;

//IMPORTS

import br.com.argos.model.Fornecedor;
import br.com.argos.connection.ConnectionFactory;
import java.util.UUID;
import java.time.LocalDateTime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {


//          CRUD

//    INSERT
    public void inserir(Fornecedor fornecedor) throws SQLException {
        String sql = "INSERT INTO fornecedor (cnpj, nome, telefone, email, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getCnpj());
            stmt.setString(2, fornecedor.getNome());
            stmt.setString(3, fornecedor.getTelefone());
            stmt.setString(4, fornecedor.getEmail());
            stmt.setBoolean(5, fornecedor.isAtivo());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir fornecedor no banco: " + e.getMessage());
            throw e;
        }
    }


//    READ
    public Fornecedor buscarPorId(UUID id) throws SQLException {
        String sql = "SELECT * FROM fornecedor WHERE id_fornecedor = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearFornecedor(rs);
                }
            }
        }
        return null;
    }

    public List<Fornecedor> listarTodos() throws SQLException {
        String sql = "SELECT * FROM fornecedor ORDER BY nome";
        List<Fornecedor> fornecedores = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                fornecedores.add(mapearFornecedor(rs));
            }

        } catch (SQLException e) {
        System.err.println("Erro ao listar fornecedores: " + e.getMessage());
        e.printStackTrace(); // imprime a exception inteira
        throw e;
        }

        return fornecedores;
    }


//    UPDATE
    public void atualizar(Fornecedor fornecedor) throws SQLException {
        String sql = "UPDATE fornecedor SET cnpj = ?, nome = ?, telefone = ?, email = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_fornecedor = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getCnpj());
            stmt.setString(2, fornecedor.getNome());
            stmt.setString(3, fornecedor.getTelefone());
            stmt.setString(4, fornecedor.getEmail());
            stmt.setBoolean(5, fornecedor.isAtivo());
            stmt.setObject(6, fornecedor.getIdFornecedor());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar fornecedor no banco: " + e.getMessage());
            throw e;
        }
    }

//    DELETE
    public void deletar(UUID id) throws SQLException {
        String sql = "DELETE FROM fornecedor WHERE id_fornecedor = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar fornecedor no banco: " + e.getMessage());
            throw e;
        }
    }

    private Fornecedor mapearFornecedor(ResultSet rs) throws SQLException {
        UUID idFornecedor = rs.getObject("id_fornecedor", UUID.class);
        String cnpj = rs.getString("cnpj");
        String nome = rs.getString("nome");
        String telefone = rs.getString("telefone");
        String email = rs.getString("email");

        LocalDateTime atualizadoEm = null;
        Timestamp tsAtualizadoEm = rs.getTimestamp("atualizado_em");
        if (tsAtualizadoEm != null) {
            atualizadoEm = tsAtualizadoEm.toLocalDateTime();
        }

        boolean ativo = rs.getBoolean("ativo");

//         A ordem precisa bater exatamente com a ordem do construtor no Model
        return new Fornecedor(idFornecedor, cnpj, nome, telefone, email,
                atualizadoEm, ativo);
    }
}