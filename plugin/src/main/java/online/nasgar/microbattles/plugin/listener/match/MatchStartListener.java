package online.nasgar.microbattles.plugin.listener.match;

import online.nasgar.microbattles.api.event.match.MatchStartEvent;
import online.nasgar.microbattles.api.event.match.MatchWallsBreakEvent;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.engine.MicroBattlesEngine;
import online.nasgar.microbattles.api.model.kit.KitApplier;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.plugin.MicroBattlesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;

public class MatchStartListener implements Listener {
  private final MatchManager matchManager;
  private final MicroBattlesPlugin plugin;
  private final MicroBattlesEngine engine;
  private final KitApplier kitApplier;

  @Inject
  public MatchStartListener(MatchManager matchManager,
                            MicroBattlesPlugin plugin,
                            MicroBattlesEngine engine,
                            KitApplier kitApplier) {
    this.matchManager = matchManager;
    this.plugin = plugin;
    this.engine = engine;
    this.kitApplier = kitApplier;
  }

  @EventHandler
  public void onMatchStart(MatchStartEvent event) {
    Match match = event.getMatch();
    match.setPhase(Match.Phase.PLAYING_WITH_WALLS);
    match.setStartDate(System.currentTimeMillis());

    matchManager.notifyUsersAnd(
      match,
      user -> kitApplier.apply(user, user.getCurrentKit()),
      () -> {},
      false,
      MessageMode.TITLE,
      "match.started"
    );

    Bukkit.getScheduler().runTaskLater(
      plugin,
      () -> Bukkit.getPluginManager().callEvent(new MatchWallsBreakEvent(match)),
      engine.getWallsBreakDelay()
    );
  }
}
