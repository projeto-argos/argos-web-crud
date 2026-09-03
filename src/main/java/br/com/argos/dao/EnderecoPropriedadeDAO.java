package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.EnderecoPropriedade;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnderecoPropriedadeDAO {

    // CREATE - INSERIR INFORMAÇÕES
    public void inserir(EnderecoPropriedade enderecoPropriedade) throws SQLException {
        String sql = "INSERT INTO endereco_propriedade (numero, observacoes, bairro, estado, cidade, " +
                "id_propriedade, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, enderecoPropriedade.getNumero());
            stmt.setString(2, enderecoPropriedade.getObservacoes());
            stmt.setString(3, enderecoPropriedade.getBairro());
            stmt.setString(4, enderecoPropriedade.getEstado());
            stmt.setString(5, enderecoPropriedade.getCidade());
            stmt.setObject(6, enderecoPropriedade.getIdPropriedade());
            stmt.setBoolean(7, enderecoPropriedade.isAtivo());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir Endereço Propriedade no banco: " + e.getMessage());
            throw e;
        }
    }

    // BUSCAR POR ID (Usando o IdPropriedade)
    public EnderecoPropriedade buscarPorId(UUID idPropriedade) throws SQLException {
        String sql = "SELECT * FROM endereco_propriedade WHERE id_propriedade = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, idPropriedade);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEnderecoPropriedade(rs);
                }
            }
        }
        return null;
    }

    // LISTAR TODOS OS ENDEREÇOSPROPRIEDADE
    public List<EnderecoPropriedade> listarTodos() throws SQLException {
        String sql = "SELECT * FROM endereco_propriedade ORDER BY cidade";
        List<EnderecoPropriedade> enderecoPropriedades = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                enderecoPropriedades.add(mapearEnderecoPropriedade(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar Endereço Propriedade no banco: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return enderecoPropriedades;
    }

    // UPDATE - ATUALIZAR INFORMAÇÕES
    public void atualizar(EnderecoPropriedade enderecoPropriedade) throws SQLException {
        String sql = "UPDATE endereco_propriedade SET numero = ?, observacoes = ?, bairro = ?, " +
                "estado = ?, cidade = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_propriedade = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, enderecoPropriedade.getNumero());
            stmt.setString(2, enderecoPropriedade.getObservacoes());
            stmt.setString(3, enderecoPropriedade.getBairro());
            stmt.setString(4, enderecoPropriedade.getEstado());
            stmt.setString(5, enderecoPropriedade.getCidade());
            stmt.setBoolean(6, enderecoPropriedade.isAtivo());
            stmt.setObject(7, enderecoPropriedade.getIdPropriedade());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar Endereço Propriedade no banco: " + e.getMessage());
            throw e;
        }
    }

    // DELETE - DELETAR INFORMAÇÕES
    public void deletar(UUID idPropriedade) throws SQLException {
        String sql = "DELETE FROM endereco_propriedade WHERE id_propriedade = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, idPropriedade);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar Endereço Propriedade no banco: " + e.getMessage());
            throw e;
        }
    }

    // MAPEAR ENDERECO PROPRIEDADE
    private EnderecoPropriedade mapearEnderecoPropriedade(ResultSet rs) throws SQLException {
        int numero = rs.getInt("numero");
        String observacoes = rs.getString("observacoes");
        String bairro = rs.getString("bairro");
        String estado = rs.getString("estado");
        String cidade = rs.getString("cidade");
        UUID idPropriedade = rs.getObject("id_propriedade", UUID.class);
        boolean ativo = rs.getBoolean("ativo");

        LocalDateTime atualizadoEm = null;
        Timestamp tsAtualizadoEm = rs.getTimestamp("atualizado_em");
        if (tsAtualizadoEm != null) {
            atualizadoEm = tsAtualizadoEm.toLocalDateTime();
        }

        return new EnderecoPropriedade(bairro, observacoes, numero, cidade, estado, atualizadoEm, idPropriedade, ativo);
    }
}