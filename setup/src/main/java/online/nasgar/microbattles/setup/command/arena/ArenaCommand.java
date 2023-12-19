package online.nasgar.microbattles.setup.command.arena;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.arena.Arena;
import online.nasgar.microbattles.api.model.cuboid.Cuboid;
import online.nasgar.microbattles.api.model.point.CoordinatePoint;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Command(
  names = "arena",
  permission = "microbattles.setup.arena"
)
@SubCommandClasses({
  //ArenaEditCommand.class,
  //ArenaTeamsCommand.class
})
@Singleton
public class ArenaCommand implements CommandClass {
  private final MessageHandler messageHandler;
  private final CachedRemoteModelService<Arena> arenaService;

  @Inject
  public ArenaCommand(MessageHandler messageHandler,
                      CachedRemoteModelService<Arena> arenaService) {
    this.messageHandler = messageHandler;
    this.arenaService = arenaService;
  }

  @Command(
    names = ""
  )
  public void runArenaCommand(@Sender Player player) {
    messageHandler.sendReplacingIn(
      player,
      MessageMode.INFO,
      "arena.help"
    );
  }

  @Command(
    names = "create"
  )
  public void runArenaCreateCommand(@Sender Player player,
                                    String id,
                                    String translationKey,
                                    int maxPlayers,
                                    int minPlayers,
                                    Arena.MatchType matchType,
                                    @OptArg("0") int radius) {
    arenaService
      .find(id)
      .whenComplete((arena, error) -> {
        if (error != null) {
          ErrorNotifier.notify(error);

          messageHandler.sendReplacingIn(
            player,
            MessageMode.ERROR,
            "arena.error"
          );

          return;
        }

        if (arena != null) {
          messageHandler.sendReplacingIn(
            player,
            MessageMode.ERROR,
            "arena.already-exists",
            "%arena%", "%path_" + arena.getTranslationKey() + "%"
          );

          return;
        }

        arena = new Arena(
          id,
          translationKey,
          maxPlayers,
          minPlayers,
          new HashSet<>(),
          new HashMap<>(),
          radius,
          Cuboid.DUMMY,
          new ArrayList<>(),
          CoordinatePoint.DUMMY,
          CoordinatePoint.DUMMY,
          matchType
        );

        messageHandler.sendReplacingIn(
          player,
          MessageMode.SUCCESS,
          "arena.created",
          "%arena%", id
        );

        arenaService.saveSync(arena);
      });
  }
}
