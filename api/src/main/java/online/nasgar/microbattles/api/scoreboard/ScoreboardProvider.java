package online.nasgar.microbattles.api.scoreboard;

import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Represents a scoreboard which purpose is providing
 * information about objects of type {@link T} to
 * instances of {@link User}. For example, a scoreboard
 * provider for {@link Match} instances will retrieve
 * information of the current match, such as arena name,
 * alive players, and so on.
 *
 * @param <T> The type of information displayed by this scoreboard
 *            provider.
 */
public interface ScoreboardProvider<T> {
  Random RANDOM = new Random();
  ChatColor[] COLORS = ChatColor.values();

  /**
   * Builds and applies a {@link Board} instance to
   * the given {@code user}, retrieving required information
   * from the given {@code t} object.
   *
   * @param user The user to add the scoreboard to.
   * @param t    The object to retrieve information from.
   */
  void apply(User user,
             T t);

  /**
   * Attempts to remove the given {@code user} from
   * the specific registry.
   *
   * @param user The user to be removed from the registry.
   */
  void remove(User user);

  Set<String> getUsedEmptySpaces();

  default List<String> interceptLines(List<String> lines) {
    lines.replaceAll(line -> {
      if (!line.equals("%%space%%")) {
        return line;
      }

      ChatColor color = COLORS[RANDOM.nextInt(17)];
      StringBuilder result = new StringBuilder()
        .append(ChatColor.COLOR_CHAR)
        .append(color.getChar());

      if (getUsedEmptySpaces().contains(result.toString())) {
        result
          .append(ChatColor.COLOR_CHAR)
          .append(color.getChar());
      }

      getUsedEmptySpaces().add(result.toString());

      return result.toString();
    });

    return lines;
  }
}
