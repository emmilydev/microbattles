package online.nasgar.microbattles.commons.redis.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.reflect.TypeToken;
import net.cosmogrp.storage.redis.channel.Channel;
import net.cosmogrp.storage.redis.channel.ChannelListener;
import net.cosmogrp.storage.redis.messenger.Messenger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class JacksonRedisChannel<T> implements Channel<T> {
  private final String parentChannel;
  private final String serverId;
  private final String name;
  private final TypeToken<T> type;
  private final Messenger messenger;
  private final JedisPool jedisPool;
  private final Set<ChannelListener<T>> listeners;

  private final ObjectMapper objectMapper;

  public JacksonRedisChannel(
    String parentChannel, String serverId,
    String name, TypeToken<T> type,
    Messenger messenger,
    JedisPool jedisPool,
    ObjectMapper objectMapper
  ) {
    this.parentChannel = parentChannel;
    this.serverId = serverId;
    this.name = name;
    this.type = type;
    this.messenger = messenger;
    this.jedisPool = jedisPool;
    this.objectMapper = objectMapper;
    this.listeners = new HashSet<>();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Messenger getMessenger() {
    return messenger;
  }

  @Override
  public TypeToken<T> getType() {
    return type;
  }

  @Override
  public Channel<T> sendMessage(T message, String targetServer) {
    JsonNode objectNode = objectMapper.valueToTree(message);
    ObjectNode objectToSend = objectMapper
      .createObjectNode()
      .put("channel", name)
      .put("server", serverId);

    if (targetServer != null) {
      objectToSend.put("targetServer", targetServer);
    }

    objectToSend.putPOJO("object", objectNode);

    String json = objectToSend.toString();

    try (Jedis jedis = jedisPool.getResource()) {
      jedis.publish(parentChannel, json);
    }

    return this;
  }

  @Override
  public Channel<T> addListener(ChannelListener<T> channelListener) {
    listeners.add(channelListener);
    return this;
  }

  @Override
  public void listen(String server, T object) {
    for (ChannelListener<T> listener : listeners) {
      listener.listen(this, server, object);
    }
  }

  @Override
  public Set<ChannelListener<T>> getListeners() {
    return listeners;
  }
}
