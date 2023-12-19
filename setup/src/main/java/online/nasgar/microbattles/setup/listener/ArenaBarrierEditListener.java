package online.nasgar.microbattles.setup.listener;

import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.event.arena.ArenaBarrierEditEvent;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.arena.Arena;
import online.nasgar.microbattles.api.model.cuboid.Cuboid;
import online.nasgar.microbattles.api.model.point.CoordinatePoint;
import online.nasgar.microbattles.setup.MicroBattlesSetupPlugin;
import online.nasgar.microbattles.setup.model.ArenaEditSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

import javax.inject.Inject;

public class ArenaBarrierEditListener implements Listener {
  private final CachedRemoteModelService<Arena> arenaService;
  private final MessageHandler messageHandler;
  private final ModelService<ArenaEditSession> arenaEditSessionService;

  @Inject
  public ArenaBarrierEditListener(CachedRemoteModelService<Arena> arenaService,
                                  MessageHandler messageHandler,
                                  ModelService<ArenaEditSession> arenaEditSessionService) {
    this.arenaService = arenaService;
    this.messageHandler = messageHandler;
    this.arenaEditSessionService = arenaEditSessionService;
  }

  @EventHandler
  public void onArenaBarrierEdit(ArenaBarrierEditEvent event) {
    Player player = event.getPlayer();
    ArenaEditSession session = arenaEditSessionService.findSync(player.getUniqueId().toString());

    if (session == null) {
      return;
    }

    arenaService
      .find(session.getArena())
      .whenComplete((arena, error) -> {
        if (error != null) {
          ErrorNotifier.notify(error);

          messageHandler.sendReplacingIn(
            player,
            MessageMode.ERROR,
            "arena.error.edition"
          );

          return;
        }

        if (session.addPoint(event.getCoordinatePoint())) {
          arena.getBarriers().addAll(session.getBarriers());
          session.getBarriers().clear();

          arenaService.saveSync(arena);

          messageHandler.sendReplacingIn(
            player,
            MessageMode.SUCCESS,
            "arena.barrier.added"
          );
        }
      });
  }
}
