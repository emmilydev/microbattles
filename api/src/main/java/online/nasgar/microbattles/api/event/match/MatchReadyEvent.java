package online.nasgar.microbattles.api.event.match;

import online.nasgar.microbattles.api.model.match.Match;
import org.bukkit.event.HandlerList;

/**
 * Event triggered whenever a {@link Match}
 * starts in the current running server.
 */
public class MatchReadyEvent extends MatchEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public MatchReadyEvent(Match match) {
    super(match);
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

}
