package br.com.argos.util;

import java.text.Normalizer;

/**
 * Utilitário com as comparações usadas pelos filtros das telas.
 * Campo de filtro vazio significa "sem filtro".
 */
public final class FilterUtil {

    private FilterUtil() {}

    /** Verdadeiro se o filtro está vazio ou se o campo contém o texto (sem acento, sem maiúsculas). */
    public static boolean contains(String field, String term) {
        if (term == null || term.isBlank()) {
            return true;
        }
        if (field == null) {
            return false;
        }
        return simplify(field).contains(simplify(term));
    }

    /** Deixa só os dígitos (CPF, CNPJ, telefone). */
    public static String digits(String value) {
        return value == null ? null : value.replaceAll("\\D", "");
    }

    private static String simplify(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toLowerCase();
    }
}