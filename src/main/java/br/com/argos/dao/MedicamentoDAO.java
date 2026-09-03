package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Medicamento;

import java.util.UUID;
import java.time.LocalDateTime;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDAO {

//    CRUD

//    CREATE - INSERIR INFORMAÇÕES
    public void inserir(Medicamento medicamento) throws SQLException {
        String sql = "INSERT INTO medicamento (nome, dose_ml, principio_ativo, carencia_indicada, categoria_terapeutica, " +
                "id_fornecedor, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medicamento.getNome());
            stmt.setDouble(2, medicamento.getDoseMl());
            stmt.setString(3, medicamento.getPrincipioAtivo());
            stmt.setInt(4, medicamento.getCarenciaIndicada());
            stmt.setString(5, medicamento.getCategoriaTerapeutica());
            stmt.setObject(6, medicamento.getIdFornecedor());
            stmt.setBoolean(7, medicamento.isAtivo());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir medicamento no banco: " + e.getMessage());
            throw e;
        }
    }

//    BUSCAR POR ID
    public Medicamento buscarPorId(UUID id) throws SQLException {
        String sql = "SELECT * FROM medicamento WHERE id_medicamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMedicamento(rs);
                }
            }
        }
        return null;
    }

//    LISTAR TODOS OS MEDICAMENTOS
    public List<Medicamento> listarTodos() throws SQLException {
        String sql = "SELECT * FROM medicamento ORDER BY nome";
        List<Medicamento> medicamentos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                medicamentos.add(mapearMedicamento(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar medicamentos: " + e.getMessage());
            e.printStackTrace(); // imprime a exception inteira
            throw e;
        }

        return medicamentos;
    }

//      UPDATE - ATUALIZAR INFORMAÇÕES
    public void atualizar(Medicamento medicamento) throws SQLException {
        String sql = "UPDATE medicamento SET nome = ?, dose_ml = ?, principio_ativo = ?, " +
                "carencia_indicada = ?, categoria_terapeutica = ?, id_fornecedor = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_medicamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medicamento.getNome());
            stmt.setDouble(2, medicamento.getDoseMl());
            stmt.setString(3, medicamento.getPrincipioAtivo());
            stmt.setInt(4, medicamento.getCarenciaIndicada());
            stmt.setString(5, medicamento.getCategoriaTerapeutica());
            stmt.setObject(6, medicamento.getIdFornecedor());
            stmt.setBoolean(7, medicamento.isAtivo());
            stmt.setObject(8, medicamento.getIdMedicamento());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar medicamento no banco: " + e.getMessage());
            throw e;
        }
    }

//    DELETE - DELETAR INFORMAÇÕES
    public void deletar(UUID id) throws SQLException {
        String sql = "DELETE FROM medicamento WHERE id_medicamento = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar medicamento no banco: " + e.getMessage());
            throw e;
        }
    }

//    MAPEAR MEDICAMENTOS
    private Medicamento mapearMedicamento(ResultSet rs) throws SQLException {
        UUID idMedicamento = rs.getObject("id_medicamento", UUID.class);
        String nome = rs.getString("nome");
        double doseMl = rs.getDouble("dose_ml");
        String principioAtivo = rs.getString("principio_ativo");
        int carenciaIndicada = rs.getInt("carencia_indicada");
        String categoriaTerapeutica = rs.getString("categoria_terapeutica");
        UUID idFornecedor = rs.getObject("id_fornecedor", UUID.class);
        boolean ativo = rs.getBoolean("ativo");

        LocalDateTime atualizadoEm = null;
        Timestamp tsAtualizadoEm = rs.getTimestamp("atualizado_em");
        if (tsAtualizadoEm != null) {
            atualizadoEm = tsAtualizadoEm.toLocalDateTime();
        }

        // A ordem exata baseada no construtor do model Medicamento:
        // (UUID idMedicamento, String nome, double doseMl, String principioAtivo, int carenciaIndicada, String categoriaTerapeutica, LocalDateTime atualizadoEm, UUID idFornecedor, boolean ativo)
        return new Medicamento(idMedicamento, nome, doseMl, principioAtivo, carenciaIndicada,
                categoriaTerapeutica, atualizadoEm, idFornecedor, ativo);
    }
}