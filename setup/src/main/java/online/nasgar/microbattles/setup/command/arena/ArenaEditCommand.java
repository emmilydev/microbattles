package online.nasgar.microbattles.setup.command.arena;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.arena.Arena;
import online.nasgar.microbattles.api.model.kit.item.ClickableItemApplier;
import online.nasgar.microbattles.api.model.point.CoordinatePoint;
import online.nasgar.microbattles.commons.command.types.BoundType;
import online.nasgar.microbattles.commons.command.types.CapacityType;
import online.nasgar.microbattles.commons.command.types.SpawnpointType;
import online.nasgar.microbattles.setup.model.ArenaEditSession;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;

@Command(
  names = {"edit", "set", "change"},
  permission = "microbattles.setup.arena"
)
@Singleton
public class ArenaEditCommand implements CommandClass {
  private final MessageHandler messageHandler;
  private final CachedRemoteModelService<Arena> arenaService;
  private final ClickableItemApplier itemStackApplier;
  private final ModelService<ArenaEditSession> arenaEditSessionService;
  @Inject
  public ArenaEditCommand(MessageHandler messageHandler,
                          CachedRemoteModelService<Arena> arenaService,
                          ClickableItemApplier itemStackApplier,
                          ModelService<ArenaEditSession> arenaEditSessionService) {
    this.messageHandler = messageHandler;
    this.arenaService = arenaService;
    this.itemStackApplier = itemStackApplier;
    this.arenaEditSessionService = arenaEditSessionService;
  }

  @Command(
    names = ""
  )
  public void runArenaEditCommand(@Sender Player player) {
    messageHandler.sendReplacingIn(
      player,
      MessageMode.INFO,
      "arena.edit.help"
    );
  }

  @Command(
    names = "capacity"
  )
  public void runArenaEditCapacityCommand(@Sender Player player,
                                          @Named("arena") CompletableFuture<Arena> arenaFuture,
                                          CapacityType capacityType,
                                          int capacity) {
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

      if (capacityType == CapacityType.MAX) {
        arena.setMaxPlayers(capacity);
      } else {
        arena.setMinPlayers(capacity);
      }

      messageHandler.sendReplacingIn(
        player,
        MessageMode.INFO,
        "arena.capacity-set",
        "%arena%", "%path_" + arena.getTranslationKey() + "%"
      );

      arenaService.saveSync(arena);
    });
  }

  @Command(
    names = "bounds"
  )
  public void runArenaEditBoundsCommand(@Sender Player player,
                                        @Named("arena") CompletableFuture<Arena> arenaFuture,
                                        BoundType boundType) {
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

      CoordinatePoint coordinates = new CoordinatePoint(player.getLocation());

      switch (boundType) {
        case LOWER: {
          arena.getBounds().setLowerBound(coordinates);

          break;
        }
        case UPPER: {
          arena.getBounds().setUpperBound(coordinates);

          break;
        }
        case BARRIER: {
          if (arenaEditSessionService.findSync(player.getUniqueId().toString()) != null) {
            messageHandler.sendReplacingIn(
              player,
              MessageMode.ERROR,
              "arena.barrier.already-in-session"
            );

            break;
          }

          arenaEditSessionService.saveSync(new ArenaEditSession(
            player.getUniqueId().toString(),
            arena.getId()
          ));

          itemStackApplier.apply(player, "barrier-editor");

          messageHandler.sendReplacingIn(
            player,
            MessageMode.SUCCESS,
            "arena.barrier.joined-session"
          );
        }
      }

      if (boundType != BoundType.BARRIER) {
        messageHandler.sendReplacingIn(
          player,
          MessageMode.SUCCESS,
          "arena.bounds.set",
          "%arena%", "%path_" + arena.getTranslationKey() + "%",
          "%type%", boundType
        );

        arenaService.saveSync(arena);
      }
    });
  }

  @Command(
    names = "spawnpoint"
  )
  public void runArenaEditSpawnpointCommand(@Sender Player player,
                                            @Named("arena") CompletableFuture<Arena> arenaFuture,
                                            SpawnpointType spawnpointType,
                                            @OptArg DyeColor team) {
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

      CoordinatePoint coordinates = new CoordinatePoint(player.getLocation());

      if (team == null) {
        if (spawnpointType == null) {
          messageHandler.sendReplacingIn(
            player,
            MessageMode.ERROR,
            "arena.spawnpoint.no-type-provided"
          );

          return;
        }

        if (spawnpointType == SpawnpointType.SPECTATOR) {
          arena.setSpectatorSpawnpoint(coordinates);
        } else {
          arena.setWaitIsland(coordinates);
        }

        messageHandler.sendReplacingIn(
          player,
          MessageMode.SUCCESS,
          "arena.spawnpoint.set",
          "%type%", spawnpointType
        );
      } else {
        if (!arena.getTeams().contains(team)) {
          messageHandler.sendReplacingIn(
            player,
            MessageMode.ERROR,
            "arena.spawnpoint.invalid-team",
            "%team%", team
          );

          return;
        }

        arena.getSpawnpoints().put(team, coordinates);

        messageHandler.sendReplacingIn(
          player,
          MessageMode.SUCCESS,
          "arena.spawnpoint.team-set",
          "%team%", team
        );
      }

      arenaService.saveSync(arena);
    });
  }

  @Command(
    names = "radius"
  )
  public void runArenaEditRadiusCommand(@Sender Player player,
                                        @Named("arena") CompletableFuture<Arena> arenaFuture,
                                        int radius) {
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

      arena.setRadius(radius);

      messageHandler.sendReplacingIn(
        player,
        MessageMode.SUCCESS,
        "arena.radius-set",
        "%arena%", "%path_" + arena.getTranslationKey() + "%",
        "%radius%", radius
      );

      arenaService.saveSync(arena);
    });
  }

  @Command(
    names = "type"
  )
  public void runArenaEditTypeCommand(@Sender Player player,
                                      @Named("arena") CompletableFuture<Arena> arenaFuture,
                                      Arena.MatchType matchType) {
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

      arena.setMatchType(matchType);

      messageHandler.sendReplacingIn(
        player,
        MessageMode.SUCCESS,
        "arena.type-set",
        "%arena%", "%path_" + arena.getTranslationKey() + "%",
        "%type%", matchType
      );

      arenaService.saveSync(arena);
    });
  }
}
