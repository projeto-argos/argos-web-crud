package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Aplicacao;
import java.util.UUID;
import java.time.LocalDateTime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AplicacaoDAO {

    public void inserir(Aplicacao aplicacao) throws SQLException {
        String sql = "INSERT INTO aplicacao (dose_aplicada, observacoes, membro_aplicacao, local_aplicacao, objetivo, ativo, " +
                "id_animal, id_medicamento, id_estoque_medicamento, data_hora, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, aplicacao.getDoseAplicada());
            stmt.setString(2, aplicacao.getObservacoes());
            stmt.setString(3, aplicacao.getMembroAplicacao());
            stmt.setString(4, aplicacao.getLocalAplicacao());
            stmt.setString(5, aplicacao.getObjetivo());
            stmt.setBoolean(6, aplicacao.isAtivo());
            stmt.setObject(7, aplicacao.getIdAnimal());
            stmt.setObject(8, aplicacao.getIdMedicamento());
            stmt.setObject(9, aplicacao.getIdEstoqueMedicamento());
            stmt.setTimestamp(10, Timestamp.valueOf(aplicacao.getDataHora()));

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir aplicação no banco: " + e.getMessage());
            throw e;
        }
    }

    public Aplicacao buscarPorId(UUID id) throws SQLException {
        String sql = "SELECT * FROM aplicacao WHERE id_aplicacao = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearAplicacao(rs);
                }
            }
        }
        return null;
    }

    public List<Aplicacao> listarTodos() throws SQLException {
        String sql = "SELECT * FROM aplicacao ORDER BY data_hora";
        List<Aplicacao> aplicacoes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                aplicacoes.add(mapearAplicacao(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar aplicação: " + e.getMessage());
            e.printStackTrace(); // imprime a exception inteira
            throw e;
        }

        return aplicacoes;
    }

    public void atualizar(Aplicacao aplicacao) throws SQLException {
        String sql = "UPDATE aplicacao SET dose_aplicada = ?, observacoes = ?, membro_aplicacao = ?, " +
                "local_aplicacao = ?, objetivo = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_aplicacao = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, aplicacao.getDoseAplicada());
            stmt.setString(2, aplicacao.getObservacoes());
            stmt.setString(3, aplicacao.getMembroAplicacao());
            stmt.setString(4, aplicacao.getLocalAplicacao());
            stmt.setString(5, aplicacao.getObjetivo());
            stmt.setBoolean(6, aplicacao.isAtivo());
            stmt.setObject(7, aplicacao.getIdAplicacao());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar aplicação no banco: " + e.getMessage());
            throw e;
        }
    }

    public void deletar(UUID id) throws SQLException {
        String sql = "DELETE FROM aplicacao WHERE id_aplicacao = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar aplicação no banco: " + e.getMessage());
            throw e;
        }
    }

    private Aplicacao mapearAplicacao(ResultSet rs) throws SQLException {
        UUID idAplicacao = rs.getObject("id_aplicacao", UUID.class);
        double doseAplicada = rs.getDouble("dose_aplicada");
        String membroAplicacao = rs.getString("membro_aplicacao");
        String objetivo = rs.getString("objetivo");
        String localAplicacao = rs.getString("local_aplicacao");
        String observacoes = rs.getString("observacoes");
        UUID idAnimal = rs.getObject("id_animal", UUID.class);
        UUID idMedicamento = rs.getObject("id_medicamento", UUID.class);
        UUID idEstoqueMedicamento = rs.getObject("id_estoque_medicamento", UUID.class);
        boolean ativo = rs.getBoolean("ativo");

        LocalDateTime dataHora = null;
        Timestamp tsDataHora = rs.getTimestamp("data_hora");
        if (tsDataHora != null) {
            dataHora = tsDataHora.toLocalDateTime();
        }

        LocalDateTime atualizadoEm = null;
        Timestamp tsAtualizadoEm = rs.getTimestamp("atualizado_em");
        if (tsAtualizadoEm != null) {
            atualizadoEm = tsAtualizadoEm.toLocalDateTime();
        }

        // Lembrar --> A ordem precisa bater exatamente com a ordem do construtor no Model
        return new Aplicacao(idAplicacao, doseAplicada, membroAplicacao, objetivo, dataHora,
                localAplicacao, observacoes, atualizadoEm, idAnimal, idMedicamento,
                idEstoqueMedicamento, ativo);
    }
}