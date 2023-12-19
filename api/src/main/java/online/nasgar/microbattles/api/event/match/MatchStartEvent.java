package online.nasgar.microbattles.api.event.match;

import online.nasgar.microbattles.api.model.match.Match;
import org.bukkit.event.HandlerList;

public class MatchStartEvent extends MatchEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public MatchStartEvent(Match match) {
    super(match);
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }


}
