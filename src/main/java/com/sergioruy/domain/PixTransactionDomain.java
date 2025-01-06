package com.sergioruy.domain;

import com.sergioruy.model.PixTransaction;
import com.sergioruy.model.enumeration.StatusPix;
import com.sergioruy.model.records.PixKey;
import com.sergioruy.model.records.Typableline;
import com.sergioruy.repository.PixTransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.Optional;

@ApplicationScoped
public class PixTransactionDomain {

    private final PixTransactionRepository pixTransactionRepository;

    public PixTransactionDomain(PixTransactionRepository pixTransactionRepository) {
        this.pixTransactionRepository = pixTransactionRepository;
    }

    @Transactional
    public void addPixTransaction(final Typableline typableline, final BigDecimal amount, final PixKey pixKey) {
        pixTransactionRepository.addPixTransaction(typableline, amount, pixKey);
    }

    public Optional<PixTransaction> approveTransaction(final String uuid) {
        try {
            return pixTransactionRepository.changeTransactionStatus(uuid, StatusPix.APPROVED);
        } finally {
            // initProcess(uuid);
        }
    }

    public Optional<PixTransaction> rejectPixTransaction(final String uuid) {
        return pixTransactionRepository.changeTransactionStatus(uuid, StatusPix.REJECTED);
    }

    public Optional<PixTransaction> initProcess(final String uuid) {
        return pixTransactionRepository.changeTransactionStatus(uuid, StatusPix.IN_PROCESS);
    }

    public Optional<PixTransaction> findById(final String uuid) {
        Optional<Document> optionalDocument = pixTransactionRepository.findOne(uuid);
        return optionalDocument.map(PixTransactionConverterApply::apply);
    }
}
