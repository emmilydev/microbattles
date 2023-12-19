package online.nasgar.microbattles.api.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import online.nasgar.microbattles.api.model.Model;
import online.nasgar.microbattles.api.model.achievement.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;
import java.util.*;

public class User implements Model {
  private final String id;
  private int kills;
  private int assistedKills;
  private int deaths;
  private int wonMatches;
  private int loseMatches;
  private int elo;
  private final Map<Achievement.Type, Achievement> achievements;
  private final List<String> kits;
  private final String currentKit;
  @JsonIgnore
  private String currentMatch;
  @JsonIgnore
  private DyeColor currentTeam;

  @ConstructorProperties({
    "id",
    "kills",
    "assistedKills",
    "deaths",
    "wonMatches",
    "loseMatches",
    "elo",
    "achievements",
    "kits",
    "currentKit"
  })
  public User(String id,
              int kills,
              int assistedKills,
              int deaths,
              int wonMatches,
              int loseMatches,
              int elo,
              Map<Achievement.Type, Achievement> achievements,
              List<String> kits,
              String currentKit) {
    this.id = id;
    this.kills = kills;
    this.assistedKills = assistedKills;
    this.deaths = deaths;
    this.wonMatches = wonMatches;
    this.loseMatches = loseMatches;
    this.elo = elo;
    this.achievements = achievements;
    this.kits = kits;
    this.currentKit = currentKit;
  }

  public User(String id) {
    this.id = id;
    this.achievements = new HashMap<>();
    this.kits = new ArrayList<>();
    this.currentKit = "archer";
  }

  @Override
  public String getId() {
    return id;
  }

  public int getKills() {
    return kills;
  }

  public void setKills(int kills) {
    this.kills = kills;
  }

  public void addKills() {
    this.kills += 1;
  }

  public int getAssistedKills() {
    return assistedKills;
  }

  public void setAssistedKills(int assistedKills) {
    this.assistedKills = assistedKills;
  }

  public void addAssistedKills() {
    this.assistedKills += 1;
  }

  public int getDeaths() {
    return deaths;
  }

  public void setDeaths(int deaths) {
    this.deaths = deaths;
  }

  public void addDeaths() {
    this.deaths += 1;
  }

  public int getWonMatches() {
    return wonMatches;
  }

  public void setWonMatches(int wonMatches) {
    this.wonMatches = wonMatches;
  }

  public void addWonMatches() {
    this.wonMatches += 1;
  }

  public int getLoseMatches() {
    return loseMatches;
  }

  public void setLoseMatches(int loseMatches) {
    this.loseMatches = loseMatches;
  }

  public void addLoseMatches() {
    this.loseMatches += 1;
  }

  public int getElo() {
    return elo;
  }

  public void setElo(int elo) {
    this.elo = elo;
  }

  public Map<Achievement.Type, Achievement> getAchievements() {
    return achievements;
  }

  @JsonIgnore
  public String getCurrentMatch() {
    return currentMatch;
  }

  public void setCurrentMatch(String currentMatch) {
    this.currentMatch = currentMatch;
  }

  @JsonIgnore
  public DyeColor getCurrentTeam() {
    return currentTeam;
  }

  public void setCurrentTeam(DyeColor currentTeam) {
    this.currentTeam = currentTeam;
  }

  @JsonIgnore
  public Player player() {
    return Bukkit.getPlayer(UUID.fromString(id));
  }

  @JsonIgnore
  public OfflinePlayer offlinePlayer() {
    return Bukkit.getOfflinePlayer(UUID.fromString(id));
  }

  public List<String> getKits() {
    return kits;
  }

  public String getCurrentKit() {
    return currentKit;
  }
}
