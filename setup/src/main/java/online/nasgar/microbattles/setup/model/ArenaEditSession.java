package online.nasgar.microbattles.setup.model;

import online.nasgar.microbattles.api.model.Model;
import online.nasgar.microbattles.api.model.cuboid.Cuboid;
import online.nasgar.microbattles.api.model.point.CoordinatePoint;

import java.util.ArrayList;
import java.util.List;

public class ArenaEditSession implements Model {
  private final String id;
  private final String arena;
  private final List<Cuboid> barriers;
  private final List<CoordinatePoint> rawPoints;

  public ArenaEditSession(String id,
                          String arena) {
    this.id = id;
    this.arena = arena;
    this.barriers = new ArrayList<>();
    this.rawPoints = new ArrayList<>();
  }

  @Override
  public String getId() {
    return id;
  }

  public String getArena() {
    return arena;
  }

  public List<Cuboid> getBarriers() {
    return barriers;
  }

  public List<CoordinatePoint> getRawPoints() {
    return rawPoints;
  }

  public boolean addPoint(CoordinatePoint point) {
    if (rawPoints.size() % 2 == 0) {
      rawPoints.add(point);

      return false;
    } else {
      CoordinatePoint latest = rawPoints.get(rawPoints.size() - 1);
      barriers.add(new Cuboid(latest, point));

      return true;
    }
  }

  public void end() {
    rawPoints.clear();
    barriers.clear();
  }
}
