package br.com.argos.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** Testes para normalização de entrada */
class NormalizerTest {

    @Test
    void onlyDigitsRemovesMask() {
        assertEquals("52998224725", Normalizer.onlyDigits("529.982.247-25"));
        assertEquals("11999999999", Normalizer.onlyDigits("(11) 99999-9999"));
    }

    @Test
    void onlyDigitsReturnsNullForBlankOrNoDigits() {
        assertNull(Normalizer.onlyDigits(null));
        assertNull(Normalizer.onlyDigits("   "));
        assertNull(Normalizer.onlyDigits("abc"));
    }

    @Test
    void emailIsTrimmedAndLowercase() {
        assertEquals("ana@email.com", Normalizer.email("  Ana@Email.COM "));
        assertNull(Normalizer.email(null));
    }

    @Test
    void textIsTrimmed() {
        assertEquals("Fazenda Boa Vista", Normalizer.text("  Fazenda Boa Vista  "));
        assertNull(Normalizer.text(null));
    }
}
