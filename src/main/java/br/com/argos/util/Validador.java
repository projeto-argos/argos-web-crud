package br.com.argos.util;

import java.util.regex.Pattern;

public class Validador {

    private static final Pattern PADRAO_CPF = Pattern.compile("^\\d{11}$");
    private static final Pattern PADRAO_TELEFONE = Pattern.compile("^\\d{10,11}$");
    private static final Pattern PADRAO_EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PADRAO_CNPJ = Pattern.compile("^\\d{14}$");

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

    private static boolean todosDigitosIguais(String texto) {
        for (int i = 1; i < texto.length(); i++) {
            if (texto.charAt(i) != texto.charAt(0)) {
                return false;
            }
        }
        return true;
    }

    // ================== CNPJ (FORMATO + DÍGITO VERIFICADOR) ==================

    public static boolean cnpjValido(String cnpj) {
        if (cnpj == null) {
            return false;
        }

        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");

        if (!PADRAO_CNPJ.matcher(cnpjLimpo).matches()) {
            return false;
        }

        if (todosDigitosIguais(cnpjLimpo)) {
            return false;
        }

        // Os pesos do CNPJ seguem um padrão específico que se repete e diminui
        int[] pesosPrimeiroDigito = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesosSegundoDigito = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int primeiroDigito = calcularDigitoCnpj(cnpjLimpo, pesosPrimeiroDigito);
        int segundoDigito = calcularDigitoCnpj(cnpjLimpo, pesosSegundoDigito);

        // O 13º e 14º caracteres (índices 12 e 13) são os dígitos informados
        int digitoInformado1 = Character.getNumericValue(cnpjLimpo.charAt(12));
        int digitoInformado2 = Character.getNumericValue(cnpjLimpo.charAt(13));

        return primeiroDigito == digitoInformado1 && segundoDigito == digitoInformado2;
    }

    private static int calcularDigitoCnpj(String cnpj, int[] pesos) {
        int soma = 0;

        for (int i = 0; i < pesos.length; i++) {
            int numero = Character.getNumericValue(cnpj.charAt(i));
            soma += numero * pesos[i];
        }

        int resto = soma % 11;
        return (resto < 2) ? 0 : (11 - resto);
    }
}