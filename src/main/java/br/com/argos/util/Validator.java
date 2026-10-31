package br.com.argos.util;

import java.util.regex.Pattern;

/** Valida entrada de dados: CPF, CNPJ, e-mail, phone e senha formatados. */
public class Validator {

    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{11}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10,11}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w.+-]+@(?:[\\w-]+\\.)+[a-zA-Z]{2,}$");
    private static final Pattern CNPJ_PATTERN = Pattern.compile("^\\d{14}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[\\w.]{5,20}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^.{8,64}$");

    //  CNPJ PESOS
    private static final int[] CNPJ_FIRST_WEIGHTS =
            {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private static final int[] CNPJ_SECOND_WEIGHTS =
            {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    //  FORMATO (REGEX)

    public static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        String cleanPhone = phone.replaceAll("[^0-9]", "");
        return PHONE_PATTERN.matcher(cleanPhone).matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidUsername(String username){
        if (username == null) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }

        return PASSWORD_PATTERN.matcher(password).matches();
    }

    // CPF (FORMATO + VERIFICAÇÃO DE DIGITOS)

    public static boolean isValidCpf(String cpf) {
        if (cpf == null) {
            return false;
        }

        String cleanCpf = cpf.replaceAll("[^0-9]", "");

        if (!CPF_PATTERN.matcher(cleanCpf).matches()) {
            return false;
        }

        if (allDigitsEqualCpf(cleanCpf)) {
            return false;
        }

        int firstDigit = calculateCpfDigit(cleanCpf, 9);
        int secondDigit = calculateCpfDigit(cleanCpf, 10);

        int givenDigit1 = Character.getNumericValue(cleanCpf.charAt(9));
        int givenDigit2 = Character.getNumericValue(cleanCpf.charAt(10));

        return firstDigit == givenDigit1 && secondDigit == givenDigit2;
    }

    private static int calculateCpfDigit(String cpf, int digitCount) {
        int sum = 0;
        int weight = digitCount + 1;

        for (int i = 0; i < digitCount; i++) {
            int number = Character.getNumericValue(cpf.charAt(i));
            sum += number * weight;
            weight--;
        }

        int remainder = (sum * 10) % 11;
        return (remainder == 10) ? 0 : remainder;
    }

    private static boolean allDigitsEqualCpf(String cpf) {
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                return false;
            }
        }
        return true;
    }

    // CNPJ (FORMATO + VERIFICAÇÃO DE DIGITOS)

    public static boolean isValidCnpj(String cnpj){
        if(cnpj == null){
            return false;
        }

        String cleanCnpj = cnpj.replaceAll("[^0-9]", "");

         if (!CNPJ_PATTERN.matcher(cleanCnpj).matches()){
            return false;
        }

         if(allDigitsEqualCnpj(cleanCnpj)) {
             return false;
         }

        int firstDigit = calculateCnpjDigit(cleanCnpj, 12, CNPJ_FIRST_WEIGHTS);
         String cnpjForSecondDigit = cleanCnpj.substring(0,12) + firstDigit;

        int secondDigit = calculateCnpjDigit(cnpjForSecondDigit, 13, CNPJ_SECOND_WEIGHTS);

        int givenDigit1 = Character.getNumericValue(cleanCnpj.charAt(12));
        int givenDigit2 = Character.getNumericValue(cleanCnpj.charAt(13));

        return firstDigit == givenDigit1 && secondDigit == givenDigit2;

    }

    private static int calculateCnpjDigit(String cnpj, int digitCount, int[] weights) {
        int sum = 0;

        for (int i = 0; i < digitCount; i++) {
            int number = Character.getNumericValue(cnpj.charAt(i));
            sum += number * weights[i];
        }

        int remainder = sum % 11;
        return (remainder < 2) ? 0 : (11 - remainder);
    }

    private static boolean allDigitsEqualCnpj(String cnpj) {
        for (int i = 1; i < cnpj.length(); i++) {
            if (cnpj.charAt(i) != cnpj.charAt(0)) {
                return false;
            }
        }
        return true;
    }
}