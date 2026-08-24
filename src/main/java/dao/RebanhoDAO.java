package dao;

//  IMPORTS

import model.Rebanho;
import br.com.argos.connection.ConnectionFactory;
import java.util.UUID;

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
        Rebanho rebanho = new Rebanho();
        rebanho.setIdRebanho(rs.getObject("id_rebanho", UUID.class));
        rebanho.setIdPropriedade(rs.getObject("id_propriedade", UUID.class));
        rebanho.setNome(rs.getString("nome"));
        rebanho.setRaca(rs.getString("raca"));
        rebanho.setAtivo(rs.getBoolean("ativo"));
        rebanho.setFinalidade(rs.getString("finalidade"));
        rebanho.setQtdCabecas(rs.getInt("qtd_cabecas"));

        Timestamp timestamp = rs.getTimestamp("atualizado_em");
        if (timestamp != null){
            rebanho.setAtualizadoEm(timestamp.toLocalDateTime());
        }

        return rebanho;
    }
}