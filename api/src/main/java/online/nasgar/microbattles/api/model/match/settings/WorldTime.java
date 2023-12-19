package online.nasgar.microbattles.api.model.match.settings;

/**
 * Represents a component of Minecraft's
 * daylight cycle. The field {@link #minecraftTime}
 * makes it easy to keep a world's time in a
 * specifi value -daylight cycle component-.
 */
public enum WorldTime {
  /**
   * Represents the morning. The time is the
   * same as the set by the {@literal /time set day}
   * command.
   */
  MORNING(1000),
  /**
   * Represents the noon, i.e., the sun is at
   * its peak. The time is the same as the
   * set by the {@literal /time set noon}
   * command.
   */
  NOON(6000),
  /**
   * Represents the afternoon, i.e., when
   * the sunset starts. The time is the same
   * as the set by the {@literal /time set sunset}
   * command.
   */
  AFTERNOON(12000),
  /**
   * Represents the night, i.e., when the
   * sun is gone. The time is the same as the
   * set by the {@literal /time set night}
   * command.
   */
  NIGHT(13000);

  private final long minecraftTime;

  WorldTime(long minecraftTime) {
    this.minecraftTime = minecraftTime;
  }

  public long getMinecraftTime() {
    return minecraftTime;
  }
}
