package online.nasgar.microbattles.api.model.engine;

import online.nasgar.microbattles.api.model.arena.ArenaBlockFallConfiguration;
import online.nasgar.microbattles.api.credentials.ServerCredentials;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MicroBattlesEngine {
  private final Map<String, Object> mongoCredentials;
  private final Map<String, Object> redisCredentials;
  private final List<ArenaBlockFallConfiguration> fallingBlocksConfiguration;
  private final long wallsBreakDelay;
  private final long startCountdown;
  private final String serviceName;
  private final String arena;
  private final String defaultLanguage;

  @ConstructorProperties({
    "mongoCredentials",
    "redisCredentials",
    "fallingBlocksConfiguration",
    "wallsBreakDelay",
    "startCountdown",
    "serviceName",
    "arena",
    "defaultLanguage",
    "modelsMeta"
  })
  public MicroBattlesEngine(Map<String, Object> mongoCredentials,
                            Map<String, Object> redisCredentials,
                            List<ArenaBlockFallConfiguration> fallingBlocksConfiguration,
                            int wallsBreakDelay,
                            long startCountdown,
                            String serviceName,
                            String arena,
                            String defaultLanguage) {
    this.mongoCredentials = mongoCredentials;
    this.redisCredentials = redisCredentials;
    this.fallingBlocksConfiguration = fallingBlocksConfiguration;
    this.wallsBreakDelay = 20L * wallsBreakDelay;
    this.startCountdown = TimeUnit.SECONDS.toMillis(startCountdown);
    this.serviceName = serviceName;
    this.arena = arena;
    this.defaultLanguage = defaultLanguage;
  }

  public Map<String, Object> getMongoCredentials() {
    return mongoCredentials;
  }

  public Map<String, Object> getRedisCredentials() {
    return redisCredentials;
  }

  public List<ArenaBlockFallConfiguration> getFallingBlocksConfiguration() {
    return fallingBlocksConfiguration;
  }

  public long getWallsBreakDelay() {
    return wallsBreakDelay;
  }

  public long getStartCountdown() {
    return startCountdown;
  }

  public String getServiceName() {
    return serviceName;
  }

  public String getArena() {
    return arena;
  }

  public String getDefaultLanguage() {
    return defaultLanguage;
  }

}
