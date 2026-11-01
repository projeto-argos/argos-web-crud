package br.com.argos.util;

import br.com.argos.exceptions.DataAccessException;
import br.com.argos.exceptions.ValidationException;

import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.Map;

/**
 * Traduz erros do PostgreSQL em exceções que o usuário entende.
 * A mensagem é escolhida pelo NOME da constraint violada (e não pelo texto do erro,
 * que inclui o valor digitado e poderia confundir).
 */
public final class DbErrors {

    private static final String CHAVE_DUPLICADA = "23505";

    // nome da constraint (schema.sql) -> mensagem para o usuário
    private static final Map<String, String> DUPLICADOS = Map.of(
            "uk_admin_cpf", "CPF já registrado.",
            "uk_user_cpf", "CPF já registrado.",
            "uk_admin_email", "Email já registrado.",
            "uk_user_email", "Email já registrado.",
            "uk_animal_eartag_lot", "Já existe um animal com esse brinco neste lote.",
            "uk_lot_code_herd", "Já existe um lote com esse código neste rebanho.",
            "uk_property_address", "Esse endereço já está em uso por outra propriedade."
    );

    private DbErrors() {
    }

    /** Sempre use: throw DbErrors.translate("mensagem", e); */
    public static RuntimeException translate(String message, SQLException e) {
        if (CHAVE_DUPLICADA.equals(e.getSQLState())) {
            String mensagem = DUPLICADOS.get(constraintName(e));
            // erro causado pelo usuário (valor repetido): ValidationException
            return new ValidationException(mensagem != null ? mensagem : "Registro já existente.");
        }
        return new DataAccessException(message, e);
    }

    private static String constraintName(SQLException e) {
        if (e instanceof PSQLException psql && psql.getServerErrorMessage() != null) {
            String name = psql.getServerErrorMessage().getConstraint();
            return name == null ? "" : name.toLowerCase();
        }
        return "";
    }
}
