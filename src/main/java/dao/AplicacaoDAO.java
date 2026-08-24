package dao;

import br.com.argos.connection.ConnectionFactory;
import model.Aplicacao;
import java.util.UUID;

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
        Aplicacao aplicacao = new Aplicacao();
        aplicacao.setIdAplicacao(rs.getObject("id_aplicacao", UUID.class));
        aplicacao.setDoseAplicada(rs.getDouble("dose_aplicada"));
        aplicacao.setMembroAplicacao(rs.getString("membro_aplicacao"));
        aplicacao.setObjetivo(rs.getString("objetivo"));
        aplicacao.setLocalAplicacao(rs.getString("local_aplicacao"));
        aplicacao.setObservacoes(rs.getString("observacoes"));
        aplicacao.setAtivo(rs.getBoolean("ativo"));
        aplicacao.setIdAnimal(rs.getObject("id_animal", UUID.class));
        aplicacao.setIdMedicamento(rs.getObject("id_medicamento", UUID.class));
        aplicacao.setIdEstoqueMedicamento(rs.getObject("id_estoque_medicamento", UUID.class));

        Timestamp dataHora = rs.getTimestamp("data_hora");
        if (dataHora != null) {
            aplicacao.setDataHora(dataHora.toLocalDateTime());
        }

        Timestamp atualizadoEm = rs.getTimestamp("atualizado_em");
        if (atualizadoEm != null) {
            aplicacao.setAtualizadoEm(atualizadoEm.toLocalDateTime());
        }

        return aplicacao;
    }
}