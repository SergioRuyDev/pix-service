package com.sergioruy.config;

import com.sergioruy.model.PixKey;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

@ApplicationScoped
public class RedisCache {

    private final ValueCommands<String, PixKey> commands;

    public RedisCache(RedisDataSource redisDataSource) {
        this.commands = redisDataSource.value(PixKey.class);
    }

    public PixKey get(String key) {
        return commands.get(key);
    }

    public void set(String key, PixKey cached) {
        commands.set(key, cached, new SetArgs().ex(Duration.ofMinutes(30)));
    }

    public void evict(String key) {
        commands.getdel(key);
    }

    public PixKey getOrSetIfAbsent(String key, Supplier<PixKey> cachedObj) {

        var cashed = get(key);
        if (Objects.nonNull(cashed)) {
            return cashed;
        } else {
            var result = cachedObj.get();
            set(key, result);
            return result;
        }
    }
}
