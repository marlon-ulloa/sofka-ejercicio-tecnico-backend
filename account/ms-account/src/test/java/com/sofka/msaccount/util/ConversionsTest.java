package com.sofka.msaccount.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ConversionsTest {

    private Conversions conversions;
    @BeforeEach
    public void setup() {
        conversions = new Conversions();
    }

    @Test
    public void toNegative() {
        BigDecimal value = BigDecimal.valueOf(500);
        BigDecimal expectedValue = BigDecimal.valueOf(-500);

        BigDecimal prossecedValue = conversions.toNegative(value);

        assertEquals(expectedValue, prossecedValue);
    }
}