package online.nasgar.microbattles.api.model.point;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.beans.ConstructorProperties;

public class CoordinatePoint {
  public static final CoordinatePoint DUMMY = new CoordinatePoint(
    "",
    0,
    0,
    0,
    0,
    0
  );

  private final String world;
  private final double x;
  private final double y;
  private final double z;
  private final float yaw;
  private final float pitch;

  @ConstructorProperties({
    "world",
    "x",
    "y",
    "z",
    "yaw",
    "pitch"
  })
  public CoordinatePoint(String world,
                         double x,
                         double y,
                         double z,
                         float yaw,
                         float pitch) {
    this.world = world;
    this.x = x;
    this.y = y;
    this.z = z;
    this.yaw = yaw;
    this.pitch = pitch;
  }

  public CoordinatePoint(Location location) {
    this(
      location.getWorld().getName(),
      location.getX(),
      location.getY(),
      location.getZ(),
      location.getYaw(),
      location.getPitch()
    );
  }

  public String getWorld() {
    return world;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public double getZ() {
    return z;
  }

  public float getYaw() {
    return yaw;
  }

  public float getPitch() {
    return pitch;
  }

  public CoordinatePoint add(double x,
                             double y,
                             double z,
                             float yaw,
                             float pitch) {
    return new CoordinatePoint(
      world,
      this.x + x,
      this.y + y,
      this.z + z,
      this.yaw + yaw,
      this.pitch + pitch
    );
  }

  public CoordinatePoint rest(double x,
                              double y,
                              double z,
                              float yaw,
                              float pitch) {
    return new CoordinatePoint(
      world,
      this.x - x,
      this.y - y,
      this.y - y,
      this.yaw - yaw,
      this.pitch - pitch
    );
  }

  public Location toLocation() {
    return new Location(
      Bukkit.getWorld(world),
      x,
      y,
      z,
      yaw,
      pitch
    );
  }

  @Override
  public String toString() {
    return new StringBuilder()
      .append(world)
      .append(" - ")
      .append(x)
      .append(" - ")
      .append(y)
      .append(" - ")
      .append(z)
      .append(" - ")
      .append(yaw)
      .append(" - ")
      .append(pitch)
      .toString();
  }

}