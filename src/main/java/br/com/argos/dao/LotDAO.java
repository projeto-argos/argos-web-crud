
package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.exceptions.DataAccessException;
import br.com.argos.interfaces.GenericDAO;
import br.com.argos.model.Lot;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LotDAO implements GenericDAO<Lot, UUID> {

    // CREATE
    @Override
    public void insert(Lot lot) {
        String sql = "INSERT INTO LOT " +
                "(id_herd, lot_code, category, head_count, open_date, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, now())";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, lot.getIdHerd());
            stmt.setString(2, lot.getLotCode());
            stmt.setString(3, lot.getCategory());

            if (lot.getHeadCount() != null) {
                stmt.setInt(4, lot.getHeadCount());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            if (lot.getOpeningDate() != null) {
                stmt.setDate(5, Date.valueOf(lot.getOpeningDate()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error inserting lot.", e);
        }
    }

    // READ BY ID
    @Override
    public Lot findById(UUID id) {
        String sql = """
                SELECT * FROM LOT
                WHERE id_lot = ? AND active = TRUE
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapLot(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error searching for lot: " + id, e);
        }
    }

    // READ ALL ACTIVE LOTS
    @Override
    public List<Lot> findAll() {
        String sql = """
                SELECT * FROM LOT
                WHERE active = TRUE
                ORDER BY lot_code
                """;

        List<Lot> lots = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lots.add(mapLot(rs));
            }

            return lots;

        } catch (SQLException e) {
            throw new DataAccessException("Error listing lots.", e);
        }
    }

    // FIND LOT BY CODE AND HERD
    public Lot findByLotCodeAndHerd(String lotCode, UUID idHerd) {
        String sql = """
                SELECT * FROM LOT
                WHERE lot_code = ? AND id_herd = ? AND active = TRUE
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lotCode);
            stmt.setObject(2, idHerd);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapLot(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error searching for lot by code and herd.", e);
        }
    }

    // FIND LOTS BY HERD
    @Override
    public List<Lot> findByHerd(UUID idHerd) {
        String sql = """
                SELECT * FROM LOT
                WHERE id_herd = ? AND active = TRUE
                ORDER BY lot_code
                """;

        List<Lot> lots = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, idHerd);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lots.add(mapLot(rs));
                }
            }

            return lots;

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error listing lots by herd: " + idHerd, e);
        }
    }

    // FIND LOTS BY CATEGORY
    @Override
    public List<Lot> findByCategory(String category) {
        String sql = """
                SELECT * FROM LOT
                WHERE category = ? AND active = TRUE
                ORDER BY lot_code
                """;

        List<Lot> lots = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lots.add(mapLot(rs));
                }
            }

            return lots;

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error listing lots by category: " + category, e);
        }
    }

    // UPDATE
    @Override
    public void update(Lot lot) {
        String sql = """
                UPDATE LOT SET
                    id_herd = ?,
                    lot_code = ?,
                    category = ?,
                    head_count = ?,
                    open_date = ?,
                    updated_at = now()
                WHERE id_lot = ? AND active = TRUE
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, lot.getIdHerd());
            stmt.setString(2, lot.getLotCode());
            stmt.setString(3, lot.getCategory());

            if (lot.getHeadCount() != null) {
                stmt.setInt(4, lot.getHeadCount());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            if (lot.getOpeningDate() != null) {
                stmt.setDate(5, Date.valueOf(lot.getOpeningDate()));
            } else {
                stmt.setNull(5, Types.DATE);
            }

            stmt.setObject(6, lot.getIdLot());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException(
                        "Lot not found: " + lot.getIdLot());
            }

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error updating lot: " + lot.getIdLot(), e);
        }
    }

    // SOFT DELETE BY ID
    @Override
    public void delete(UUID id) {
        String sql = """
                UPDATE LOT
                SET active = FALSE, updated_at = now()
                WHERE id_lot = ? AND active = TRUE
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new DataAccessException("Lot not found: " + id);
            }

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error deactivating lot: " + id, e);
        }
    }

    // SOFT DELETE ALL LOTS OF A HERD
    public void deleteByHerd(UUID idHerd) {
        String sql = """
                UPDATE LOT
                SET active = FALSE, updated_at = now()
                WHERE id_herd = ? AND active = TRUE
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, idHerd);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException(
                    "Error deactivating lots of herd: " + idHerd, e);
        }
    }

    // MAPPER
    private Lot mapLot(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id_lot", UUID.class);
        UUID idHerd = rs.getObject("id_herd", UUID.class);

        String lotCode = rs.getString("lot_code");
        String category = rs.getString("category");

        Integer headCount = rs.getObject("head_count", Integer.class);

        LocalDate openingDate = null;
        Date sqlDate = rs.getDate("open_date");

        if (sqlDate != null) {
            openingDate = sqlDate.toLocalDate();
        }

        LocalDateTime updatedAt = null;
        Timestamp timestamp = rs.getTimestamp("updated_at");

        if (timestamp != null) {
            updatedAt = timestamp.toLocalDateTime();
        }

        boolean active = rs.getBoolean("active");

        return new Lot(id, idHerd, lotCode, category, headCount, openingDate, updatedAt, active);
    }
}
