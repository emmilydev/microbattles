package online.nasgar.microbattles.api.model.arena;

import online.nasgar.microbattles.api.model.Model;
import online.nasgar.microbattles.api.model.cuboid.Cuboid;
import online.nasgar.microbattles.api.model.point.CoordinatePoint;
import online.nasgar.microbattles.api.model.translation.TranslatableModel;
import org.bukkit.DyeColor;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Arena implements Model, TranslatableModel {
  private final String id;
  private String translationKey;
  private int maxPlayers;
  private int minPlayers;
  private Set<DyeColor> teams;
  private Map<DyeColor, CoordinatePoint> spawnpoints;
  private int radius;
  private Cuboid bounds;
  private List<Cuboid> barriers;
  private CoordinatePoint waitIsland;
  private CoordinatePoint spectatorSpawnpoint;
  private MatchType matchType;

  @ConstructorProperties({
    "id",
    "translationKey",
    "maxPlayers",
    "minPlayers",
    "teams",
    "spawnpoints",
    "radius",
    "bounds",
    "barriers",
    "waitIsland",
    "spectatorSpawnpoint",
    "matchType"
  })
  public Arena(String id,
               String translationKey,
               int maxPlayers,
               int minPlayers,
               Set<DyeColor> teams,
               Map<DyeColor, CoordinatePoint> spawnpoints,
               int radius,
               Cuboid bounds,
               List<Cuboid> barriers,
               CoordinatePoint waitIsland,
               CoordinatePoint spectatorSpawnpoint,
               MatchType matchType) {
    this.id = id;
    this.translationKey = translationKey;
    this.maxPlayers = maxPlayers;
    this.minPlayers = minPlayers;
    this.teams = teams;
    this.spawnpoints = spawnpoints;
    this.radius = radius;
    this.bounds = bounds;
    this.barriers = barriers;
    this.waitIsland = waitIsland;
    this.spectatorSpawnpoint = spectatorSpawnpoint;
    this.matchType = matchType;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getTranslationKey() {
    return translationKey;
  }

  public void setTranslationKey(String translationKey) {
    this.translationKey = translationKey;
  }

  public int getMaxPlayers() {
    return maxPlayers;
  }

  public void setMaxPlayers(int maxPlayers) {
    this.maxPlayers = maxPlayers;
  }

  public int getMinPlayers() {
    return minPlayers;
  }

  public void setMinPlayers(int minPlayers) {
    this.minPlayers = minPlayers;
  }

  public Set<DyeColor> getTeams() {
    return teams;
  }

  public void setTeams(Set<DyeColor> teams) {
    this.teams = teams;
  }

  public Map<DyeColor, CoordinatePoint> getSpawnpoints() {
    return spawnpoints;
  }

  public void setSpawnpoints(Map<DyeColor, CoordinatePoint> spawnpoints) {
    this.spawnpoints = spawnpoints;
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  public Cuboid getBounds() {
    return bounds;
  }

  public void setBounds(Cuboid bounds) {
    this.bounds = bounds;
  }

  public List<Cuboid> getBarriers() {
    return barriers;
  }

  public void setBarriers(List<Cuboid> barriers) {
    this.barriers = barriers;
  }

  public CoordinatePoint getWaitIsland() {
    return waitIsland;
  }

  public void setWaitIsland(CoordinatePoint waitIsland) {
    this.waitIsland = waitIsland;
  }

  public CoordinatePoint getSpectatorSpawnpoint() {
    return spectatorSpawnpoint;
  }

  public void setSpectatorSpawnpoint(CoordinatePoint spectatorSpawnpoint) {
    this.spectatorSpawnpoint = spectatorSpawnpoint;
  }

  public MatchType getMatchType() {
    return matchType;
  }

  public void setMatchType(MatchType matchType) {
    this.matchType = matchType;
  }

  public enum MatchType {
    SOLO(1),
    DUO(2),
    SQUAD(4);

    private final int maxPlayersPerTeam;

    MatchType(int maxPlayersPerTeam) {
      this.maxPlayersPerTeam = maxPlayersPerTeam;
    }

    public int getMaxPlayersPerTeam() {
      return maxPlayersPerTeam;
    }
  }
}
