//package br.com.argos.dao;
//
//import br.com.argos.connection.ConnectionFactory;
//import br.com.argos.exceptions.DataAccessException;
//import br.com.argos.interfaces.GenericDAO;
//import br.com.argos.interfaces.IBatchDAO;
//import br.com.argos.model.Batch;
//
//import java.sql.*;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
///**
// * Classe de acesso a dados (DAO).
// * Acesso ao banco para a tabela Batch (Lote).
// * Implementa as operações de CRUD e métodos personalizados.
// */
//public class BatchDAO implements GenericDAO<Batch, UUID>, IBatchDAO {
//
//    // CREATE
//    @Override
//    public void insert(Batch batch) {
//        String sql = """
//                INSERT INTO batch (id_herd, batch_code, category, head_count, opening_date, updated_at)
//                VALUES (?, ?, ?, ?, ?, now())
//                """;
//
//        try (Connection conn = ConnectionFactory.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            fillStatement(stmt, batch);
//            stmt.executeUpdate();
//
//        } catch (SQLException e) {
//            throw new DataAccessException("Error inserting batch: ", e);
//        }
//    }
//
//    // READ
//    @Override
//    public Batch findById(UUID id) {
//        String sql = """
//                SELECT * FROM batch WHERE id_batch = ? AND active = true
//                """;
//
//        try (Connection conn = ConnectionFactory.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setObject(1, id);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return mapBatch(rs);
//                }
//            }
//            return null;
//
//        } catch (SQLException e) {
//            throw new DataAccessException("Error searching for batch: " + id, e);
//        }
//    }
//
//    @Override
//    public List<Batch> findAll() {
//        String sql = """
//                SELECT * FROM batch WHERE active = true ORDER BY batch_code
//                """;
//        List<Batch> batches = new ArrayList<>();
//
//        try (Connection conn = ConnectionFactory.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//
//            while (rs.next()) {
//                batches.add(mapBatch(rs));
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException("Error listing batches: ", e);
//        }
//
//        return batches;
//    }
//
//    // FIND BATCH BY CODE
//    @Override
//    public Batch findByBatchCode(String batchCode) {
//        String sql = """
//                SELECT * FROM batch WHERE batch_code = ? AND active = true
//                """;
//
//        try (Connection conn = ConnectionFactory.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setString(1, batchCode);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    return mapBatch(rs);
//                }
//            }
//            return null;
//
//        } catch (SQLException e) {
//            throw new DataAccessException("Error searching for batch by code: " + batchCode, e);
//        }
//    }
//
//    // FIND BATCHES BY HERD
//    @Override
//    public List<Batch> findByHerd(UUID idHerd) {
//        String sql = """
//                SELECT * FROM batch WHERE id_herd = ? AND active = true ORDER BY batch_code
//                """;
//        List<Batch> batches = new ArrayList<>();
//
//        try (Connection conn = ConnectionFactory.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setObject(1, idHerd);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    batches.add(mapBatch(rs));
//                }
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException("Error listing batches by herd: " + idHerd, e);
//        }
//
//        return batches;
//    }
//
//    // FIND BATCHES BY CATEGORY
//    @Override
//    public List<Batch> findByCategory(String category) {
//        String sql = """
//                SELECT * FROM batch WHERE category = ? AND active = true ORDER BY batch_code
//                """;
//        List<Batch> batches = new ArrayList<>();
//
//        try (Connection conn = ConnectionFactory.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setString(1, category);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    batches.add(mapBatch(rs));
//                }
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException("Error listing batches by category: " + category, e);
//        }
//
//        return batches;
//    }
//
//    // FIND BATCHES BY OPENING DATE RANGE
//    @Override
//    public List<Batch> findByOpeningDateBetween(LocalDate start, LocalDate end) {
//        String sql = """
//                SELECT * FROM batch WHERE opening_date BETWEEN ? AND ? AND active = true
//                ORDER BY opening_date
//                """;
//        List<Batch> batches = new ArrayList<>();
//
//        try (Connection conn = ConnectionFactory.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setDate(1, Date.valueOf(start));
//            stmt.setDate(2, Date.valueOf(end));
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    batches.add(mapBatch(rs));
//                }
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException("Error listing batches by opening date: ", e);
//        }
//
//        return batches;
//    }
//
//    // UPDATE
//    @Override
//    public void update(Batch batch) {
//        String sql = """
//                UPDATE batch SET id_herd = ?, batch_code = ?, category = ?, head_count = ?,
//                opening_date = ?, updated_at = now()
//                WHERE id_batch = ? AND active = true
//                """;
//
//        try (Connection conn = ConnectionFactory.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            fillStatement(stmt, batch);
//            stmt.setObject(6, batch.getIdBatch());
//
//            int rowsAffected = stmt.executeUpdate();
//
//            if (rowsAffected == 0) {
//                throw new DataAccessException("Batch not found " + batch.getIdBatch());
//            }
//
//        } catch (SQLException e) {
//            throw new DataAccessException("Error updating batch: ", e);
//        }
//    }
//
//    // SOFT DELETE
//    @Override
//    public void delete(UUID id) {
//        String sql = """
//                UPDATE batch SET active = false, updated_at = now() WHERE id_batch = ? AND active = true
//                """;
//
//        try (Connection conn = ConnectionFactory.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setObject(1, id);
//
//            int rowsAffected = stmt.executeUpdate();
//
//            if (rowsAffected == 0) {
//                throw new DataAccessException("Batch not found " + id);
//            }
//
//        } catch (SQLException e) {
//            throw new DataAccessException("Error deactivating batch: " + id, e);
//        }
//    }
//
//    // SOFT DELETE BY CODE
//    @Override
//    public void deleteByBatchCode(String batchCode) {
//        String sql = """
//                UPDATE batch SET active = false, updated_at = now() WHERE batch_code = ? AND active = true
//                """;
//
//        try (Connection conn = ConnectionFactory.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setString(1, batchCode);
//
//            int rowsAffected = stmt.executeUpdate();
//
//            if (rowsAffected == 0) {
//                throw new DataAccessException("Batch not found with code " + batchCode);
//            }
//
//        } catch (SQLException e) {
//            throw new DataAccessException("Error deactivating batch: " + batchCode, e);
//        }
//    }
//
//    // SOFT DELETE ALL BATCHES OF A HERD
//    @Override
//    public void deleteByHerd(UUID idHerd) {
//        String sql = """
//                UPDATE batch SET active = false, updated_at = now() WHERE id_herd = ? AND active = true
//                """;
//
//        try (Connection conn = ConnectionFactory.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setObject(1, idHerd);
//            stmt.executeUpdate();
//
//        } catch (SQLException e) {
//            throw new DataAccessException("Error deactivating batches of herd: " + idHerd, e);
//        }
//    }
//
//    // Preenche os parâmetros 1 a 5 (usados no insert e no update)
//    private void fillStatement(PreparedStatement stmt, Batch batch) throws SQLException {
//        stmt.setObject(1, batch.getIdHerd());
//        stmt.setString(2, batch.getBatchCode());
//        stmt.setString(3, batch.getCategory());
//        stmt.setObject(4, batch.getHeadCount());
//
//        if (batch.getOpeningDate() != null) {
//            stmt.setDate(5, Date.valueOf(batch.getOpeningDate()));
//        } else {
//            stmt.setNull(5, Types.DATE);
//        }
//    }
//
//    // MAPPER
//    private Batch mapBatch(ResultSet rs) throws SQLException {
//        UUID id = rs.getObject("id_batch", UUID.class);
//        UUID idHerd = rs.getObject("id_herd", UUID.class);
//        String batchCode = rs.getString("batch_code");
//        String category = rs.getString("category");
//        Integer headCount = rs.getObject("head_count", Integer.class);
//
//        LocalDate openingDate = null;
//        Date sqlDate = rs.getDate("opening_date");
//        if (sqlDate != null) {
//            openingDate = sqlDate.toLocalDate();
//        }
//
//        LocalDateTime updatedAt = null;
//        Timestamp tsUpdatedAt = rs.getTimestamp("updated_at");
//        if (tsUpdatedAt != null) {
//            updatedAt = tsUpdatedAt.toLocalDateTime();
//        }
//
//        boolean active = rs.getBoolean("active");
//
//        return new Batch(id, idHerd, batchCode, category, headCount, openingDate, updatedAt, active);
//    }
//}