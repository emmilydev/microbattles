package online.nasgar.microbattles.api.event.arena;

import online.nasgar.microbattles.api.model.point.CoordinatePoint;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ArenaBarrierEditEvent extends PlayerEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  private final CoordinatePoint coordinatePoint;

  public ArenaBarrierEditEvent(Player who,
                               CoordinatePoint coordinatePoint) {
    super(who);
    this.coordinatePoint = coordinatePoint;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public CoordinatePoint getCoordinatePoint() {
    return coordinatePoint;
  }
}
