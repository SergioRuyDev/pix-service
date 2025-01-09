package com.sergioruy.service;

import com.sergioruy.config.RedisCache;
import com.sergioruy.model.records.CreatePixTypableLineRequest;
import com.sergioruy.model.records.PixKey;
import com.sergioruy.model.enumeration.KeyType;
import com.sergioruy.model.enumeration.PersonType;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.util.Objects;

@ApplicationScoped
public class DictService {
    private final String key;
    private final String ispb;
    private final String document;
    private final String name;
    private final RedisCache redisCache;

    public DictService(
            @ConfigProperty(name = "pix.key") String key,
            @ConfigProperty(name = "pix.ispb") String ispb,
            @ConfigProperty(name = "pix.document") String document,
            @ConfigProperty(name = "pix.name") String name,
            RedisCache redisCache
    ) {
        this.key = key;
        this.ispb = ispb;
        this.document = document;
        this.name = name;
        this.redisCache = redisCache;
    }

    public PixKey findPixKeyDetailsOnCache(String key) {
        var pixKey = findCachedPixKey(key);
        if (Objects.isNull(pixKey)) { // if null will return pix key from property
            var fakePixKey = buildPixKeyByProperties(key);
            redisCache.set(key, buildPixKeyByProperties(key));
            return fakePixKey;
        }

        return pixKey;
    }

    private PixKey findCachedPixKey(String key) {
        var pixKey = redisCache.get(key);
        Log.infof("Found pix key on cache: %s", pixKey);
        return pixKey;
    }

    public PixKey buildPixKeyByRequest(CreatePixTypableLineRequest request) {
        KeyType keyType = determineKeyType(request.key());
        PersonType personType = determinePersonType(request.document());

        return new PixKey(
                keyType,
                request.key(),
                request.ispb(),
                personType,
                request.document(),
                request.name(),
                LocalDateTime.now()
        );
    }

    public PixKey buildPixKeyByProperties(String key){
        return new PixKey(
                KeyType.DOCUMENT,
                key,
                ispb,
                PersonType.NATURAL_PERSON,
                document,
                name,
                LocalDateTime.now()
        );
    }

    private KeyType determineKeyType(String key) {
        if (key.contains("@")) {
            return KeyType.EMAIL;
        } else if (key.length() == 14) {
            return KeyType.DOCUMENT;
        } else if (key.length() == 11 && !key.startsWith("+")) {
            return KeyType.DOCUMENT;
        } else if (key.startsWith("+")) {
            return KeyType.PHONE;
        } else {
            throw new IllegalArgumentException("Invalid key format: " + key);
        }
    }

    private PersonType determinePersonType(String document) {
        if (document.length() == 14) {
            return PersonType.LEGAL_PERSON;
        } else {
            return PersonType.NATURAL_PERSON;
        }
    }
}
