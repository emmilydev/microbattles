package online.nasgar.microbattles.api.event.user;

import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import org.bukkit.event.HandlerList;

public class UserLeaveMatchEvent extends UserEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  private final Match match;
  private final Cause cause;

  public UserLeaveMatchEvent(User user,
                             Match match,
                             Cause cause) {
    super(user);
    this.match = match;
    this.cause = cause;
  }

  public Match getMatch() {
    return match;
  }

  public Cause getCause() {
    return cause;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public enum Cause {
    DISCONNECT,
    QUIT
  }

}
