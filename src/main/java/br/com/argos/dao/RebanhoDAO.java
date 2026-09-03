package br.com.argos.dao;

//  IMPORTS

import br.com.argos.model.Rebanho;
import br.com.argos.connection.ConnectionFactory;
import java.util.UUID;
import java.time.LocalDateTime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RebanhoDAO {


//    CRUD

//        INSERIR ALGUMA INFORMAÇÃO - INSERT

    public void inserir(Rebanho rebanho) throws SQLException{
        String sql = "INSERT INTO rebanho (nome, raca, finalidade, id_propriedade, qtd_cabecas, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rebanho.getNome());
            stmt.setString(2, rebanho.getRaca());
            stmt.setString(3, rebanho.getFinalidade());
            stmt.setObject(4, rebanho.getIdPropriedade());
            stmt.setInt(5, rebanho.getQtdCabecas());
            stmt.setBoolean(6, rebanho.isAtivo());

            stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Erro ao inserir rebanho no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }
    }

//    BUSCAR O ID
    public Rebanho buscarPorId(UUID id) throws SQLException {
        String sql = "SELECT * FROM rebanho WHERE id_rebanho = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearRebanho(rs);
                }
            }
        }
        return null;
    }


//    LISTAR TODOS
    public List<Rebanho> listarTodos() throws SQLException {
        String sql = "SELECT * FROM rebanho ORDER BY nome";
        List<Rebanho> rebanhos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rebanhos.add(mapearRebanho(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar rebanho: " + e.getMessage());
            e.printStackTrace(); // imprime a exception inteira
            throw e;
        }

        return rebanhos;
    }

    //    ATUALIZAR ALGUMA INFORMAÇÃO - UPDATE

    public void atualizar(Rebanho rebanho) throws SQLException {
        String sql = "UPDATE rebanho SET nome = ?, raca = ?, finalidade = ?, qtd_cabecas = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_rebanho = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rebanho.getNome());
            stmt.setString(2, rebanho.getRaca());
            stmt.setString(3, rebanho.getFinalidade());
            stmt.setInt(4, rebanho.getQtdCabecas());
            stmt.setBoolean(5, rebanho.isAtivo());
            stmt.setObject(6, rebanho.getIdRebanho());

            stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Erro ao atualizar rebanho no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }
    }

    //    DELETAR ALGUMA INFORMAÇÃO - DELETE

    public void deletar(UUID id) throws SQLException {
        String sql = "DELETE FROM rebanho WHERE id_rebanho = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Erro ao deletar rebanho no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }
    }

    //    MAPEAR REBANHO

    private Rebanho mapearRebanho(ResultSet rs) throws SQLException {
        UUID idRebanho = rs.getObject("id_rebanho", UUID.class);
        String raca = rs.getString("raca");
        String finalidade = rs.getString("finalidade");
        String nome = rs.getString("nome");

        LocalDateTime atualizadoEm = null;
        Timestamp tsAtualizadoEm = rs.getTimestamp("atualizado_em");
        if (tsAtualizadoEm != null) {
            atualizadoEm = tsAtualizadoEm.toLocalDateTime();
        }

        UUID idPropriedade = rs.getObject("id_propriedade", UUID.class);
        boolean ativo = rs.getBoolean("ativo");
        int qtdCabecas = rs.getInt("qtd_cabecas");

        // A ordem precisa bater exatamente com a ordem do construtor no Model
        return new Rebanho(idRebanho, raca, finalidade, nome, atualizadoEm,
                idPropriedade, ativo, qtdCabecas);
    }
}