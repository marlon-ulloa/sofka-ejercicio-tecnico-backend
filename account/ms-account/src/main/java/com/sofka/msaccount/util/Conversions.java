package com.sofka.msaccount.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Conversions {

    public BigDecimal toNegative(BigDecimal value){
        return value.negate();
    }
}
