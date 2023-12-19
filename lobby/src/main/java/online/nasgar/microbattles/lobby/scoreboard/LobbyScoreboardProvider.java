package online.nasgar.microbattles.lobby.scoreboard;

import me.yushust.message.MessageHandler;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.api.model.user.kdr.KdrCalculator;
import online.nasgar.microbattles.api.scoreboard.ScoreboardProvider;
import team.unnamed.scoreboard.Board;
import team.unnamed.scoreboard.BoardRegistry;

import javax.inject.Inject;
import java.util.Set;

public class LobbyScoreboardProvider implements ScoreboardProvider<Object> {
  private final MessageHandler messageHandler;
  private final BoardRegistry boardRegistry;
  private final Set<String> usedEmptySpaces;

  @Inject
  public LobbyScoreboardProvider(MessageHandler messageHandler,
                                 BoardRegistry boardRegistry,
                                 Set<String> usedEmptySpaces) {
    this.messageHandler = messageHandler;
    this.boardRegistry = boardRegistry;
    this.usedEmptySpaces = usedEmptySpaces;
  }

  @Override
  public void apply(User user,
                    Object unused) {
    Board board = boardRegistry
      .get(user.player())
      .orElseGet(() -> boardRegistry.create(
        user.player(),
        messageHandler.get(user, "scoreboard.lobby.title")
      ));

    board.setLines(interceptLines(messageHandler.replacingMany(
      user,
      "scoreboard.lobby.lines",
      "%kills%", user.getKills(),
      "%deaths%", user.getDeaths(),
      "%kdr%", KdrCalculator.getKdr(user),
      "%wins%", user.getWonMatches()
    )));
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
