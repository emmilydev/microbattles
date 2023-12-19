package online.nasgar.microbattles.api.model.match;

import online.nasgar.microbattles.api.model.Model;
import online.nasgar.microbattles.api.model.arena.ArenaBlockFallConfiguration;
import online.nasgar.microbattles.api.model.match.settings.WorldTime;
import org.bukkit.DyeColor;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Match implements Model {
  private final String id;
  private final String arena;
  private final Map<DyeColor, Collection<String>> teams;
  private final Set<String> spectators;
  private Phase phase;
  private long startDate;
  private long countdownStartDate;
  private int countdownId;
  private int breakerId;
  private int breakerStart;
  private ArenaBlockFallConfiguration fallingBlocksConfiguration;
  private final Collection<String> rejoinQueue;
  private WorldTime worldTime;
  private boolean friendlyFire;

  @ConstructorProperties({
    "id",
    "arena",
    "teams",
    "startDate",
    "fallingBlocksConfiguration"
  })
  public Match(String id,
               String arena,
               Map<DyeColor, Collection<String>> teams,
               long startDate,
               ArenaBlockFallConfiguration fallingBlocksConfiguration) {
    this.id = id;
    this.arena = arena;
    this.teams = teams;
    this.spectators = new HashSet<>();
    this.startDate = startDate;
    this.fallingBlocksConfiguration = fallingBlocksConfiguration;
    this.rejoinQueue = new HashSet<>();
    this.phase = Phase.WAITING;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getArena() {
    return arena;
  }

  public Map<DyeColor, Collection<String>> getTeams() {
    return teams;
  }

  public Set<String> getSpectators() {
    return spectators;
  }

  public Phase getPhase() {
    return phase;
  }

  public void setPhase(Phase phase) {
    this.phase = phase;
  }

  public long getStartDate() {
    return startDate;
  }

  public void setStartDate(long startDate) {
    this.startDate = startDate;
  }

  public long getCountdownStartDate() {
    return countdownStartDate;
  }

  public void setCountdownStartDate(long countdownStartDate) {
    this.countdownStartDate = countdownStartDate;
  }

  public int getCountdownId() {
    return countdownId;
  }

  public void setCountdownId(int countdownId) {
    this.countdownId = countdownId;
  }

  public int getBreakerId() {
    return breakerId;
  }

  public void setBreakerId(int breakerId) {
    this.breakerId = breakerId;
  }

  public int getBreakerStart() {
    return breakerStart;
  }

  public void setBreakerStart(int breakerStart) {
    this.breakerStart = breakerStart;
  }

  public ArenaBlockFallConfiguration getFallingBlocksConfiguration() {
    return fallingBlocksConfiguration;
  }

  public void setFallingBlocksConfiguration(ArenaBlockFallConfiguration fallingBlocksConfiguration) {
    this.fallingBlocksConfiguration = fallingBlocksConfiguration;
  }

  public Collection<String> getRejoinQueue() {
    return rejoinQueue;
  }

  public WorldTime getWorldTime() {
    return worldTime;
  }

  public void setWorldTime(WorldTime worldTime) {
    this.worldTime = worldTime;
  }

  public boolean isFriendlyFire() {
    return friendlyFire;
  }

  public void setFriendlyFire(boolean friendlyFire) {
    this.friendlyFire = friendlyFire;
  }

  @Override
  public String toString() {
    return "Match{" +
      "id='" + id + '\'' +
      ", arena='" + arena + '\'' +
      ", teams=" + teams +
      ", spectators=" + spectators +
      ", phase=" + phase +
      ", startDate=" + startDate +
      ", countdownStartDate=" + countdownStartDate +
      ", countdownId=" + countdownId +
      ", fallingBlocksConfiguration=" + fallingBlocksConfiguration +
      ", rejoinQueue=" + rejoinQueue +
      '}';
  }

  public enum Phase {
    WAITING,
    STARTING,
    PLAYING_WITH_WALLS,
    PLAYING;
  }
}
