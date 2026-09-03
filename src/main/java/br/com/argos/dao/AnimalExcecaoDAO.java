package br.com.argos.dao;

//IMPORTS

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.AnimalExcecao;

import java.time.LocalDate;
import java.util.UUID;
import java.time.LocalDateTime;

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
        } catch (SQLException e) {
            System.err.println("Erro ao listar animal exceção: " + e.getMessage());
            e.printStackTrace(); // imprime a exception inteira
            throw e;
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
    UUID idAnimal = rs.getObject("id_animal", UUID.class);
    double peso = rs.getDouble("peso");

    LocalDate dataNascimento = null;
    Date dtDataNascimento = rs.getDate("data_nascimento");
    if (dtDataNascimento != null) {
        dataNascimento = dtDataNascimento.toLocalDate();
    }

    String observacoes = rs.getString("observacoes");
    int brinco = rs.getInt("brinco");

    LocalDateTime atualizadoEm = null;
    Timestamp tsAtualizadoEm = rs.getTimestamp("atualizado_em");
    if (tsAtualizadoEm != null) {
        atualizadoEm = tsAtualizadoEm.toLocalDateTime();
    }

    UUID idLote = rs.getObject("id_lote", UUID.class);
    boolean ativo = rs.getBoolean("ativo");

    // Lembrar --> A ordem precisa bater exatamente com a ordem do construtor no Model
    return new AnimalExcecao(idAnimal, peso, dataNascimento, observacoes, brinco,
            atualizadoEm, idLote, ativo);
}
}