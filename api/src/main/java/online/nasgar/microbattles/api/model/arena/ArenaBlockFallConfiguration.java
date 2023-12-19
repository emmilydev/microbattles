package online.nasgar.microbattles.api.model.arena;

import java.beans.ConstructorProperties;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ArenaBlockFallConfiguration {
  private final int tier;
  private final int rate;
  private final long duration;
  private long startDate;

  @ConstructorProperties({
    "tier",
    "rate",
    "duration"
  })
  public ArenaBlockFallConfiguration(int tier,
                                     int rate,
                                     int duration) {
    this.tier = tier;
    this.rate = rate;
    this.duration = TimeUnit.SECONDS.toMillis(duration);
  }

  public int getTier() {
    return tier;
  }

  public int getRate() {
    return rate;
  }

  public long getDuration() {
    return duration;
  }

  public long getStartDate() {
    return startDate;
  }

  public void setStartDate(long startDate) {
    this.startDate = startDate;
  }

  public boolean hasEnded() {
    return System.currentTimeMillis() - startDate >= duration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ArenaBlockFallConfiguration that = (ArenaBlockFallConfiguration) o;

    return tier == that.tier && rate == that.rate && duration == that.duration;
  }

  @Override
  public int hashCode() {
    return Objects.hash(tier, rate, duration, startDate);
  }
}