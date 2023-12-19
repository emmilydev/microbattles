package online.nasgar.microbattles.plugin.scoreboard;

import me.yushust.message.MessageHandler;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.event.match.MatchEndEvent;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.engine.MicroBattlesEngine;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.api.scoreboard.ScoreboardProvider;
import online.nasgar.microbattles.plugin.MicroBattlesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import team.unnamed.scoreboard.Board;
import team.unnamed.scoreboard.BoardRegistry;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WaitingScoreboardProvider implements ScoreboardProvider<Match> {
  private final MessageHandler messageHandler;
  private final BoardRegistry boardRegistry;
  private final MatchManager matchManager;
  private final MicroBattlesPlugin plugin;
  private final MicroBattlesEngine engine;
  private final Set<String> usedEmptySpaces;

  @Inject
  public WaitingScoreboardProvider(MessageHandler messageHandler,
                                   BoardRegistry boardRegistry,
                                   MatchManager matchManager,
                                   MicroBattlesPlugin plugin,
                                   MicroBattlesEngine engine) {
    this.messageHandler = messageHandler;
    this.boardRegistry = boardRegistry;
    this.matchManager = matchManager;
    this.plugin = plugin;
    this.engine = engine;
    usedEmptySpaces = new HashSet<>();
  }

  @Override
  public void apply(User user,
                    Match match) {
    matchManager
      .getArena(match)
      .whenComplete((arena, error) -> {
        if (error != null) {
          ErrorNotifier.notify(error);

          for (Player player : matchManager.getPlayers(match)) {
            messageHandler.sendIn(
              player,
              MessageMode.ERROR,
              "match.error.waiting-scoreboard"
            );
          }

          Bukkit.getScheduler().runTaskLater(
            plugin,
            () -> Bukkit.getPluginManager().callEvent(new MatchEndEvent(match, null, false)),
            100
          );

          return;
        }

        if (arena == null) {
          for (Player player : matchManager.getPlayers(match)) {
            messageHandler.sendIn(
              player,
              MessageMode.ERROR,
              "match.error.arena-not-found"
            );
          }

          Bukkit.getScheduler().runTaskLater(
            plugin,
            () -> Bukkit.getPluginManager().callEvent(new MatchEndEvent(match, null, false)),
            100
          );

          return;
        }

        Board board = boardRegistry
          .get(user.player())
          .orElseGet(() -> boardRegistry.create(
            user.player(),
            messageHandler.get(user.player(), "scoreboard.waiting.title")
          ));

        List<String> lines = new ArrayList<>(messageHandler.replacingMany(
          user,
          "scoreboard.waiting.lines",
          "%server%", engine.getServiceName(),
          "%arena%", "%path_" + arena.getTranslationKey() + "%",
          "%current-players%", matchManager.countPlayers(match),
          "%max-players%", arena.getMaxPlayers(),
          "%phase%", "%path_" + match.getPhase().name().toLowerCase().replace("_", "-") + "%",
          "%version%", plugin.getDescription().getVersion()
        ));
        lines.add(messageHandler.get(user, "scoreboard.waiting.footer"));

        board.setLines(interceptLines(lines));
      });
  }

  @Override
  public void remove(User user) {
    boardRegistry
      .get(user.player())
      .ifPresent(board -> {
        board.delete();
        boardRegistry.remove(user.player());
      });
  }

  @Override
  public Set<String> getUsedEmptySpaces() {
    return usedEmptySpaces;
  }
}
