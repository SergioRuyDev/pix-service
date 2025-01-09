package com.sergioruy.domain;

import com.sergioruy.model.PixTransaction;
import com.sergioruy.model.enumeration.StatusPix;
import com.sergioruy.model.records.PixKey;
import com.sergioruy.model.records.Typableline;
import com.sergioruy.repository.PixTransactionPanacheRepository;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.subscription.MultiEmitter;
import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PixTransactionDomain {

    private final PixTransactionPanacheRepository pixTransactionRepository;
    private final Emitter<PixTransaction> transactionEmitter;

    public PixTransactionDomain(PixTransactionPanacheRepository pixTransactionRepository,
                                @Channel("pix") Emitter<PixTransaction> transactionEmitter) {
        this.pixTransactionRepository = pixTransactionRepository;
        this.transactionEmitter = transactionEmitter;
    }

    public void addPixTransaction(final Typableline typableline, final BigDecimal amount, final PixKey pixKey) {
        pixTransactionRepository.addPixTransaction(typableline, amount, pixKey);
    }

    public Optional<PixTransaction> approveTransaction(final String uuid) {
        Optional<PixTransaction> optTransaction = pixTransactionRepository.changeTransactionStatus(uuid, StatusPix.APPROVED);
        optTransaction.ifPresent(this::sendTransaction);
        return optTransaction;
    }

    private void sendTransaction(final PixTransaction transaction) {
        Log.infof("Sending a new transaction: %s", transaction);

        transactionEmitter.send(
                Message.of(transaction).addMetadata(OutgoingKafkaRecordMetadata.<String>builder()
                        .withKey(transaction.getId())
                        .withHeaders(new RecordHeaders().add("x-line", transaction.getLine().getBytes(StandardCharsets.UTF_8)))
                        .build()));

//        transactionEmitter.send(transaction); this is a simple approach, I did a better way
    }


    public List<PixTransaction> findPixTransactionByDate(final Date InitDate, final Date EndDate) {
        return pixTransactionRepository.findPixTransactions(InitDate, EndDate);
    }

    public Optional<PixTransaction> rejectPixTransaction(final String uuid) {
        return pixTransactionRepository.changeTransactionStatus(uuid, StatusPix.REJECTED);
    }

    public Optional<PixTransaction> findById(final String uuid) {
        return pixTransactionRepository.findOne(uuid);
    }
}
