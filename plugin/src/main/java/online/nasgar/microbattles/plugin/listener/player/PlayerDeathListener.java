package online.nasgar.microbattles.plugin.listener.player;

import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.dist.RemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.inject.Inject;

public class PlayerDeathListener implements Listener {
  private final MessageHandler messageHandler;
  private final CachedRemoteModelService<User> userService;
  private final RemoteModelService<Match> matchService;
  private final MatchManager matchManager;

  @Inject
  public PlayerDeathListener(MessageHandler messageHandler,
                             CachedRemoteModelService<User> userService,
                             RemoteModelService<Match> matchService,
                             MatchManager matchManager) {
    this.messageHandler = messageHandler;
    this.userService = userService;
    this.matchService = matchService;
    this.matchManager = matchManager;
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player player = event.getEntity();
    Player playerKiller = player.getKiller();
    EntityDamageEvent.DamageCause cause = player.getLastDamageCause().getCause();

    if (playerKiller == null) {
      return;
    }

    userService
      .getOrFind(player.getUniqueId().toString())
      .whenComplete((user, error) -> {
        if (error != null) {
          ErrorNotifier.notify(error);
        }

        User killer = userService.getOrFindSync(playerKiller.getUniqueId().toString());

        if (killer == null || user == null) {
          return;
        }

        if (killer.getCurrentMatch() == null || user.getCurrentMatch() == null) {
          return;
        }

        killer.addKills();
        user.addDeaths();

        Match match = matchManager.getMatchSync(killer.getCurrentMatch());

        if (match == null) {
          return;
        }

        // Bukkit.getPluginManager().callEvent(new );
      });
  }
}
