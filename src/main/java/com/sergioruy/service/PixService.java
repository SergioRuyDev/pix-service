package com.sergioruy.service;

import com.sergioruy.config.TypablelineCache;
import com.sergioruy.domain.PixTransactionDomain;
import com.sergioruy.model.PixTransaction;
import com.sergioruy.model.records.PixKey;
import com.sergioruy.model.records.Typableline;
import com.sergioruy.model.qrcode.QrCode;
import com.sergioruy.model.qrcode.SendData;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@ApplicationScoped
public class PixService {

    public static final String QRCODE_PATH = "/tmp/qrcode";

    private final PixTransactionDomain pixTransactionDomain;
    private final TypablelineCache typablelineCache;

    public PixService(PixTransactionDomain pixTransactionDomain, TypablelineCache typablelineCache) {
        this.pixTransactionDomain = pixTransactionDomain;
        this.typablelineCache = typablelineCache;
    }

    public BufferedInputStream generateQrCode(final String uuid) throws IOException {

        // todo recuperar do cache
        var imagePath = QRCODE_PATH + uuid + ".png";

        try {
            return new BufferedInputStream(new FileInputStream(imagePath));
        } finally {
            Files.delete(Paths.get(imagePath));
        }
    }

    public Typableline generateTypableline(final PixKey key, BigDecimal value, String originCity) {

        var qrCode = new QrCode(new SendData(key, value, originCity));
        var uuid = UUID.randomUUID().toString();
        var imagePath = QRCODE_PATH + uuid + ".png";

        qrCode.save(Path.of(imagePath));
        String qrCodeString = qrCode.toString();
        var typableline = new Typableline(qrCodeString, uuid);
        saveTypableLine(key, value, typableline);

        return typableline;
    }

    private void saveTypableLine(PixKey key, BigDecimal value, Typableline typableline) {
        pixTransactionDomain.addPixTransaction(typableline, value, key);
        typablelineCache.set(typableline.uuid(), typableline);
    }

    public Optional<PixTransaction> findById(final String uuid) {
        return pixTransactionDomain.findById(uuid);
    }

    public Optional<PixTransaction> approvePixTransaction(final String uuid) {
        return pixTransactionDomain.approveTransaction(uuid);
    }

    private void processarPix() {

    }

    public List<PixTransaction> findPixTransactions(final Date initDate, final Date endDate) {
        return pixTransactionDomain.findPixTransactionByDate(initDate, endDate);
    }

    public Optional<PixTransaction> rejectPixTransaction(final String uuid) {
        return pixTransactionDomain.rejectPixTransaction(uuid);
    }

}
