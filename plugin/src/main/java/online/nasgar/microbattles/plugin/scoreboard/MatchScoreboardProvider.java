package online.nasgar.microbattles.plugin.scoreboard;

import me.yushust.message.MessageHandler;
import online.nasgar.microbattles.api.scoreboard.ScoreboardProvider;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import team.unnamed.scoreboard.Board;
import team.unnamed.scoreboard.BoardRegistry;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Singleton
public class MatchScoreboardProvider implements ScoreboardProvider<Match> {
  private final MessageHandler messageHandler;
  private final BoardRegistry boardRegistry;
  private final Set<String> usedEmptySpaces;

  @Inject
  public MatchScoreboardProvider(MessageHandler messageHandler,
                                 BoardRegistry boardRegistry) {
    this.messageHandler = messageHandler;
    this.boardRegistry = boardRegistry;
    usedEmptySpaces = new HashSet<>();
  }

  @Override
  public void apply(User user,
                    Match match) {
    Board board = boardRegistry
      .get(user.player())
      .orElseGet(() -> boardRegistry.create(user.player(), messageHandler.get(user, "scoreboard.match.title")));
    List<String> lines = new ArrayList<>(messageHandler.getMany(
      user, "scoreboard.match.lines.pre-teams"
    ));

    for (Map.Entry<DyeColor, Collection<String>> teamEntry : match.getTeams().entrySet()) {
      for (String team : teamEntry.getValue()) {
        String teamKey = teamEntry
          .getKey()
          .name()
          .toLowerCase()
          .replace('_', '-');

        lines.add(messageHandler.replacing(
          user,
          "scoreboard.match.lines.player-format",
          "%team%", "%path_scoreboard.match.lines.team-format." + teamKey + "%",
          "%name%", Bukkit.getOfflinePlayer(UUID.fromString(team)).getName()
        ));
      }
    }

    lines.addAll(messageHandler.getMany(user, "scoreboard.match.lines.post-teams"));
    lines.add(messageHandler.get(user, "scoreboard.match.lines.footer"));

    board.setLines(interceptLines(lines));
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

