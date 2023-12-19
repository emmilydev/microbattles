package online.nasgar.microbattles.api.event.arena;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaRequestEvent extends Event {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  private final String arena;

  public ArenaRequestEvent(String arena) {
    this.arena = arena;
  }

  @Override
  public HandlerList getHandlers() {
    return null;
  }
}
