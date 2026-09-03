package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.PeriodoCarencia;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PeriodoCarenciaDAO {

//    INSERT - INSERIR INFORMAÇÕES
    public void inserir(PeriodoCarencia periodoCarencia) throws SQLException {
        String sql = "INSERT INTO periodo_carencia (liberado_para_abate, data_fim_carencia, id_animal, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, periodoCarencia.isLiberadoParaAbate());

            // Tratamento para LocalDate, evitando erro de nulidade
            if (periodoCarencia.getDataFimCarencia() != null) {
                stmt.setDate(2, java.sql.Date.valueOf(periodoCarencia.getDataFimCarencia()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }

            stmt.setObject(3, periodoCarencia.getIdAnimal());
            stmt.setBoolean(4, periodoCarencia.isAtivo());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir Período de Carência no banco: " + e.getMessage());
            throw e;
        }
    }

//    BUSCAR POR ID
    public PeriodoCarencia buscarPorId(UUID id) throws SQLException {
        String sql = "SELECT * FROM periodo_carencia WHERE id_carencia = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPeriodoCarencia(rs);
                }
            }
        }
        return null;
    }

    //    LISTAR PERIODO CARENCIA

    public List<PeriodoCarencia> listarTodos() throws SQLException {
        String sql = "SELECT * FROM periodo_carencia ORDER BY data_fim_carencia DESC";
        List<PeriodoCarencia> periodosCarencia = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                periodosCarencia.add(mapearPeriodoCarencia(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar Períodos de Carência: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return periodosCarencia;
    }

//    UPDATE - ATUALIZAR INFORMAÇÕES
    public void atualizar(PeriodoCarencia periodoCarencia) throws SQLException {
        String sql = "UPDATE periodo_carencia SET liberado_para_abate = ?, data_fim_carencia = ?, " +
                "id_animal = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_carencia = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, periodoCarencia.isLiberadoParaAbate());

            if (periodoCarencia.getDataFimCarencia() != null) {
                stmt.setDate(2, java.sql.Date.valueOf(periodoCarencia.getDataFimCarencia()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }

            stmt.setObject(3, periodoCarencia.getIdAnimal());
            stmt.setBoolean(4, periodoCarencia.isAtivo());
            stmt.setObject(5, periodoCarencia.getIdCarencia());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar Período de Carência no banco: " + e.getMessage());
            throw e;
        }
    }

//    DELETE - DELETAR INFORMAÇÕES
    public void deletar(UUID id) throws SQLException {
        String sql = "DELETE FROM periodo_carencia WHERE id_carencia = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar Período de Carência no banco: " + e.getMessage());
            throw e;
        }
    }

//    MAPEAR PERIODO CARENCIA
    private PeriodoCarencia mapearPeriodoCarencia(ResultSet rs) throws SQLException {
        UUID idCarencia = rs.getObject("id_carencia", UUID.class);
        boolean liberadoParaAbate = rs.getBoolean("liberado_para_abate");

        LocalDate dataFimCarencia = null;
        java.sql.Date sqlDate = rs.getDate("data_fim_carencia");
        if (sqlDate != null) {
            dataFimCarencia = sqlDate.toLocalDate();
        }

        LocalDateTime atualizadoEm = null;
        Timestamp tsAtualizadoEm = rs.getTimestamp("atualizado_em");
        if (tsAtualizadoEm != null) {
            atualizadoEm = tsAtualizadoEm.toLocalDateTime();
        }

        UUID idAnimal = rs.getObject("id_animal", UUID.class);
        boolean ativo = rs.getBoolean("ativo");

        return new PeriodoCarencia(idCarencia, liberadoParaAbate, dataFimCarencia, atualizadoEm, idAnimal, ativo);
    }
}