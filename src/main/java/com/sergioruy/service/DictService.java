package com.sergioruy.service;

import com.sergioruy.model.PixKey;
import com.sergioruy.model.enumeration.KeyType;
import com.sergioruy.model.enumeration.PersonType;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;

@ApplicationScoped
public class DictService {
    private final String key;
    private final String ispb;
    private final String document;
    private final String name;

    public DictService(
            @ConfigProperty(name = "pix.key") String key,
            @ConfigProperty(name = "pix.ispb") String ispb,
            @ConfigProperty(name = "pix.document") String document,
            @ConfigProperty(name = "pix.name") String name
    ) {
        this.key = key;
        this.ispb = ispb;
        this.document = document;
        this.name = name;
    }

    public PixKey findKeyByLegalPersonDocument(String key) {
        return new PixKey(
                KeyType.EMAIL,
                key,
                ispb,
                PersonType.LEGAL_PERSON,
                document,
                name,
                LocalDateTime.now()
        );
    }

    public PixKey findKeyByNaturalPersonDocument(String key){
        return new PixKey(
                KeyType.EMAIL,
                key,
                ispb,
                PersonType.NATURAL_PERSON,
                document,
                name,
                LocalDateTime.now()
        );
    }
}
