package br.com.argos.util;

import br.com.argos.exceptions.DataAccessException;
import br.com.argos.exceptions.ValidationException;

import java.sql.SQLException;

/**
 * Traduz erros do PostgreSQL em exceções que o usuário entende.
 */
public final class DbErrors {

    private static final String CHAVE_DUPLICADA = "23505";

    private DbErrors() {
    }

    public static RuntimeException translate(String message, SQLException e) {
        String state = e.getSQLState();

        if (CHAVE_DUPLICADA.equals(state)) {
            String text = String.valueOf(e.getMessage());
            if (text.contains("cpf")) {
                throw new DataAccessException("CPF já registrado.");
            }
            if (text.contains("email")) {
                throw new DataAccessException("Email já registrado.");
            }
            if (text.contains("ear")) {
                throw new DataAccessException("Já existe um animal com esse brinco neste lote.");
            }
            if (text.contains("lot")) {
                throw new DataAccessException("Já existe um lote com esse código neste rebanho.");
            }
            return new ValidationException("Registro já existente.");
        }
        return new DataAccessException(message, e);
    }
}
