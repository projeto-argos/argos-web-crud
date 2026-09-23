package br.com.argos.util;

import java.util.regex.Pattern;

public class Validador {

    private static final Pattern PADRAO_CPF = Pattern.compile("^\\d{11}$");
    private static final Pattern PADRAO_TELEFONE = Pattern.compile("^\\d{10,11}$");
    private static final Pattern PADRAO_EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PADRAO_CNPJ = Pattern.compile("^\\d{14}$");
    private static final Pattern PADRAO_USUARIO = Pattern.compile("^[\\w.]{5,20}$");

    // ================== PESOS CNPJ ==================
    private static final int[] PESOS_CNPJ_PRIMEIRO =
            {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private static final int[] PESOS_CNPJ_SEGUNDO =
            {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

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

    public static boolean usuarioValido(String usuario){
        if (usuario == null) {
            return false;
        }
        return PADRAO_USUARIO.matcher(usuario).matches();
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

        if (todosDigitosIguaisCpf(cpfLimpo)) {
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

    private static boolean todosDigitosIguaisCpf(String cpf) {
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                return false;
            }
        }
        return true;
    }

    // ================== CNPJ (FORMATO + DÍGITO VERIFICADOR) ==================

    public static boolean cnpjValido(String cnpj){
        if(cnpj == null){
            return false;
        }

        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");

         if (!PADRAO_CNPJ.matcher(cnpjLimpo).matches()){
            return false;
        }

         if(todosDigitosIguaisCnpj(cnpjLimpo)) {
             return false;
         }

        int primeiroDigito = calcularDigitoCnpj(cnpjLimpo, 12, PESOS_CNPJ_PRIMEIRO);
         String cnpjParaSegundoDigito = cnpjLimpo.substring(0,12) + primeiroDigito;

        int segundoDigito = calcularDigitoCnpj(cnpjParaSegundoDigito, 13, PESOS_CNPJ_SEGUNDO);

        int digitoInformado1 = Character.getNumericValue(cnpjLimpo.charAt(12));
        int digitoInformado2 = Character.getNumericValue(cnpjLimpo.charAt(13));

        return primeiroDigito == digitoInformado1 && segundoDigito == digitoInformado2;

    }

    private static int calcularDigitoCnpj(String cnpj, int quantidadeDigitos, int[] pesos) {
        int soma = 0;

        for (int i = 0; i < quantidadeDigitos; i++) {
            int numero = Character.getNumericValue(cnpj.charAt(i));
            soma += numero * pesos[i];
        }

        int resto = soma % 11;
        return (resto < 2) ? 0 : (11 - resto);
    }

    private static boolean todosDigitosIguaisCnpj(String cnpj) {
        for (int i = 1; i < cnpj.length(); i++) {
            if (cnpj.charAt(i) != cnpj.charAt(0)) {
                return false;
            }
        }
        return true;
    }
}