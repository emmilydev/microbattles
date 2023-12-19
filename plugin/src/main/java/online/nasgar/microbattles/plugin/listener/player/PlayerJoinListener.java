package online.nasgar.microbattles.plugin.listener.player;

import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.dist.RemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.event.user.SpectatorJoinMatchEvent;
import online.nasgar.microbattles.api.event.user.UserJoinMatchEvent;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.plugin.MicroBattlesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;

public class PlayerJoinListener implements Listener {
  private final CachedRemoteModelService<User> userService;
  private final MatchManager matchManager;
  private final MessageHandler messageHandler;
  private final MicroBattlesPlugin plugin;

  @Inject
  public PlayerJoinListener(CachedRemoteModelService<User> userService,
                            MatchManager matchManager,
                            MessageHandler messageHandler,
                            MicroBattlesPlugin plugin) {
    this.userService = userService;
    this.matchManager = matchManager;
    this.messageHandler = messageHandler;
    this.plugin = plugin;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();

    userService
      .getOrFind(player.getUniqueId().toString())
      .whenComplete((user, error) -> {
        if (error != null) {
          ErrorNotifier.notify(error);

          return;
        }

        if (user == null) {
          user = new User(player.getUniqueId().toString());
          userService.saveInCache(user);
        }

        if (!matchManager.playing()) {
          messageHandler.sendReplacingIn(
            player,
            MessageMode.ERROR,
            "match.not-found"
          );
          // Send to the lobby
        } else {
          Match match = matchManager.getCurrentMatchSync();
          Event eventToCall;

          if (match.getPhase() != Match.Phase.WAITING) {
            eventToCall = new SpectatorJoinMatchEvent(user, match);
          } else {
            eventToCall = new UserJoinMatchEvent(user, match);
          }

          Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(eventToCall));
        }
      });
  }

}
