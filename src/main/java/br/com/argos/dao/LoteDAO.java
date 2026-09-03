package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.Lote;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoteDAO {

    // INSERT - INSERIR INFORMAÇÕES
    public void inserir(Lote lote) throws SQLException {
        String sql = "INSERT INTO lote (categoria, id_rebanho, ativo, qtd_cabecas, atualizado_em) " +
                "VALUES (?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lote.getCategoria());
            stmt.setObject(2, lote.getIdRebanho());
            stmt.setBoolean(3, lote.isAtivo());
            stmt.setInt(4, lote.getQtdCabecas());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao inserir Lote no banco: " + e.getMessage());
            throw e;
        }
    }

    // BUSCAR POR ID
    public Lote buscarPorId(UUID id) throws SQLException {
        String sql = "SELECT * FROM lote WHERE id_lote = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearLote(rs);
                }
            }
        }
        return null;
    }

    // LISTAR LOTE
    public List<Lote> listarTodos() throws SQLException {
        // Ordenando por categoria, mas você pode mudar para atualizado_em se preferir
        String sql = "SELECT * FROM lote ORDER BY categoria ASC";
        List<Lote> lotes = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lotes.add(mapearLote(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar Lotes: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return lotes;
    }

    // UPDATE - ATUALIZAR INFORMAÇÕES
    public void atualizar(Lote lote) throws SQLException {
        String sql = "UPDATE lote SET categoria = ?, id_rebanho = ?, ativo = ?, qtd_cabecas = ?, atualizado_em = now() " +
                "WHERE id_lote = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lote.getCategoria());
            stmt.setObject(2, lote.getIdRebanho());
            stmt.setBoolean(3, lote.isAtivo());
            stmt.setInt(4, lote.getQtdCabecas());
            stmt.setObject(5, lote.getIdLote());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar Lote no banco: " + e.getMessage());
            throw e;
        }
    }

    // DELETE - DELETAR INFORMAÇÕES
    public void deletar(UUID id) throws SQLException {
        String sql = "DELETE FROM lote WHERE id_lote = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar Lote no banco: " + e.getMessage());
            throw e;
        }
    }

    // MAPEAR LOTE
    private Lote mapearLote(ResultSet rs) throws SQLException {
        UUID idLote = rs.getObject("id_lote", UUID.class);
        String categoria = rs.getString("categoria");
        UUID idRebanho = rs.getObject("id_rebanho", UUID.class);
        boolean ativo = rs.getBoolean("ativo");
        int qtdCabecas = rs.getInt("qtd_cabecas");

        // Tratamento da data
        LocalDateTime atualizadoEm = null;
        Timestamp tsAtualizadoEm = rs.getTimestamp("atualizado_em");
        if (tsAtualizadoEm != null) {
            atualizadoEm = tsAtualizadoEm.toLocalDateTime();
        }


        // Lote(UUID idLote, String categoria, LocalDateTime atualizadoEm, UUID idRebanho, boolean ativo, int qtdCabecas)
        return new Lote(idLote, categoria, atualizadoEm, idRebanho, ativo, qtdCabecas);
    }
}