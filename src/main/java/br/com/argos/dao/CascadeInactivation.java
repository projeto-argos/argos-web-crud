package br.com.argos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Inativacao em cascata (soft delete dos registros "filhos").
 */
final class CascadeInactivation {

    private CascadeInactivation() {
    }

    /** Subconsultas */
    static final String LOTS_BY_ID =
            "SELECT id_lot FROM lot WHERE id_lot = ?";
    static final String LOTS_BY_HERD =
            "SELECT id_lot FROM lot WHERE id_herd = ?";

    /** Subconsultas */
    static final String HERDS_BY_ID =
            "SELECT id_herd FROM herd WHERE id_herd = ?";
    static final String HERDS_BY_PROPERTY =
            "SELECT id_herd FROM herd WHERE id_property = ?";

    /** Inativa o que depende dos lotes */
    static void inactivateLotChildren(Connection conn, String lotIdsQuery, UUID id) throws SQLException {
        run(conn, "UPDATE withdrawal_period SET active = false, updated_at = now() "
                + "WHERE active = true AND id_animal IN "
                + "(SELECT id_animal FROM animal WHERE id_lot IN (" + lotIdsQuery + "))", id);

        run(conn, "UPDATE application SET active = false, updated_at = now() "
                + "WHERE active = true AND id_lot IN (" + lotIdsQuery + ")", id);

        run(conn, "UPDATE animal SET active = false, updated_at = now() "
                + "WHERE active = true AND id_lot IN (" + lotIdsQuery + ")", id);
    }

    /** Inativa o que depende dos rebanhos */
    static void inactivateHerdChildren(Connection conn, String herdIdsQuery, UUID id) throws SQLException {
        String lotsOfHerds = "SELECT id_lot FROM lot WHERE id_herd IN (" + herdIdsQuery + ")";

        inactivateLotChildren(conn, lotsOfHerds, id);

        run(conn, "UPDATE lot SET active = false, updated_at = now() "
                + "WHERE active = true AND id_herd IN (" + herdIdsQuery + ")", id);
    }

    /** Executa um UPDATE que tem um único parametro */
    private static void run(Connection conn, String sql, UUID id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        }
    }
}
