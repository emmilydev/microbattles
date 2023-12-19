package online.nasgar.microbattles.commons.bind;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import me.yushust.inject.AbstractModule;
import me.yushust.inject.key.TypeReference;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.dist.LocalModelService;
import net.cosmogrp.storage.dist.RemoteModelService;
import net.cosmogrp.storage.redis.channel.Channel;
import net.cosmogrp.storage.redis.connection.Redis;
import online.nasgar.microbattles.api.message.server.ServerStateMessage;
import online.nasgar.microbattles.api.model.Model;
import online.nasgar.microbattles.api.model.arena.Arena;
import online.nasgar.microbattles.api.model.engine.MicroBattlesEngine;
import online.nasgar.microbattles.api.model.kit.Kit;
import online.nasgar.microbattles.api.model.kit.item.ClickableItem;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.match.RejoinQueueEntry;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.api.storage.StorageType;
import online.nasgar.microbattles.commons.engine.MicroBattlesEngineLoader;
import online.nasgar.microbattles.commons.mongo.InjectableMongoModelService;
import online.nasgar.microbattles.commons.redis.messaging.JacksonRedis;
import online.nasgar.microbattles.commons.redis.storage.JacksonRedisModelService;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ModelModule extends AbstractModule {
  private final JavaPlugin plugin;

  public ModelModule(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
      ObjectMapper objectMapper = new ObjectMapper();
      bind(ObjectMapper.class).toInstance(objectMapper);

      Executor executor = Executors.newCachedThreadPool();
      bind(Executor.class).toInstance(executor);

      MicroBattlesEngine engine = new MicroBattlesEngineLoader(objectMapper).loadEngine(plugin);

      bind(MicroBattlesEngine.class).toInstance(engine);

      StringBuilder uri = new StringBuilder();
      Map<String, Object> credentials = engine.getMongoCredentials();

      if (credentials.containsKey("scheme")) {
        uri.append((String) credentials.get("scheme"));
      } else {
        uri.append("mongodb");
      }

      uri.append("://");

      if (credentials.containsKey("auth") && (boolean) credentials.get("auth")) {
        if (credentials.containsKey("username") && credentials.containsKey("password")) {
          uri
            .append((String) credentials.get("username"))
            .append(":")
            .append((String) credentials.get("password"))
            .append("@");
        } else {
          plugin.getLogger().warning("Your MongoDB credentials specify that auth is enabled, but no username " +
            "nor password was provided");
        }
      }

      uri.append((String) credentials.get("host"));

      if (credentials.containsKey("port")) {
        uri
          .append(":")
          .append((int) credentials.get("port"));
      }

      bind(MongoClient.class).toInstance(MongoClients.create(uri.toString()));

      uri.setLength(0);
      credentials = engine.getRedisCredentials();

      if (credentials.containsKey("scheme")) {
        uri.append((String) credentials.get("scheme"));
      } else {
        uri.append("redis");
      }

      uri.append("://");

      if (credentials.containsKey("auth") && (boolean) credentials.get("auth")) {
        uri
          .append((String) credentials.getOrDefault("username", ""))
          .append(":")
          .append((String) credentials.getOrDefault("password", ""))
          .append("@");
      }

      uri.append((String) credentials.get("host"));

      if (credentials.containsKey("port")) {
        uri
          .append(":")
          .append((int) credentials.get("port"));
      }


      JedisPool jedisPool = new JedisPool(uri.toString());

      uri.setLength(0);

      bind(JedisPool.class).toInstance(jedisPool);

      Redis redis = new JacksonRedis(
        "microbattles",
        engine.getServiceName(),
        executor,
        objectMapper,
        jedisPool,
        jedisPool.getResource()
      );

      bind(Redis.class).toInstance(redis);
      bind(TypeReference.of(Channel.class, ServerStateMessage.class)).toInstance(
        redis.getMessenger().getChannel("server-state", ServerStateMessage.class)
      );

      bindModel(
        User.class,
        StorageType.LOCAL,
        StorageType.MONGO
      );
      bindModel(
        Match.class,
        StorageType.REDIS
      );
      bindModel(
        Arena.class,
        StorageType.LOCAL,
        StorageType.MONGO
      );
      bindModel(
        Kit.class,
        StorageType.LOCAL,
        StorageType.MONGO
      );
      bindModel(
        RejoinQueueEntry.class,
        StorageType.REDIS
      );
      bindModel(
        ClickableItem.class,
        StorageType.LOCAL
      );
  }

  protected <T extends Model> void bindModel(Class<T> model,
                                             StorageType... types) {
    for (StorageType type : types) {
      Class<? extends ModelService> serviceType = null;
      Class<? extends ModelService> actualType = null;

      switch (type) {
        case REDIS: {
          serviceType = RemoteModelService.class;
          actualType = JacksonRedisModelService.class;

          break;
        }
        case LOCAL: {
          serviceType = ModelService.class;
          actualType = LocalModelService.class;

          break;
        }
        case MONGO: {
          serviceType = CachedRemoteModelService.class;
          actualType = InjectableMongoModelService.class;

          break;
        }
      }

      bind(TypeReference.of(serviceType, model))
        .to(TypeReference.of(actualType, model))
        .singleton();
    }
  }
}

