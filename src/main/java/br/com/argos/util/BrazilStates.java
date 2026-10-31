package br.com.argos.util;

import java.util.List;

/** Siglas dos estados brasileiros (UF) para preencher listas de seleção nas JSPs. */
public final class BrazilStates {

    private static final List<String> STATES = List.of(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA",
            "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");

    private BrazilStates() { }

    /** Lista de siglas em ordem alfabética */
    public static List<String> list() {
        return STATES;
    }
}
