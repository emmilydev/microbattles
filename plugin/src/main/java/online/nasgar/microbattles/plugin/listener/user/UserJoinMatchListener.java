package online.nasgar.microbattles.plugin.listener.user;

import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.dist.RemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.event.match.MatchReadyEvent;
import online.nasgar.microbattles.api.event.user.SpectatorJoinMatchEvent;
import online.nasgar.microbattles.api.event.user.UserJoinMatchEvent;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.plugin.MicroBattlesPlugin;
import online.nasgar.microbattles.plugin.scoreboard.MatchScoreboardProvider;
import online.nasgar.microbattles.plugin.scoreboard.WaitingScoreboardProvider;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;
import java.util.Collection;

public class UserJoinMatchListener implements Listener {
  private final MatchManager matchManager;
  private final MessageHandler messageHandler;
  private final RemoteModelService<Match> matchService;
  private final WaitingScoreboardProvider waitingScoreboardProvider;
  private final MicroBattlesPlugin plugin;

  @Inject
  public UserJoinMatchListener(MatchManager matchManager,
                               MessageHandler messageHandler,
                               RemoteModelService<Match> matchService,
                               WaitingScoreboardProvider waitingScoreboardProvider,
                               MicroBattlesPlugin plugin) {
    this.matchManager = matchManager;
    this.messageHandler = messageHandler;
    this.matchService = matchService;
    this.waitingScoreboardProvider = waitingScoreboardProvider;
    this.plugin = plugin;
  }

  @EventHandler
  public void onUserJoinMatch(UserJoinMatchEvent event) {
    User user = event.getUser();
    Match match = event.getMatch();

    matchManager
      .getArena(match)
      .whenComplete((arena, error) -> {
        if (error != null) {
          ErrorNotifier.notify(error);

          return;
        }

        boolean matchedTeam = false;

        for (DyeColor team : arena.getTeams()) {
          Collection<String> members = match.getTeams().get(team);

          if (members.size() >= arena.getMatchType().getMaxPlayersPerTeam()) {
            continue;
          }

          matchedTeam = true;

          members.add(user.getId());

          user.setCurrentMatch(match.getId());
          user.setCurrentTeam(team);

          Bukkit.getScheduler().runTask(plugin, () ->
            user.player().teleport(arena.getWaitIsland().toLocation()));

          waitingScoreboardProvider.apply(user, match);

          matchService.saveSync(match);

          break;
        }

        if (!matchedTeam) {
          messageHandler.sendReplacingIn(
            user,
            MessageMode.ERROR,
            "match.full"
          );

          Bukkit.getScheduler().runTask(plugin, () ->
            Bukkit.getPluginManager().callEvent(new SpectatorJoinMatchEvent(user, match)));
        } else {
          int currentPlayers = match.getTeams().values().size();
          int maxPlayers = arena.getMaxPlayers();

          for (Player player : matchManager.getPlayers(match)) {
            messageHandler.sendReplacingIn(
              player,
              MessageMode.INFO,
              "match.player-joined",
              "%player%", user.player().getName(),
              "%current-players%", currentPlayers,
              "%max-players%", maxPlayers
            );
          }

          if (currentPlayers >= maxPlayers) {
            Bukkit.getScheduler().runTask(plugin, () ->
              Bukkit.getPluginManager().callEvent(new MatchReadyEvent(match)));
          }
        }
      });
  }
}
