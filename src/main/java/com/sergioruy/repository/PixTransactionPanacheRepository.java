package com.sergioruy.repository;

import com.sergioruy.domain.PixTransactionConverterApply;
import com.sergioruy.model.PixTransaction;
import com.sergioruy.model.enumeration.StatusPix;
import com.sergioruy.model.records.PixKey;
import com.sergioruy.model.records.Typableline;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.sergioruy.repository.PixTransactionMongoRepositoryImpl.AMERICA_SAO_PAULO;

@ApplicationScoped
public class PixTransactionPanacheRepository implements PanacheMongoRepository<PixTransaction> {

    public List<PixTransaction> findPixTransactions(final Date dateFrom, final Date dateTo) {
        return find("date >= ?1 and date <= ?2 and status = ?3", dateFrom, dateTo, StatusPix.APPROVED)
                .stream().collect(Collectors.toList());
    }

    public void addPixTransaction(Typableline typableline, BigDecimal value, PixKey pixKey) {
        var pixTransaction = new PixTransaction();
        pixTransaction.setKey(pixKey.key());
        pixTransaction.setPixDate(LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO)));
        pixTransaction.setId(typableline.uuid());
        pixTransaction.setStatus(StatusPix.CREATED);
        pixTransaction.setValue(value);
        pixTransaction.setLine(typableline.line());
        pixTransaction.setKeyType(pixKey.keyType().toString());
        pixTransaction.persistOrUpdate();
    }

    public Optional<PixTransaction> changeTransactionStatus(String uuid, StatusPix statusPix) {
        Optional<PixTransaction> pixTransaction = findOne(uuid);

        if (pixTransaction.isPresent()) {
            var transaction = pixTransaction.get();
            transaction.setStatus(statusPix);
            transaction.update();
            return Optional.of(transaction);
        }
        return Optional.empty();
    }

    public Optional<PixTransaction> findOne(String uuid) {
        return find(PixTransactionConverterApply.ID, uuid).stream().findFirst();
    }
}
