package online.nasgar.microbattles.api.event.match;

import online.nasgar.microbattles.api.model.match.Match;
import org.bukkit.event.HandlerList;

public class MatchFallingBlocksConfigurationUpgradeEvent extends MatchEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  private final int next;

  public MatchFallingBlocksConfigurationUpgradeEvent(Match match,
                                                     int next) {
    super(match);
    this.next = next;
  }

  public int getNext() {
    return next;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

}
