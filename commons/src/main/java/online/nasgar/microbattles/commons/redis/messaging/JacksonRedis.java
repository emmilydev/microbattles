package online.nasgar.microbattles.commons.redis.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.cosmogrp.storage.redis.connection.Redis;
import net.cosmogrp.storage.redis.messenger.Messenger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executor;

public class JacksonRedis implements Redis {
  private final JedisPool jedisPool;
  private final Jedis listenerConnection;

  private Messenger messenger;

  public JacksonRedis(
    String parentChannel, String serverId,
    Executor executor, ObjectMapper objectMapper,
    JedisPool jedisPool,
    Jedis listenerConnection
  ) {
    this.jedisPool = jedisPool;
    this.listenerConnection = listenerConnection;

    this.messenger = new JacksonRedisMessenger(
      parentChannel, serverId, executor,
      objectMapper, jedisPool, listenerConnection
    );
  }

  @Override
  public Messenger getMessenger() {
    return messenger;
  }

  @Override
  public JedisPool getRawConnection() {
    return jedisPool;
  }

  @Override
  public Jedis getListenerConnection() {
    return listenerConnection;
  }

  @Override
  public void close() throws IOException {
    messenger.close();
    messenger = null;
    jedisPool.close();
    listenerConnection.close();
  }

}
