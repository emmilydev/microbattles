package online.nasgar.microbattles.api.event.match;

import online.nasgar.microbattles.api.model.match.Match;
import org.bukkit.DyeColor;
import org.bukkit.event.HandlerList;

/**
 * Event triggered whenever a {@link Match}
 * ends, whether successfully or not.
 */
public class MatchEndEvent extends MatchEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  private final DyeColor winner;
  private final boolean successfully;

  public MatchEndEvent(Match match,
                       DyeColor winner,
                       boolean successfully) {
    super(match);
    this.winner = winner;
    this.successfully = successfully;
  }

  public DyeColor getWinner() {
    return winner;
  }

  public boolean isSuccessfully() {
    return successfully;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }
}
