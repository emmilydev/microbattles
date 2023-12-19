package online.nasgar.microbattles.plugin.listener.user;

import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.dist.RemoteModelService;
import online.nasgar.microbattles.api.event.user.UserLeaveMatchEvent;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.match.RejoinQueueEntry;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.plugin.scoreboard.MatchScoreboardProvider;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;

public class UserLeaveMatchListener implements Listener {
  private final MatchManager matchManager;
  private final MessageHandler messageHandler;
  private final RemoteModelService<RejoinQueueEntry> rejoinQueueService;
  private final RemoteModelService<Match> matchService;

  @Inject
  public UserLeaveMatchListener(MatchManager matchManager,
                                MessageHandler messageHandler,
                                RemoteModelService<RejoinQueueEntry> rejoinQueueService,
                                RemoteModelService<Match> matchService) {
    this.matchManager = matchManager;
    this.messageHandler = messageHandler;
    this.rejoinQueueService = rejoinQueueService;
    this.matchService = matchService;
  }

  @EventHandler
  public void onUserLeaveMatch(UserLeaveMatchEvent event) {
    User user = event.getUser();
    Match match = event.getMatch();
    boolean cancelled = match.getPhase() == Match.Phase.STARTING;

    if (cancelled) {
      match.setPhase(Match.Phase.WAITING);
      match.setStartDate(0);
    }

    DyeColor currentTeam = user.getCurrentTeam();

    if (currentTeam == null) {
      match.getSpectators().remove(user.getId());
    } else {
      match.getTeams().get(currentTeam).remove(user.getId());
    }

    Player leftPlayer = user.player();

    if (event.getCause() != UserLeaveMatchEvent.Cause.DISCONNECT) {
      // Send to lobby
    } else {
      rejoinQueueService.save(new RejoinQueueEntry(user.getId(), match.getId()));
    }

    for (Player player : matchManager.getPlayers(match, true)) {
      if (cancelled) {
        messageHandler.sendReplacingIn(
          player,
          MessageMode.ERROR,
          "match.cancelled"
        );
      } else {
        messageHandler.sendReplacingIn(
          player,
          MessageMode.INFO,
          "match.player-left",
          "%player%", leftPlayer.getName()
        );
      }
    }

    matchService.save(match);
  }
}
