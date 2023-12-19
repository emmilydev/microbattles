package online.nasgar.microbattles.plugin.command;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.dist.RemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.event.user.UserLeaveMatchEvent;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

@Command(
  names = "leave"
)
public class LeaveCommand implements CommandClass {
  private final MessageHandler messageHandler;
  private final RemoteModelService<Match> matchService;

  @Inject
  public LeaveCommand(MessageHandler messageHandler,
                      RemoteModelService<Match> matchService) {
    this.messageHandler = messageHandler;
    this.matchService = matchService;
  }

  @Command(
    names = ""
  )
  public void runLeaveCommand(@Sender Player player,
                              @Sender CompletableFuture<User> userFuture) {
    userFuture.whenComplete((user, error) -> {
      if (error != null) {
        ErrorNotifier.notify(error);

        return;
      }

      if (user.getCurrentMatch() == null) {
        messageHandler.sendIn(
          player,
          MessageMode.ERROR,
          "match.not-playing"
        );

        return;
      }

      Bukkit.getPluginManager().callEvent(new UserLeaveMatchEvent(
        user,
        matchService.findSync(user.getCurrentMatch()),
        UserLeaveMatchEvent.Cause.QUIT
      ));
    });
  }
}
