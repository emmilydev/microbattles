package online.nasgar.microbattles.setup.command.kit;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.kit.Kit;
import online.nasgar.microbattles.setup.menu.kit.KitEditorMenu;
import online.nasgar.microbattles.setup.menu.kit.KitsMenu;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@Command(
  names = {"kit", "kits"},
  permission = "microbattles.setup.kit"
)
public class KitCommand implements CommandClass {
  private final MessageHandler messageHandler;
  private final CachedRemoteModelService<Kit> kitService;
  private final KitEditorMenu kitEditorMenu;
  private final KitsMenu kitsMenu;

  @Inject
  public KitCommand(MessageHandler messageHandler,
                    CachedRemoteModelService<Kit> kitService,
                    KitEditorMenu kitEditorMenu,
                    KitsMenu kitsMenu) {
    this.messageHandler = messageHandler;
    this.kitService = kitService;
    this.kitEditorMenu = kitEditorMenu;
    this.kitsMenu = kitsMenu;
  }

  @Command(
    names = ""
  )
  public void runKitCommand(@Sender Player player) {
    messageHandler.sendIn(
      player,
      MessageMode.INFO,
      "kit.help"
    );
  }

  @Command(
    names = "list"
  )
  public void runKitListCommand(@Sender Player player) {
    kitsMenu.open(player);
  }

  @Command(
    names = "edit"
  )
  public void runKitEditCommand(@Sender Player player,
                                String kitId) {
    kitService
      .find(kitId)
      .whenComplete((kit, error) -> {
        if (error != null) {
          ErrorNotifier.notify(error);

          messageHandler.sendReplacingIn(
            player,
            MessageMode.ERROR,
            "kit.error"
          );

          return;
        }

        kitEditorMenu.open(player, kitId, Collections.emptyList());
      });
  }

  @Command(
    names = "delete"
  )
  public void runKitDeleteCommand(@Sender Player player,
                                  @Named("kit") CompletableFuture<Kit> kitFuture) {
    kitFuture.whenComplete((kit, error) -> {
      if (error != null) {
        ErrorNotifier.notify(error);

        messageHandler.sendReplacingIn(
          player,
          MessageMode.ERROR,
          "kit.error"
        );

        return;
      }

      if (kit == null) {
        messageHandler.sendReplacingIn(
          player,
          MessageMode.ERROR,
          "kit.not-found"
        );

        return;
      }

      kitService.deleteSync(kit);

      messageHandler.sendReplacingIn(
        player,
        MessageMode.SUCCESS,
        "kit.deleted",
        "%kit%", kit.getId()
      );
    });
  }
}
