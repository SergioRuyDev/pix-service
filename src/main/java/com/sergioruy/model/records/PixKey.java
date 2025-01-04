package com.sergioruy.model.records;

import com.sergioruy.model.enumeration.KeyType;
import com.sergioruy.model.enumeration.PersonType;

import java.time.LocalDateTime;

public record PixKey(
        KeyType keyType,
        String key,
        String ispb,
        PersonType personType,
        String document,
        String name,
        LocalDateTime creationDate
) {
}
