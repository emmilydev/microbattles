package online.nasgar.microbattles.setup.command.arena;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.arena.Arena;
import online.nasgar.microbattles.api.model.point.CoordinatePoint;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Command(
  names = {"teams", "team"},
  permission = "microbattles.setup.arena"
)
public class ArenaTeamsCommand implements CommandClass {
  private final MessageHandler messageHandler;
  private final CachedRemoteModelService<Arena> arenaService;

  @Inject
  public ArenaTeamsCommand(MessageHandler messageHandler,
                           CachedRemoteModelService<Arena> arenaService) {
    this.messageHandler = messageHandler;
    this.arenaService = arenaService;
  }

  @Command(
    names = ""
  )
  public void runArenaTeamsCommand(@Sender Player player) {
    messageHandler.sendReplacingIn(
      player,
      MessageMode.INFO,
      "arena.teams.help"
    );
  }

  @Command(
    names = {"add", "put", "new"}
  )
  public void runArenaTeamsAddCommand(@Sender Player player,
                                      @Named("arena") CompletableFuture<Arena> arenaFuture,
                                      DyeColor team) {
    arenaFuture.whenComplete((arena, error) -> {
      if (error != null) {
        ErrorNotifier.notify(error);

        messageHandler.sendReplacingIn(
          player,
          MessageMode.ERROR,
          "command.error"
        );

        return;
      }

      if (arena == null) {
        messageHandler.sendReplacingIn(
          player,
          MessageMode.ERROR,
          "arena.not-found"
        );

        return;
      }

      String teamKey = team.name().replace("_", "-").toLowerCase();

      if (arena.getTeams().contains(team)) {
        messageHandler.sendReplacingIn(
          player,
          MessageMode.ERROR,
          "arena.teams.already-exists",
          "%arena%", "%path_" + arena.getTranslationKey() + "%",
          "%team%", "%path_arena.team." + teamKey + "%"
        );

        return;
      }

      arena.getTeams().add(team);

      arena.getSpawnpoints().put(team, new CoordinatePoint(player.getLocation()));

      messageHandler.sendReplacingIn(
        player,
        MessageMode.SUCCESS,
        "arena.teams.added",
        "%arena%", "%path_" + arena.getTranslationKey() + "%",
        "%team%", "%path_arena.team." + teamKey + "%"
      );

      arenaService.saveSync(arena);
    });
  }

  @Command(
    names = {"remove", "delete"}
  )
  public void runArenaTeamsRemoveCommand(@Sender Player player,
                                         @Named("arena") CompletableFuture<Arena> arenaFuture,
                                         DyeColor team) {
    arenaFuture.whenComplete((arena, error) -> {
      if (error != null) {
        ErrorNotifier.notify(error);

        messageHandler.sendReplacingIn(
          player,
          MessageMode.ERROR,
          "command.error"
        );

        return;
      }

      if (arena == null) {
        messageHandler.sendReplacingIn(
          player,
          MessageMode.ERROR,
          "arena.not-found"
        );

        return;
      }

      String teamKey = team.name().replace("_", "-").toLowerCase();

      if (!arena.getTeams().contains(team)) {
        messageHandler.sendReplacingIn(
          player,
          MessageMode.ERROR,
          "arena.teams.not-found",
          "%arena%", "%path_" + arena.getTranslationKey() + "%",
          "%team%", "%path_arena.team." + teamKey + "%"
        );

        return;
      }

      arena.getTeams().remove(team);

      arena.getSpawnpoints().remove(team);

      messageHandler.sendReplacingIn(
        player,
        MessageMode.SUCCESS,
        "arena.teams.removed",
        "%arena%", "%path_" + arena.getTranslationKey() + "%",
        "%team%", "%path_arena.team." + teamKey + "%"
      );

      arenaService.saveSync(arena);
    });
  }

  @Command(
    names = "clear"
  )
  public void runArenaTeamsClearCommand(@Sender Player player,
                                        @Named("arena") CompletableFuture<Arena> arenaFuture) {
    arenaFuture.whenComplete((arena, error) -> {
      if (error != null) {
        ErrorNotifier.notify(error);

        messageHandler.sendReplacingIn(
          player,
          MessageMode.ERROR,
          "command.error"
        );

        return;
      }

      if (arena == null) {
        messageHandler.sendReplacingIn(
          player,
          MessageMode.ERROR,
          "arena.not-found"
        );

        return;
      }

      arena.getTeams().clear();

      messageHandler.sendReplacingIn(
        player,
        MessageMode.SUCCESS,
        "arena.teams.cleared",
        "%arena%", "%path_" + arena.getTranslationKey() + "%"
      );

      arenaService.saveSync(arena);

    });
  }
}
