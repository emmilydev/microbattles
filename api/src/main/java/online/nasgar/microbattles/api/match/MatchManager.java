package online.nasgar.microbattles.api.match;

import online.nasgar.microbattles.api.model.arena.Arena;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface MatchManager {
  boolean playing();

  CompletableFuture<Match> getCurrentMatch();

  Match getCurrentMatchSync();

  CompletableFuture<Match> getMatch(String id);

  Match getMatchSync(String id);

  CompletableFuture<Arena> getArena(Match match);

  Arena getArenaSync(Match match);

  Collection<Player> getPlayers(Match match,
                                boolean includeSpectators);

  default Collection<Player> getPlayers(Match match) {
    return getPlayers(match, false);
  }

  CompletableFuture<Collection<User>> getUsers(Match match,
                                               boolean includeSpectators);

  default CompletableFuture<Collection<User>> getUsers(Match match) {
    return getUsers(match, false);
  }

  Collection<User> getUsersSync(Match match,
                                boolean includeSpectators);

  default Collection<User> getUsersSync(Match match) {
    return getUsersSync(match, false);
  }

  int countPlayers(Match match,
                   boolean includeSpectators);

  default int countPlayers(Match match) {
    return countPlayers(match, false);
  }

  void notify(Match match,
              String mode,
              String message,
              Object... replacements);

  default void notify(Match match,
                      String message,
                      Object... replacements) {
    notify(
      match,
      null,
      message,
      replacements
    );
  }

  void notifyPlayersAnd(Match match,
                        Consumer<Player> action,
                        Runnable after,
                        String mode,
                        String message,
                        Object... replacements);

  default void notifyPlayersAnd(Match match,
                                Consumer<Player> action,
                                Runnable after,
                                String message,
                                Object... replacements) {
    notifyPlayersAnd(
      match,
      action,
      after,
      null,
      message,
      replacements
    );
  }

  void notifyUsersAnd(Match match,
                      Consumer<User> action,
                      Runnable after,
                      boolean sync,
                      String mode,
                      String message,
                      Object... replacements);

  default void notifyUsersAnd(Match match,
                              Consumer<User> action,
                              Runnable after,
                              boolean sync,
                              String message,
                              Object... replacements) {
    notifyUsersAnd(
      match,
      action,
      after,
      sync,
      null,
      message,
      replacements
    );
  }

  void cancel(Match match,
              String cause,
              int delay);

  default void cancel(Match match,
                      int delay) {
    cancel(match, null, delay);
  }
}
