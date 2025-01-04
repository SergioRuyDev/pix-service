package com.sergioruy.model.qrcode;

import com.sergioruy.model.records.PixKey;

import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

public record SendData(PixKey key, BigDecimal value, String originCity, String description) {

    public static final int NAME_SIZE = 25;
    public static final int KEY_SIZE = 77;
    public static final int CITY_SIZE = 15;
    public static final int DESCRIPTION_SIZE = 72;
    public static final int VALUE_SIZE = 13;

    public SendData(PixKey key, BigDecimal value, String originCity) {
        this(key, value, originCity, "");
    }

    public SendData {
        if(requireNonNull(key.name()).isBlank())
            throw new IllegalArgumentException("Beneficiary name is required.");
        var beneficiaryName = key.name();
        var beneficiaryKey = key.key();
        if(key.name().length() > NAME_SIZE) {
            final var msg = "The beneficiary name cannot have more than 25 characters. '%s' have %d characters."
                    .formatted(beneficiaryName, beneficiaryName.length());
            throw new IllegalArgumentException(msg);
        }

        if(requireNonNull(beneficiaryKey).isBlank())
            throw new IllegalArgumentException("The beneficiary pix key is required.");
        beneficiaryKey = beneficiaryKey.trim();
        if(beneficiaryKey.length() > KEY_SIZE) {
            final var msg = "The beneficiary pix key cannot have more than 77 characters. '%s' have %d characters."
                    .formatted(beneficiaryKey, beneficiaryKey.length());
            throw new IllegalArgumentException(msg);
        }

        if(requireNonNull(originCity).isBlank())
            throw new IllegalArgumentException("The origin city is required.");
        originCity = originCity.trim();
        if(originCity.length() > CITY_SIZE) {
            final var msg = "The origin city cannot have more than 15 characters. '%s' have %d characters."
                    .formatted(originCity, originCity.length());
            throw new IllegalArgumentException(msg);
        }

        requireNonNull(description, "The description cannot be null, inform even a empty text.");
        description = description.trim();
        if(description.length() > DESCRIPTION_SIZE) {
            final var msg = "Description cannot have more than 77 characters. '%s' have %d characters."
                    .formatted(description, description.length());
            throw new IllegalArgumentException(msg);
        }

        if(value.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("The value of Pix should be greater than 0.");

        final var valorStr = formatNumber(value);
        if(valorStr.length() > VALUE_SIZE) {
            final var msg = "The value cannot have more than 13 characters. '%s' have %d characters."
                    .formatted(valorStr, valorStr.length());
            throw new IllegalArgumentException(msg);
        }
    }

    public String valueStr(){
        return formatNumber(value);
    }

    private static String formatNumber(final BigDecimal value){
        return String.format("%.2f", value).formatted().replace(",", ".");
    }
}
