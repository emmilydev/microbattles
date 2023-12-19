package online.nasgar.microbattles.plugin.listener.user;

import net.cosmogrp.storage.dist.RemoteModelService;
import online.nasgar.microbattles.api.event.user.SpectatorJoinMatchEvent;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.plugin.scoreboard.MatchScoreboardProvider;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;

public class SpectatorJoinMatchListener implements Listener {
  private final MatchManager matchManager;
  private final MatchScoreboardProvider matchScoreboardProvider;
  private final RemoteModelService<Match> matchService;

  @Inject
  public SpectatorJoinMatchListener(MatchManager matchManager,
                                    MatchScoreboardProvider matchScoreboardProvider,
                                    RemoteModelService<Match> matchService) {
    this.matchManager = matchManager;
    this.matchScoreboardProvider = matchScoreboardProvider;
    this.matchService = matchService;
  }

  @EventHandler
  public void onSpectatorJoin(SpectatorJoinMatchEvent event) {
    User user = event.getUser();
    Match match = event.getMatch();

    matchScoreboardProvider.apply(user, match);

    Player spectator = user.player();
    spectator.setGameMode(GameMode.SPECTATOR);

    match.getSpectators().add(user.getId());

    matchManager.notify(
      match,
      MessageMode.ACTION_BAR,
      "match.spectator-joined",
      "%player%", spectator.getName()
    );

    matchService.save(match);
  }
}
