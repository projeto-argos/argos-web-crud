package dao;

//IMPORTS

import model.Propriedade;
import br.com.argos.connection.ConnectionFactory;
import java.util.UUID;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropriedadeDAO {

//    CRUD

//    INSERT
    public void inserir(Propriedade propriedade) throws SQLException{
        String sql = "INSERT INTO propriedade (telefone, nome, ativo, id_usuario, atualizado_em) " +
                "VALUES (?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, propriedade.getTelefone());
            stmt.setString(2, propriedade.getNome());
            stmt.setBoolean(3, propriedade.isAtivo());
            stmt.setObject(4, propriedade.getIdUsuario());

            stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Erro ao inserir propriedade no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }

    }


//    READ
    public Propriedade buscarPorId(UUID id) throws SQLException {
        String sql = "SELECT * FROM propriedade WHERE id_propriedade = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPropriedade(rs);
                }
            }
        }
        return null;
    }

    public List<Propriedade> listarTodos() throws SQLException {
        String sql = "SELECT * FROM propriedade ORDER BY nome";
        List<Propriedade> propriedades = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                propriedades.add(mapearPropriedade(rs));
            }
        }
        return propriedades;
    }


    //    UPDATE
    public void atualizar(Propriedade propriedade) throws SQLException {
        String sql = "UPDATE propriedade SET nome = ?, telefone = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_propriedade = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, propriedade.getNome());
            stmt.setString(2, propriedade.getTelefone());
            stmt.setBoolean(3, propriedade.isAtivo());
            stmt.setObject(4, propriedade.getIdPropriedade());

            stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Erro ao atualizar propriedade no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }
    }

    //    DELETE
    public void deletar(UUID id) throws SQLException {
        String sql = "DELETE FROM propriedade WHERE id_propriedade = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Erro ao deletar propriedade no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }
    }

    private Propriedade mapearPropriedade(ResultSet rs) throws SQLException {
        Propriedade propriedade = new Propriedade();
        propriedade.setIdPropriedade(rs.getObject("id_propriedade", UUID.class));
        propriedade.setIdUsuario(rs.getObject("id_usuario", UUID.class));
        propriedade.setNome(rs.getString("nome"));
        propriedade.setTelefone(rs.getString("telefone"));
        propriedade.setAtivo(rs.getBoolean("ativo"));
        Timestamp timestamp = rs.getTimestamp("atualizado_em");
        if (timestamp != null){
            propriedade.setAtualizadoEm(timestamp.toLocalDateTime());
        }

        return propriedade;
    }

}
