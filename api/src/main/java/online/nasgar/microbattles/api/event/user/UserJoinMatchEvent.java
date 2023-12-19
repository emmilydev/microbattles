package online.nasgar.microbattles.api.event.user;

import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserJoinMatchEvent extends UserEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  private final Match match;
  private final boolean rejoin;

  public UserJoinMatchEvent(User user,
                            Match match,
                            boolean rejoin) {
    super(user);
    this.match = match;
    this.rejoin = rejoin;
  }

  public UserJoinMatchEvent(User user,
                            Match match) {
    this(user, match, false);
  }

  public Match getMatch() {
    return match;
  }

  public boolean isRejoin() {
    return rejoin;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
