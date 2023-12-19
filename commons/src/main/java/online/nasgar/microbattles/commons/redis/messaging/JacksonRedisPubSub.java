package online.nasgar.microbattles.commons.redis.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;

public class JacksonRedisPubSub extends JedisPubSub {
  private final String parentChannel;
  private final String serverId;
  private final ObjectMapper objectMapper;
  private final Map<String, JacksonRedisChannel<?>> channels;

  public JacksonRedisPubSub(
    String parentChannel, String serverId,
    ObjectMapper objectMapper,
    Map<String, JacksonRedisChannel<?>> channels
  ) {
    this.parentChannel = parentChannel;
    this.serverId = serverId;
    this.objectMapper = objectMapper;
    this.channels = channels;
  }

  @Override
  public void onMessage(String channel, String message) {
    // we don't care if the message isn't from the parent channel
    if (!channel.equals(parentChannel)) {
      return;
    }

    // we can parse the message as a json object
    JsonNode jsonNode;
    try {
      jsonNode = objectMapper.readTree(message);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    String serverId = jsonNode.get("server").asText();

    // if the message is from the server we're listening to
    if (serverId.equals(this.serverId)) {
      return;
    }

    JsonNode targetServerElement = jsonNode.get("targetServer");

    if (targetServerElement != null) {
      String targetServer = targetServerElement.asText();

      // if the message isn't for this server, ignore it
      if (!targetServer.equals(this.serverId)) {
        return;
      }
    }

    String subChannel = jsonNode.get("channel").asText();

    @SuppressWarnings("unchecked")
    JacksonRedisChannel<Object> channelObject =
      (JacksonRedisChannel<Object>) channels.get(subChannel);

    // if the channel doesn't exist, we can't do anything
    if (channelObject == null) {
      return;
    }

    try {
      JsonNode object = jsonNode.get("object");
      Object deserializedObject = objectMapper.treeToValue(
        object,
        objectMapper.getTypeFactory().constructType(channelObject.getType().getType())
      );

      channelObject.listen(serverId, deserializedObject);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

  }
}
