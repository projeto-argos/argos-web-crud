package br.com.argos.dao;

import br.com.argos.connection.ConnectionFactory;
import br.com.argos.model.MedicationStock;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MedicationStockDAO {

    // INSERT
    public boolean insert(MedicationStock medicationStock) throws SQLException {

        String sql = "INSERT INTO medicationStock(medication_stock_id, available_quantity, storage_location, active, up)"

        try (ConnectionFactory conn = ConnectionFactory.getConnection();
        PreparedStatement stmt = conn.preparedStatement(sql)){

        }
    }

    // FIND BY ID


    // FIND ALL


    // UPDATE


    // DELETE


    // MAP RESULT SET

}