package online.nasgar.microbattles.api.event.arena;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ArenaBarrierEditSessionEndEvent extends PlayerEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public ArenaBarrierEditSessionEndEvent(Player who) {
    super(who);
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
