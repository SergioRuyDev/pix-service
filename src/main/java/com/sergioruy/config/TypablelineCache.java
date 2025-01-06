package com.sergioruy.config;

import com.sergioruy.model.records.Typableline;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

@ApplicationScoped
public class TypablelineCache {

    private final ValueCommands<String, Typableline> commands;

    public TypablelineCache(RedisDataSource ds) {
        this.commands = ds.value(Typableline.class);
    }

    public Typableline get(String key) {
        return commands.get(key);
    }
    public void set(String key, Typableline typableline) {
        commands.set(key, typableline, new SetArgs().ex(Duration.ofMinutes(30)));
    }

    public Typableline getOrSetIfAbsent(String key,
                                           Supplier<Typableline> cachedObj) {

        var cached = get(key);
        if (Objects.nonNull(cached)) {
            return cached;
        } else {
            var result = cachedObj.get();
            set(key, result);
            return result;
        }
    }

}
