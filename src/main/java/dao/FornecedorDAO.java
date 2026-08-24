package dao;

//IMPORTS

import model.Fornecedor;
import br.com.argos.connection.ConnectionFactory;
import java.util.UUID;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {


//          CRUD

//    INSERT
    public void inserir(Fornecedor fornecedor) throws SQLException {
        String sql = "INSERT INTO fornecedor (cnpj, nome, telefone, email, ativo, atualizado_em) " +
                "VALUES (?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getCnpj());
            stmt.setString(2, fornecedor.getNome());
            stmt.setString(3, fornecedor.getTelefone());
            stmt.setString(4, fornecedor.getEmail());
            stmt.setBoolean(5, fornecedor.isAtivo());

            stmt.executeUpdate();
        }
    }


//    READ
    public Fornecedor buscarPorId(UUID id) throws SQLException {
        String sql = "SELECT * FROM fornecedor WHERE id_fornecedor = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearFornecedor(rs);
                }
            }
        }
        return null;
    }

    public List<Fornecedor> listarTodos() throws SQLException {
        String sql = "SELECT * FROM fornecedor ORDER BY nome";
        List<Fornecedor> fornecedores = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                fornecedores.add(mapearFornecedor(rs));
            }
        }
        return fornecedores;
    }


//    UPDATE
    public void atualizar(Fornecedor fornecedor) throws SQLException {
        String sql = "UPDATE fornecedor SET cnpj = ?, nome = ?, telefone = ?, email = ?, ativo = ?, atualizado_em = now() " +
                "WHERE id_fornecedor = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getCnpj());
            stmt.setString(2, fornecedor.getNome());
            stmt.setString(3, fornecedor.getTelefone());
            stmt.setString(4, fornecedor.getEmail());
            stmt.setBoolean(5, fornecedor.isAtivo());
            stmt.setObject(6, fornecedor.getIdFornecedor());

            stmt.executeUpdate();
        }
    }

//    DELETE
    public void deletar(UUID id) throws SQLException {
        String sql = "DELETE FROM fornecedor WHERE id_fornecedor = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);
            stmt.executeUpdate();
        }
    }

    private Fornecedor mapearFornecedor(ResultSet rs) throws SQLException {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setIdFornecedor(rs.getObject("id_fornecedor", UUID.class));
        fornecedor.setCnpj(rs.getString("cnpj"));
        fornecedor.setNome(rs.getString("nome"));
        fornecedor.setTelefone(rs.getString("telefone"));
        fornecedor.setEmail(rs.getString("email"));
        fornecedor.setAtivo(rs.getBoolean("ativo"));
        Timestamp timestamp = rs.getTimestamp("atualizado_em");
        if (timestamp != null){
            fornecedor.setAtualizadoEm(timestamp.toLocalDateTime());
        }

        return fornecedor;
    }
}