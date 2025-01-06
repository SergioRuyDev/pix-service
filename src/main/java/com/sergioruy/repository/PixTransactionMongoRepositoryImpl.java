package com.sergioruy.repository;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.sergioruy.domain.PixTransactionConverterApply;
import com.sergioruy.model.PixTransaction;
import com.sergioruy.model.enumeration.StatusPix;
import com.sergioruy.model.records.PixKey;
import com.sergioruy.model.records.Typableline;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
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
                .append(PixTransactionConverterApply.STATUS, StatusPix.CREATED)
                .append(PixTransactionConverterApply.DATE, LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO)
                ));
        getCollection().insertOne(document);
    }

    private MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("pix").getCollection("pix_transaction");
    }

    @Override
    public Optional<PixTransaction> changeTransactionStatus(String uuid, StatusPix statusPix) {
        Optional<Document> optionalDocument = findOne(uuid);
        if (optionalDocument.isPresent()) {
            var document = optionalDocument.get();
            var opts = new FindOneAndReplaceOptions().upsert(false).returnDocument(ReturnDocument.AFTER);
            document.merge(PixTransactionConverterApply.STATUS, statusPix, (a, b) -> b); // bi function to get the new value
            var replace = getCollection().findOneAndReplace(eq(PixTransactionConverterApply.ID, uuid),
                    document, opts);

            assert replace != null;
            return Optional.ofNullable(PixTransactionConverterApply.apply(replace));
        }
        return Optional.empty();
    }

    //Todo: improve the logical with empty optional or else and etc...
    @Override
    public Optional<Document> findOne(String uuid) {
        var filter = eq(PixTransactionConverterApply.ID, uuid);
        FindIterable<Document> documents = getCollection().find(filter);
        return StreamSupport.stream(documents.spliterator(), false).findFirst();
    }
}
