package online.nasgar.microbattles.api.model.achievement;

import online.nasgar.microbattles.api.model.Model;

import java.beans.ConstructorProperties;

public class Achievement implements Model {
  private final Type type;
  private final int tier;
  private final int reward;

  @ConstructorProperties({
    "type",
    "tier",
    "reward"
  })
  public Achievement(Type type,
                     int tier,
                     int reward) {
    this.type = type;
    this.tier = tier;
    this.reward = reward;
  }

  @Override
  public String getId() {
    return type.name();
  }

  public Type getType() {
    return type;
  }

  public int getTier() {
    return tier;
  }

  public int getReward() {
    return reward;
  }

  public enum Type {
    PLACED_BLOCKS,
    KILLS,
    PER_MATCH_KILLS,
    WON_MATCHES
  }

}
