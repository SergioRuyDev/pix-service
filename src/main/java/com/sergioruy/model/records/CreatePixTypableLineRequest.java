package com.sergioruy.model.records;

import java.math.BigDecimal;

public record CreatePixTypableLineRequest(
        String key,
        String ispb,
        String document,
        String name,
        String originCity,
        BigDecimal amount
) {
}
