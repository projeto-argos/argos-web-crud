package br.com.argos.exceptions;

public class RequiredFieldException extends ValidationException {
    public RequiredFieldException(String field) {
        super(field, String.format("O campo %s é obrigatório", field));
    }
}