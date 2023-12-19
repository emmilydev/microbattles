package online.nasgar.microbattles.api.event.match;

import online.nasgar.microbattles.api.model.match.Match;
import org.bukkit.event.HandlerList;

public class MatchWallsBreakEvent extends MatchEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public MatchWallsBreakEvent(Match match) {
    super(match);
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

}
