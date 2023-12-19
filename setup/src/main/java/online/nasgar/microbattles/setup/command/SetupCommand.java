package online.nasgar.microbattles.setup.command;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.MessageHandler;
import online.nasgar.microbattles.api.message.MessageMode;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Command(
  names = "setup"
)
public class SetupCommand implements CommandClass {
  private final MessageHandler messageHandler;

  @Inject
  public SetupCommand(MessageHandler messageHandler) {
    this.messageHandler = messageHandler;
  }

  @Command(
    names = ""
  )
  public void runSetupCommand(@Sender Player player) {
    messageHandler.sendReplacingIn(
      player,
      MessageMode.INFO,
      "command.setup.help"
    );
  }
}
