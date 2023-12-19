package online.nasgar.microbattles.api.event.match;

import online.nasgar.microbattles.api.model.match.Match;
import org.bukkit.event.Event;

public abstract class MatchEvent extends Event {
  private final Match match;

  public MatchEvent(Match match) {
    this.match = match;
  }

  public Match getMatch() {
    return match;
  }
}
