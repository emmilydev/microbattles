package online.nasgar.microbattles.lobby.command;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.MessageHandler;
import online.nasgar.microbattles.api.message.MessageMode;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Command(
  names = "mblobby"
)
public class LobbyCommand implements CommandClass {
  private final MessageHandler messageHandler;

  @Inject
  public LobbyCommand(MessageHandler messageHandler) {
    this.messageHandler = messageHandler;
  }

  @Command(
    names = ""
  )
  public void runLobbyCommand(@Sender Player player) {
    messageHandler.sendIn(
      player,
      MessageMode.INFO,
      "command.lobby.help"
    );
  }
}
