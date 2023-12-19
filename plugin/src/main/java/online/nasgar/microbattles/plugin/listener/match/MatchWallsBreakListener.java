package online.nasgar.microbattles.plugin.listener.match;

import com.cryptomorin.xseries.XSound;
import me.yushust.message.MessageHandler;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.event.match.MatchWallsBreakEvent;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.cuboid.Cuboid;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.sound.SoundPlayer;
import online.nasgar.microbattles.plugin.MicroBattlesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;

public class MatchWallsBreakListener implements Listener {
  private final MatchManager matchManager;
  private final MicroBattlesPlugin plugin;
  private final MessageHandler messageHandler;

  @Inject
  public MatchWallsBreakListener(MatchManager matchManager,
                                 MicroBattlesPlugin plugin,
                                 MessageHandler messageHandler) {
    this.matchManager = matchManager;
    this.plugin = plugin;
    this.messageHandler = messageHandler;
  }

  @EventHandler
  public void onMatchWallsBreak(MatchWallsBreakEvent event) {
    Match match = event.getMatch();

    matchManager
      .getArena(match)
      .whenComplete((arena, error) -> {
        if (error != null) {
          ErrorNotifier.notify(error);

          return;
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
          for (Cuboid barrier : arena.getBarriers()) {
            for (Block block : barrier) {
              if (block.getType().name().contains("GLASS")) {
                block.setType(Material.AIR);
              }
            }
          }
        });


        match.setPhase(Match.Phase.PLAYING);

        for (Player player : matchManager.getPlayers(match)) {
          SoundPlayer.playNormal(player, XSound.ENTITY_PLAYER_LEVELUP);

          messageHandler.sendIn(
            player,
            MessageMode.TITLE,
            "match.walls-broken.title"
          );

          messageHandler.sendIn(
            player,
            MessageMode.SUBTITLE,
            "match.walls-broken.subtitle"
          );
        }
      });
  }
}
