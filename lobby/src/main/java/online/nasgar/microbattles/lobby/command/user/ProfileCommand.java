package online.nasgar.microbattles.lobby.command.user;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.MessageHandler;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.lobby.menu.user.ProfileMenu;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Command(
  names = "profile"
)
public class ProfileCommand implements CommandClass {
  private final ProfileMenu profileMenu;
  private final MessageHandler messageHandler;

  @Inject
  public ProfileCommand(ProfileMenu profileMenu,
                        MessageHandler messageHandler) {
    this.profileMenu = profileMenu;
    this.messageHandler = messageHandler;
  }

  @Command(
    names = {"", "help"}
  )
  public void runProfileCommand(@Sender Player player) {
    messageHandler.sendIn(
      player,
      MessageMode.INFO,
      "command.profile.help"
    );
  }

  @Command(
    names = "profile"
  )
  public void runProfileCommand(@Sender Player player,
                                @Sender CompletableFuture<User> userFuture) {
    userFuture.whenComplete((user, error) -> {
      if (error != null) {
        ErrorNotifier.notify(error);

        messageHandler.sendIn(
          player,
          MessageMode.ERROR,
          "profile.error.opening-menu"
        );

        return;
      }

      if (user == null) {
        messageHandler.sendIn(
          player,
          MessageMode.ERROR,
          "profile.error.opening-menu"
        );

        return;
      }

      profileMenu.open(player, user);
    });
  }
}
