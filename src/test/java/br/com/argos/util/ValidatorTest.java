package br.com.argos.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Testes para validação de CPF, CNPJ, e-mail, telefone e senha */
class ValidatorTest {

    // ===== CPF =====
    @Test
    void acceptsValidCpfWithOrWithoutMask() {
        assertTrue(Validator.isValidCpf("52998224725"));
        assertTrue(Validator.isValidCpf("529.982.247-25"));
    }

    @Test
    void rejectsCpfWithWrongCheckDigits() {
        assertFalse(Validator.isValidCpf("52998224726"));
    }

    @Test
    void rejectsCpfWithAllEqualDigits() {
        assertFalse(Validator.isValidCpf("11111111111"));
    }

    @Test
    void rejectsCpfWithWrongLengthOrNull() {
        assertFalse(Validator.isValidCpf("123"));
        assertFalse(Validator.isValidCpf(null));
    }

    // ===== CNPJ =====
    @Test
    void acceptsValidCnpjWithOrWithoutMask() {
        assertTrue(Validator.isValidCnpj("11222333000181"));
        assertTrue(Validator.isValidCnpj("11.222.333/0001-81"));
    }

    @Test
    void rejectsCnpjWithWrongCheckDigits() {
        assertFalse(Validator.isValidCnpj("11222333000182"));
    }

    @Test
    void rejectsCnpjWithAllEqualDigitsOrNull() {
        assertFalse(Validator.isValidCnpj("00000000000000"));
        assertFalse(Validator.isValidCnpj(null));
    }

    // ===== E-mail, phone and password =====
    @Test
    void validatesEmail() {
        assertTrue(Validator.isValidEmail("ana.silva@email.com"));
        assertFalse(Validator.isValidEmail("ana.silva@"));
        assertFalse(Validator.isValidEmail("sem-arroba.com"));
        assertFalse(Validator.isValidEmail(null));
    }

    @Test
    void validatesPhone() {
        assertTrue(Validator.isValidPhone("11999999999"));
        assertTrue(Validator.isValidPhone("(11) 3999-9999"));
        assertFalse(Validator.isValidPhone("12345"));
        assertFalse(Validator.isValidPhone(null));
    }

    @Test
    void validatesPasswordLength() {
        assertTrue(Validator.isValidPassword("senha1234"));
        assertFalse(Validator.isValidPassword("curta"));
        assertFalse(Validator.isValidPassword(null));
    }
}
