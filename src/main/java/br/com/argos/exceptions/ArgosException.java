package br.com.argos.exceptions;

public class ArgosException extends RuntimeException {
    public ArgosException(String message) {
        super(message);
    }

    public ArgosException(String message, Throwable cause) {
        super(message, cause);
    }
}
