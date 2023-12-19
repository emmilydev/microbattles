package online.nasgar.microbattles.lobby.command.match;

// import de.dytanic.cloudnet.driver.CloudNetDriver;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.MessageHandler;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.lobby.menu.match.MatchesMenu;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Command(
  names = {"match", "matches"}
)
public class MatchCommand implements CommandClass {
  private final MessageHandler messageHandler;
  private final MatchesMenu matchesMenu;
  // private final CloudNetDriver cloudNetDriver;

  @Inject
  public MatchCommand(MessageHandler messageHandler,
                      MatchesMenu matchesMenu/*,
                      CloudNetDriver cloudNetDriver*/) {
    this.messageHandler = messageHandler;
    this.matchesMenu = matchesMenu;
    // this.cloudNetDriver = cloudNetDriver;
  }

  @Command(
    names = ""
  )
  public void runMatchCommand(@Sender Player player) {
    messageHandler.sendIn(
      player,
      MessageMode.INFO,
      "command.match.help"
    );
  }

  @Command(
    names = "list"
  )
  public void runMatchListCommand(@Sender Player player) {
    matchesMenu.open(player);
  }

  @Command(
    names = "join"
  )
  public void runMatchJoinCommand(@Sender Player player,
                                  @Named("match")CompletableFuture<Match> matchFuture) {
    matchFuture.whenComplete((match, error) -> {
      if (error != null) {
        ErrorNotifier.notify(error);

        messageHandler.sendIn(
          player,
          MessageMode.ERROR,
          "match.error-retrieving-match"
        );

        return;
      }

      if (match == null) {
        messageHandler.sendIn(
          player,
          MessageMode.ERROR,
          "match.not-found"
        );

        return;
      }

      player.sendMessage("Cloud not integrated yet :flushed:");
    });
  }
}
