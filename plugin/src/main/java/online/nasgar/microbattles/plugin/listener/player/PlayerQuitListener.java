package online.nasgar.microbattles.plugin.listener.player;

import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.dist.RemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.event.user.UserLeaveMatchEvent;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.plugin.MicroBattlesPlugin;
import online.nasgar.microbattles.plugin.scoreboard.MatchScoreboardProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.inject.Inject;

public class PlayerQuitListener implements Listener {
  private final CachedRemoteModelService<User> userService;
  private final MatchScoreboardProvider matchScoreboardProvider;
  private final RemoteModelService<Match> matchService;
  private final MicroBattlesPlugin plugin;

  @Inject
  public PlayerQuitListener(CachedRemoteModelService<User> userService,
                            MatchScoreboardProvider matchScoreboardProvider,
                            RemoteModelService<Match> matchService,
                            MicroBattlesPlugin plugin) {
    this.userService = userService;
    this.matchScoreboardProvider = matchScoreboardProvider;
    this.matchService = matchService;
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();

    userService
      .get(player.getUniqueId().toString())
      .whenComplete((user, error) -> {
        if (error != null) {
          ErrorNotifier.notify(error);

          return;
        }

        if (user.getCurrentMatch() != null) {
          Match match = matchService.findSync(user.getCurrentMatch());

          if (match != null) {
            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new UserLeaveMatchEvent(
              user,
              match,
              UserLeaveMatchEvent.Cause.DISCONNECT
            )));
          }
        }

        matchScoreboardProvider.remove(user);

        userService.uploadSync(user);
      });
  }
}
