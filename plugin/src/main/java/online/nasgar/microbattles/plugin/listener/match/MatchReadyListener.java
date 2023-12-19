package online.nasgar.microbattles.plugin.listener.match;

import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.dist.RemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.event.match.MatchReadyEvent;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.engine.MicroBattlesEngine;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.plugin.MicroBattlesPlugin;
import online.nasgar.microbattles.plugin.scoreboard.MatchScoreboardProvider;
import online.nasgar.microbattles.plugin.scoreboard.WaitingScoreboardProvider;
import online.nasgar.microbattles.plugin.task.ArenaBreakerTask;
import online.nasgar.microbattles.plugin.task.MatchCountdownTask;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;

public class MatchReadyListener implements Listener {
  private final MatchManager matchManager;
  private final MessageHandler messageHandler;
  private final MicroBattlesPlugin plugin;
  private final MicroBattlesEngine engine;
  private final RemoteModelService<Match> matchService;
  private final MatchScoreboardProvider matchScoreboardProvider;
  private final WaitingScoreboardProvider waitingScoreboardProvider;

  @Inject
  public MatchReadyListener(MatchManager matchManager,
                            MessageHandler messageHandler,
                            MicroBattlesPlugin plugin,
                            MicroBattlesEngine engine,
                            RemoteModelService<Match> matchService,
                            MatchScoreboardProvider matchScoreboardProvider,
                            WaitingScoreboardProvider waitingScoreboardProvider) {
    this.matchManager = matchManager;
    this.messageHandler = messageHandler;
    this.plugin = plugin;
    this.engine = engine;
    this.matchService = matchService;
    this.matchScoreboardProvider = matchScoreboardProvider;
    this.waitingScoreboardProvider = waitingScoreboardProvider;
  }

  @EventHandler
  public void onMatchReady(MatchReadyEvent event) {
    Match match = event.getMatch();
    match.setPhase(Match.Phase.STARTING);
    match.setCountdownStartDate(System.currentTimeMillis());

    matchManager
      .getArena(match)
      .whenComplete((arena, error) -> {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new ArenaBreakerTask(match, arena), 0, 40);
        if (error != null) {
          ErrorNotifier.notify(error);

          for (User user : matchManager.getUsersSync(match)) {
            waitingScoreboardProvider.remove(user);

            messageHandler.sendIn(
              user,
              MessageMode.ERROR,
              "match.error.starting"
            );
          }

          matchManager.cancel(
            match,
            null,
            5
          );

          return;
        }

        if (arena == null) {
          for (User user : matchManager.getUsersSync(match)) {
            waitingScoreboardProvider.remove(user);

            messageHandler.sendIn(
              user,
              MessageMode.ERROR,
              "match.error.arena-not-found"
            );
          }

          matchManager.cancel(
            match,
            null,
            5
          );

          return;
        }

        matchManager.notifyUsersAnd(
          match,
          user -> {
            waitingScoreboardProvider.remove(user);
            matchScoreboardProvider.apply(user, match);

            if (match.getSpectators().contains(user.getId())) {
              user.player().teleport(arena.getSpectatorSpawnpoint().toLocation());
            } else {
              user.player().teleport(arena.getSpawnpoints().get(user.getCurrentTeam()).toLocation());
            }
          },
          () -> {
            match.setCountdownId(Bukkit.getScheduler().scheduleSyncRepeatingTask(
              plugin,
              new MatchCountdownTask(match, messageHandler, matchManager, engine),
              0,
              20
            ));

            matchService.saveSync(match);
          },
          true,
          MessageMode.INFO,
          "match.ready"
        );
      });
  }
}
