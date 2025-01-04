package com.sergioruy.repository;

import com.sergioruy.model.PixTransaction;
import com.sergioruy.model.enumeration.StatusPix;
import com.sergioruy.model.records.PixKey;
import com.sergioruy.model.records.Typableline;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.Optional;

public interface PixTransactionRepository {

    void addPixTransaction(final Typableline typableline, final BigDecimal value, final PixKey pixKey);

    Optional<PixTransaction> changeTransactionStatus(final String uuid, final StatusPix statusPix);

    Optional<Document> findOne(final String uuid);
}
