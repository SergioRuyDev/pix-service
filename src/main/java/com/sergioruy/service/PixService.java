package com.sergioruy.service;

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
import java.util.UUID;

@ApplicationScoped
public class PixService {

    public static final String QRCODE_PATH = "/tmp/qrcode";

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
        //todo implementar cache
        String qrCodeString = qrCode.toString();

        return new Typableline(qrCodeString, uuid);
    }
}
