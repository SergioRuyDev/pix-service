package com.sergioruy.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.sergioruy.domain.PixTransactionConverterApply;
import com.sergioruy.model.PixTransaction;
import com.sergioruy.model.enumeration.StatusPix;
import com.sergioruy.model.records.PixKey;
import com.sergioruy.model.records.Typableline;
import org.bson.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class PixTransactionMongoRepositoryImpl implements PixTransactionRepository {

    public static final String AMERICA_SAO_PAULO = "America/Sao_Paulo";

    private final MongoClient mongoClient;

    public PixTransactionMongoRepositoryImpl(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    public void addPixTransaction(Typableline typableline, BigDecimal value, PixKey pixKey) {

        var document = new Document();
        document.append(PixTransactionConverterApply.ID, typableline.uuid())
                .append(PixTransactionConverterApply.VALUE, value)
                .append(PixTransactionConverterApply.KEY_TYPE, pixKey.keyType())
                .append(PixTransactionConverterApply.KEY, pixKey)
                .append(PixTransactionConverterApply.LINE, typableline.line())
                .append(PixTransactionConverterApply.DATE, LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO)
                ));
        getCollection().insertOne(document);
    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("pix").getCollection("pix_transaction");
    }

    @Override
    public Optional<PixTransaction> changeTransactionStatus(String uuid, StatusPix statusPix) {
        return Optional.empty();
    }

    @Override
    public Optional<Document> findOne(String uuid) {
        return Optional.empty();
    }
}
