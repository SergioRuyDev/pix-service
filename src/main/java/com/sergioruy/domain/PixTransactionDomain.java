package com.sergioruy.domain;

import com.sergioruy.model.PixTransaction;
import com.sergioruy.model.enumeration.StatusPix;
import com.sergioruy.model.records.PixKey;
import com.sergioruy.model.records.Typableline;
import com.sergioruy.repository.PixTransactionPanacheRepository;
import com.sergioruy.repository.PixTransactionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PixTransactionDomain {

//    private final PixTransactionRepository pixTransactionRepository;
//
//    public PixTransactionDomain(PixTransactionRepository pixTransactionRepository) {
//        this.pixTransactionRepository = pixTransactionRepository;
//    }

    private final PixTransactionPanacheRepository pixTransactionRepository;

    public PixTransactionDomain(PixTransactionPanacheRepository pixTransactionRepository) {
        this.pixTransactionRepository = pixTransactionRepository;
    }

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

    public List<PixTransaction> findPixTransactionByDate(final Date InitDate, final Date EndDate) {
        return pixTransactionRepository.findPixTransactions(InitDate, EndDate);
    }

    public Optional<PixTransaction> rejectPixTransaction(final String uuid) {
        return pixTransactionRepository.changeTransactionStatus(uuid, StatusPix.REJECTED);
    }

    public Optional<PixTransaction> initProcess(final String uuid) {
        return pixTransactionRepository.changeTransactionStatus(uuid, StatusPix.IN_PROCESS);
    }

    public Optional<PixTransaction> findById(final String uuid) {
        return pixTransactionRepository.findOne(uuid);
    }
}
