package dao;

//IMPORTS

import br.com.argos.connection.ConnectionFactory;
import model.AnimalExcecao;
import java.util.UUID;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalExcecaoDAO {


//    CRUD

//    INSERIR ALGUMA INFORMAÇÃO - INSERT
    public void inserir(AnimalExcecao animalExcecao) throws SQLException{
        String sql = "INSERT INTO animal_excecao (peso, observacoes, brinco, id_lote, ativo, data_nascimento, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, animalExcecao.getPeso());
            stmt.setString(2, animalExcecao.getObservacoes());
            stmt.setInt(3, animalExcecao.getBrinco());
            stmt.setObject(4, animalExcecao.getIdLote());
            stmt.setBoolean(5, animalExcecao.isAtivo());
            stmt.setDate(6, Date.valueOf(animalExcecao.getDataNascimento()));

            stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Erro ao inserir animal exceção no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }
    }

//    BUSCAR O ID
    public AnimalExcecao buscarPorId(UUID id) throws SQLException {
        String sql = "SELECT * FROM animal_excecao WHERE id_animal = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearAnimal(rs);
                }
            }
        }
        return null;
    }

//    LISTAR TODOS
    public List<AnimalExcecao> listarTodos() throws SQLException {
        String sql = "SELECT * FROM animal_excecao ORDER BY brinco";
        List<AnimalExcecao> animalExcecaos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                animalExcecaos.add(mapearAnimal(rs));
            }
        }
        return animalExcecaos;
    }

//    ATUALIZAR ALGUMA INFORMAÇÃO - UPDATE

    public void atualizar(AnimalExcecao animalExcecao) throws SQLException {
        String sql = "UPDATE animal_excecao SET peso = ?, observacoes = ?, brinco = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_animal = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, animalExcecao.getPeso());
            stmt.setString(2, animalExcecao.getObservacoes());
            stmt.setInt(3, animalExcecao.getBrinco());
            stmt.setBoolean(4, animalExcecao.isAtivo());
            stmt.setObject(5, animalExcecao.getIdAnimal());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar animal exceção no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }
    }

//    DELETAR ALGUMA INFORMAÇÃO - DELETE

    public void deletar(UUID id) throws SQLException {
        String sql = "DELETE FROM animal_excecao WHERE id_animal = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e){
            System.err.println("Erro ao deletar animal exceção no banco " + e.getMessage());
            throw e; // relança, pra quem chamou (futuramente o Servlet) também saber que deu erro
        }
    }

//    MAPEAR ANIMAL
    private AnimalExcecao mapearAnimal(ResultSet rs) throws SQLException {
        AnimalExcecao animalExcecao = new AnimalExcecao();
        animalExcecao.setIdAnimal(rs.getObject("id_animal", UUID.class));
        animalExcecao.setIdLote(rs.getObject("id_lote", UUID.class));
        animalExcecao.setPeso(rs.getDouble("peso"));
        animalExcecao.setBrinco(rs.getInt("brinco"));
        animalExcecao.setAtivo(rs.getBoolean("ativo"));
        animalExcecao.setObservacoes(rs.getString("observacoes"));

        Date dataNascimento = rs.getDate("data_nascimento");
        if (dataNascimento != null) {
            animalExcecao.setDataNascimento(dataNascimento.toLocalDate());
        }

        Timestamp timestamp = rs.getTimestamp("atualizado_em");
        if (timestamp != null){
            animalExcecao.setAtualizadoEm(timestamp.toLocalDateTime());
        }

        return animalExcecao;
    }
}