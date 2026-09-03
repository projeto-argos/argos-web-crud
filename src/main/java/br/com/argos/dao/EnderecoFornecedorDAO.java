package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.EnderecoFornecedor;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnderecoFornecedorDAO {

    // CREATE - INSERIR INFORMAÇÕES
    public void inserir(EnderecoFornecedor enderecoFornecedor) throws SQLException {
        String sql = "INSERT INTO endereco_fornecedor (numero, observacoes, bairro, estado, cidade, " +
                "id_fornecedor, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, enderecoFornecedor.getNumero());
            stmt.setString(2, enderecoFornecedor.getObservacoes());
            stmt.setString(3, enderecoFornecedor.getBairro());
            stmt.setString(4, enderecoFornecedor.getEstado());
            stmt.setString(5, enderecoFornecedor.getCidade());
            stmt.setObject(6, enderecoFornecedor.getIdFornecedor());
            stmt.setBoolean(7, enderecoFornecedor.isAtivo());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir Endereço Fornecedor no banco: " + e.getMessage());
            throw e;
        }
    }

    // BUSCAR POR ID (Usando o idFornecedor)
    public EnderecoFornecedor buscarPorId(UUID idFornecedor) throws SQLException {
        String sql = "SELECT * FROM endereco_fornecedor WHERE id_fornecedor = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, idFornecedor);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEnderecoFornecedor(rs);
                }
            }
        }
        return null;
    }

    // LISTAR TODOS OS ENDEREÇOSFORNECEDOR
    public List<EnderecoFornecedor> listarTodos() throws SQLException {
        String sql = "SELECT * FROM endereco_fornecedor ORDER BY cidade";
        List<EnderecoFornecedor> enderecosFornecedores = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                enderecosFornecedores.add(mapearEnderecoFornecedor(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar Endereço Fornecedor no banco: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return enderecosFornecedores;
    }

    // UPDATE - ATUALIZAR INFORMAÇÕES
    public void atualizar(EnderecoFornecedor enderecoFornecedor) throws SQLException {
        String sql = "UPDATE endereco_fornecedor SET numero = ?, observacoes = ?, bairro = ?, " +
                "estado = ?, cidade = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_fornecedor = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, enderecoFornecedor.getNumero());
            stmt.setString(2, enderecoFornecedor.getObservacoes());
            stmt.setString(3, enderecoFornecedor.getBairro());
            stmt.setString(4, enderecoFornecedor.getEstado());
            stmt.setString(5, enderecoFornecedor.getCidade());
            stmt.setBoolean(6, enderecoFornecedor.isAtivo());
            stmt.setObject(7, enderecoFornecedor.getIdFornecedor());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar Endereço Fornecedor no banco: " + e.getMessage());
            throw e;
        }
    }

    // DELETE - DELETAR INFORMAÇÕES
    public void deletar(UUID idFornecedor) throws SQLException {
        String sql = "DELETE FROM endereco_fornecedor WHERE id_fornecedor = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, idFornecedor);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar Endereço Fornecedor no banco: " + e.getMessage());
            throw e;
        }
    }

    // MAPEAR ENDERECO FORNECEDOR
    private EnderecoFornecedor mapearEnderecoFornecedor(ResultSet rs) throws SQLException {
        int numero = rs.getInt("numero");
        String observacoes = rs.getString("observacoes");
        String bairro = rs.getString("bairro");
        String estado = rs.getString("estado");
        String cidade = rs.getString("cidade");
        UUID idFornecedor = rs.getObject("id_fornecedor", UUID.class);
        boolean ativo = rs.getBoolean("ativo");

        LocalDateTime atualizadoEm = null;
        Timestamp tsAtualizadoEm = rs.getTimestamp("atualizado_em");
        if (tsAtualizadoEm != null) {
            atualizadoEm = tsAtualizadoEm.toLocalDateTime();
        }

        // ordem baseada no construtor do model EnderecoFornecedor
        return new EnderecoFornecedor(numero, observacoes, atualizadoEm, bairro, estado, cidade, idFornecedor, ativo);
    }
}