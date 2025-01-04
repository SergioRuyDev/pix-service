package com.sergioruy.domain;

import com.sergioruy.model.records.PixKey;
import com.sergioruy.model.records.Typableline;
import com.sergioruy.repository.PixTransactionRepository;

import java.math.BigDecimal;

public class PixTransactionDomain {

    private final PixTransactionRepository pixTransactionRepository;

    public PixTransactionDomain(PixTransactionRepository pixTransactionRepository) {
        this.pixTransactionRepository = pixTransactionRepository;
    }

    public void addPixTransaction(final Typableline typableline, final BigDecimal amount, final PixKey pixKey) {
        pixTransactionRepository.addPixTransaction(typableline, amount, pixKey);
    }
}
