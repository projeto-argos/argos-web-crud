package br.com.argos.util;

import java.util.regex.Pattern;

public class Validador {

    private static final Pattern PADRAO_CPF = Pattern.compile("^\\d{11}$");
    private static final Pattern PADRAO_TELEFONE = Pattern.compile("^\\d{10,11}$");
    private static final Pattern PADRAO_EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    // ================== FORMATO (REGEX) ==================

    public static boolean telefoneValido(String telefone) {
        if (telefone == null) {
            return false;
        }
        String telefoneLimpo = telefone.replaceAll("[^0-9]", "");
        return PADRAO_TELEFONE.matcher(telefoneLimpo).matches();
    }

    public static boolean emailValido(String email) {
        if (email == null) {
            return false;
        }
        return PADRAO_EMAIL.matcher(email).matches();
    }

    // ================== CPF (FORMATO + DÍGITO VERIFICADOR) ==================

    public static boolean cpfValido(String cpf) {
        if (cpf == null) {
            return false;
        }

        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        if (!PADRAO_CPF.matcher(cpfLimpo).matches()) {
            return false;
        }

        if (todosDigitosIguais(cpfLimpo)) {
            return false;
        }

        int primeiroDigito = calcularDigitoCpf(cpfLimpo, 9);
        int segundoDigito = calcularDigitoCpf(cpfLimpo, 10);

        int digitoInformado1 = Character.getNumericValue(cpfLimpo.charAt(9));
        int digitoInformado2 = Character.getNumericValue(cpfLimpo.charAt(10));

        return primeiroDigito == digitoInformado1 && segundoDigito == digitoInformado2;
    }

    private static int calcularDigitoCpf(String cpf, int quantidadeDigitos) {
        int soma = 0;
        int peso = quantidadeDigitos + 1;

        for (int i = 0; i < quantidadeDigitos; i++) {
            int numero = Character.getNumericValue(cpf.charAt(i));
            soma += numero * peso;
            peso--;
        }

        int resto = (soma * 10) % 11;
        return (resto == 10) ? 0 : resto;
    }

    private static boolean todosDigitosIguais(String cpf) {
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                return false;
            }
        }
        return true;
    }
}