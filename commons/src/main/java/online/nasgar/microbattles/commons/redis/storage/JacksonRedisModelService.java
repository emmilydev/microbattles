package online.nasgar.microbattles.commons.redis.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.yushust.inject.key.TypeReference;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.RemoteModelService;
import net.cosmogrp.storage.redis.connection.RedisCache;
import online.nasgar.microbattles.api.model.Model;
import online.nasgar.microbattles.api.model.engine.MicroBattlesEngine;
import redis.clients.jedis.JedisPool;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class JacksonRedisModelService<T extends Model> extends RemoteModelService<T> {
  private final ObjectMapper objectMapper;
  private final Class<T> type;
  private final RedisCache redisCache;
  private final String tableName;
  private final int expireAfterSave;

  @Inject
  public JacksonRedisModelService(
    Executor executor,
    TypeReference<T> type,
    ObjectMapper objectMapper,
    MicroBattlesEngine engine,
    JedisPool jedisPool
  ) {
    super(executor);
    this.objectMapper = objectMapper;
    this.type = type.getRawType();
    this.redisCache = new RedisCache(
      (String) engine.getRedisCredentials().getOrDefault("cachePrefix", "MicroBattles"),
      jedisPool
    );
    this.tableName = this.type.getSimpleName();
    this.expireAfterSave = 600;
  }

  @Override
  public void saveSync(T model) {
    try {
      redisCache.set(
        tableName, model.getId(),
        objectMapper.writeValueAsString(model),
        expireAfterSave
      );
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteSync(T model) {
    redisCache.del(tableName, model.getId());
  }

  @Override
  public T findSync(String id) {
    String json = redisCache.get(tableName, id);

    if (json == null) {
      return null;
    }

    try {
      return objectMapper.readValue(json, type);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<T> findSync(String field, String value) {
    if (!field.equals(ModelService.ID_FIELD)) {
      throw new IllegalArgumentException(
        "Only ID field is supported for sync find"
      );
    }

    return Collections.singletonList(findSync(value));
  }

  @Override
  public List<T> findAllSync() {
    List<String> values = redisCache.getAllValues(tableName);
    List<T> models = new ArrayList<>();

    for (String value : values) {
      try {
        T model = objectMapper.readValue(value, type);

        models.add(model);
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }

    return models;
  }
}
