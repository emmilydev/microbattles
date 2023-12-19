package online.nasgar.microbattles.setup.listener;

import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.ModelService;
import online.nasgar.microbattles.api.event.arena.ArenaBarrierEditSessionEndEvent;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.setup.model.ArenaEditSession;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;

public class ArenaBarrierEditSessionEndListener implements Listener {
  private final ModelService<ArenaEditSession> arenaEditSessionService;
  private final MessageHandler messageHandler;

  @Inject
  public ArenaBarrierEditSessionEndListener(ModelService<ArenaEditSession> arenaEditSessionService,
                                            MessageHandler messageHandler) {
    this.arenaEditSessionService = arenaEditSessionService;
    this.messageHandler = messageHandler;
  }

  @EventHandler
  public void onArenaBarrierEditSessionEnd(ArenaBarrierEditSessionEndEvent event) {
    Player player = event.getPlayer();
    player.getInventory().remove(Material.BLAZE_ROD);
    ArenaEditSession session = arenaEditSessionService.findSync(player.getUniqueId().toString());

    if (session == null) {
      return;
    }

    session.end();
    arenaEditSessionService.deleteSync(session.getId());

    messageHandler.sendReplacingIn(
      player,
      MessageMode.ERROR,
      "arena.barrier.session-ended"
    );
  }
}
