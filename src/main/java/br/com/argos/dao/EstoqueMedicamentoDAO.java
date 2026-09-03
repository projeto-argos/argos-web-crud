package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.EstoqueMedicamento;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EstoqueMedicamentoDAO {

    // CREATE - INSERIR INFORMAÇÕES
    public void inserir(EstoqueMedicamento estoqueMedicamento) throws SQLException {
        String sql = "INSERT INTO estoque_medicamento (data_validade, status, local_armazenamento, data_fabricacao, " +
                "lote_fabricacao, data_entrega, qtd_disponivel, id_medicamento, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (estoqueMedicamento.getDataValidade() != null) {
                stmt.setDate(1, java.sql.Date.valueOf(estoqueMedicamento.getDataValidade()));
            } else {
                stmt.setNull(1, java.sql.Types.DATE);
            }

            stmt.setString(2, estoqueMedicamento.getStatus());
            stmt.setString(3, estoqueMedicamento.getLocalArmazenamento());

            if (estoqueMedicamento.getDataFabricacao() != null) {
                stmt.setDate(4, java.sql.Date.valueOf(estoqueMedicamento.getDataFabricacao()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            stmt.setString(5, estoqueMedicamento.getLoteFabricacao());

            if (estoqueMedicamento.getDataEntrega() != null) {
                stmt.setDate(6, java.sql.Date.valueOf(estoqueMedicamento.getDataEntrega()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }

            stmt.setInt(7, estoqueMedicamento.getQtdDisponivel());
            stmt.setObject(8, estoqueMedicamento.getIdMedicamento());
            stmt.setBoolean(9, estoqueMedicamento.getAtivo());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir Estoque Medicamento no banco: " + e.getMessage());
            throw e;
        }
    }

    // BUSCAR POR ID
    public EstoqueMedicamento buscarPorId(UUID idEstoqueMedicamento) throws SQLException {
        String sql = "SELECT * FROM estoque_medicamento WHERE id_estoque_medicamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, idEstoqueMedicamento);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEstoqueMedicamento(rs);
                }
            }
        }
        return null;
    }

    // LISTAR TODOS OS ENDEREÇOSPROPRIEDADE
    public List<EstoqueMedicamento> listarTodos() throws SQLException {
        String sql = "SELECT * FROM estoque_medicamento ORDER BY data_validade";
        List<EstoqueMedicamento> estoques = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                estoques.add(mapearEstoqueMedicamento(rs));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar Estoque Medicamento no banco: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return estoques;
    }

    // UPDATE - ATUALIZAR INFORMAÇÕES
    public void atualizar(EstoqueMedicamento estoqueMedicamento) throws SQLException {
        String sql = "UPDATE estoque_medicamento SET data_validade = ?, status = ?, local_armazenamento = ?, " +
                "data_fabricacao = ?, lote_fabricacao = ?, data_entrega = ?, qtd_disponivel = ?, " +
                "id_medicamento = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_estoque_medicamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (estoqueMedicamento.getDataValidade() != null) {
                stmt.setDate(1, java.sql.Date.valueOf(estoqueMedicamento.getDataValidade()));
            } else {
                stmt.setNull(1, java.sql.Types.DATE);
            }

            stmt.setString(2, estoqueMedicamento.getStatus());
            stmt.setString(3, estoqueMedicamento.getLocalArmazenamento());

            if (estoqueMedicamento.getDataFabricacao() != null) {
                stmt.setDate(4, java.sql.Date.valueOf(estoqueMedicamento.getDataFabricacao()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            stmt.setString(5, estoqueMedicamento.getLoteFabricacao());

            if (estoqueMedicamento.getDataEntrega() != null) {
                stmt.setDate(6, java.sql.Date.valueOf(estoqueMedicamento.getDataEntrega()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }

            stmt.setInt(7, estoqueMedicamento.getQtdDisponivel());
            stmt.setObject(8, estoqueMedicamento.getIdMedicamento());
            stmt.setBoolean(9, estoqueMedicamento.getAtivo());
            stmt.setObject(10, estoqueMedicamento.getIdEstoqueMedicamento());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar Estoque Medicamento no banco: " + e.getMessage());
            throw e;
        }
    }

    // DELETE - DELETAR INFORMAÇÕES
    public void deletar(UUID idEstoqueMedicamento) throws SQLException {
        String sql = "DELETE FROM estoque_medicamento WHERE id_estoque_medicamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, idEstoqueMedicamento);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar Estoque Medicamento no banco: " + e.getMessage());
            throw e;
        }
    }

    // MAPEAR ESTOQUE MEDICAMENTOS
    private EstoqueMedicamento mapearEstoqueMedicamento(ResultSet rs) throws SQLException {
        UUID idEstoqueMedicamento = rs.getObject("id_estoque_medicamento", UUID.class);
        String status = rs.getString("status");
        String localArmazenamento = rs.getString("local_armazenamento");
        String loteFabricacao = rs.getString("lote_fabricacao");
        int qtdDisponivel = rs.getInt("qtd_disponivel");
        UUID idMedicamento = rs.getObject("id_medicamento", UUID.class);
        boolean ativo = rs.getBoolean("ativo");

        LocalDate dataValidade = null;
        java.sql.Date sqlDataValidade = rs.getDate("data_validade");
        if (sqlDataValidade != null) {
            dataValidade = sqlDataValidade.toLocalDate();
        }

        LocalDate dataFabricacao = null;
        java.sql.Date sqlDataFabricacao = rs.getDate("data_fabricacao");
        if (sqlDataFabricacao != null) {
            dataFabricacao = sqlDataFabricacao.toLocalDate();
        }

        LocalDate dataEntrega = null;
        java.sql.Date sqlDataEntrega = rs.getDate("data_entrega");
        if (sqlDataEntrega != null) {
            dataEntrega = sqlDataEntrega.toLocalDate();
        }

        LocalDateTime atualizadoEm = null;
        Timestamp tsAtualizadoEm = rs.getTimestamp("atualizado_em");
        if (tsAtualizadoEm != null) {
            atualizadoEm = tsAtualizadoEm.toLocalDateTime();
        }

        return new EstoqueMedicamento(idEstoqueMedicamento, dataValidade, status, localArmazenamento,
                dataFabricacao, loteFabricacao, dataEntrega, qtdDisponivel, atualizadoEm, idMedicamento, ativo);
    }
}