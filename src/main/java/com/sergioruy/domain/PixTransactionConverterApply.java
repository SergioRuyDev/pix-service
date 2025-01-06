package com.sergioruy.domain;

import com.sergioruy.model.PixTransaction;
import com.sergioruy.model.enumeration.StatusPix;
import org.bson.Document;
import org.bson.types.Decimal128;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;

import static com.sergioruy.repository.PixTransactionMongoRepositoryImpl.AMERICA_SAO_PAULO;

public class PixTransactionConverterApply {

    public static final String ID = "_id";
    public static final String VALUE = "value";
    public static final String STATUS = "status";
    public static final String LINE = "line";
    public static final String KEY = "key";
    public static final String KEY_TYPE = "keyType";
    public static final String DATE = "pixDate";

    public static PixTransaction apply(final Document document) {
        var transaction = new PixTransaction();
        transaction.setId(document.getString(ID));
        transaction.setValue(BigDecimal.valueOf(document.get(VALUE,
                Decimal128.class).doubleValue()));
        transaction.setStatus(StatusPix.valueOf(document.getString(STATUS)));
        transaction.setLine(document.getString(LINE));
        transaction.setKey(document.getString(KEY));
        transaction.setKeyType(document.getString(KEY_TYPE));
        transaction.setPixDate(document.get(DATE, Date.class).toInstant()
                .atZone(ZoneId.of(AMERICA_SAO_PAULO)).toLocalDateTime()
        );
        return transaction;
    }

}
