package online.nasgar.microbattles.api.event.user;

import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import org.bukkit.event.HandlerList;

public class SpectatorJoinMatchEvent extends UserEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  private final Match match;

  public SpectatorJoinMatchEvent(User user,
                                 Match match) {
    super(user);
    this.match = match;
  }

  public Match getMatch() {
    return match;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
