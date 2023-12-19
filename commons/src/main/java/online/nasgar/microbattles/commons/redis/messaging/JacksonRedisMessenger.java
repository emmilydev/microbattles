package online.nasgar.microbattles.commons.redis.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import net.cosmogrp.storage.redis.channel.Channel;
import net.cosmogrp.storage.redis.messenger.Messenger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class JacksonRedisMessenger implements Messenger {
  private final String parentChannel;
  private final String serverId;
  private final ObjectMapper objectMapper;

  private final JedisPool messengerPool;

  private final Map<String, JacksonRedisChannel<?>> channels;

  private final JedisPubSub pubSub;

  public JacksonRedisMessenger(
    String parentChannel, String serverId,
    Executor executor, ObjectMapper objectMapper,
    JedisPool messengerPool,
    Jedis listenerConnection
  ) {
    this.parentChannel = parentChannel;
    this.serverId = serverId;
    this.objectMapper = objectMapper;
    this.messengerPool = messengerPool;

    this.channels = new ConcurrentHashMap<>();
    pubSub = new JacksonRedisPubSub(parentChannel, serverId, objectMapper, channels);

    executor.execute(() ->
      listenerConnection.subscribe(
        pubSub, parentChannel
      ));
  }

  @Override
  public <T> Channel<T> getChannel(String name, TypeToken<T> type) {
    @SuppressWarnings("unchecked")
    JacksonRedisChannel<T> channel = (JacksonRedisChannel<T>) channels.get(name);

    if (channel == null) {
      channel = new JacksonRedisChannel<>(
        parentChannel, serverId, name, type,
        this, messengerPool, objectMapper
      );

      channels.put(name, channel);
    } else {
      if (!channel.getType().equals(type)) {
        throw new IllegalArgumentException(
          "Channel type mismatch"
        );
      }
    }

    return channel;
  }

  @Override
  public void close() {
    channels.clear();

    if (pubSub.isSubscribed()) {
      pubSub.unsubscribe();
    }
  }

}
