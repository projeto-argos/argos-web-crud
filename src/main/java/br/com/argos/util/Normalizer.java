package br.com.argos.util;

/** Padroniza os dados de entrada antes de validar e gravar no banco. */
public class Normalizer {

    /** Mantém só os dígitos; devolve null se o texto for nulo ou vazio. */
    public static String onlyDigits(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        String digits = text.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? null : digits;
    }

    /** Tira espaços das pontas e deixa em minúsculas; null continua null. */
    public static String email(String email) {
        if (email == null) {
            return null;
        }
        return email.trim().toLowerCase();
    }
    /** Tira espaços das pontas; null continua null. */
    public static String text(String text) {
        return text == null ? null : text.trim();
    }
}