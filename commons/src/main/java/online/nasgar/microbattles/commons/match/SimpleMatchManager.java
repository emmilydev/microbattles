package online.nasgar.microbattles.commons.match;

import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.dist.RemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.event.match.MatchEndEvent;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.arena.Arena;
import online.nasgar.microbattles.api.model.engine.MicroBattlesEngine;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class SimpleMatchManager implements MatchManager {
  private final RemoteModelService<Match> matchService;
  private final CachedRemoteModelService<Arena> arenaService;
  private final MicroBattlesEngine engine;
  private final CachedRemoteModelService<User> userService;
  private final MessageHandler messageHandler;
  private final JavaPlugin plugin;

  @Inject
  public SimpleMatchManager(RemoteModelService<Match> matchService,
                            CachedRemoteModelService<Arena> arenaService,
                            MicroBattlesEngine engine,
                            CachedRemoteModelService<User> userService,
                            MessageHandler messageHandler,
                            JavaPlugin plugin) {
    this.matchService = matchService;
    this.arenaService = arenaService;
    this.engine = engine;
    this.userService = userService;
    this.messageHandler = messageHandler;
    this.plugin = plugin;
  }

  @Override
  public boolean playing() {
    return matchService.findSync(engine.getServiceName()) != null;
  }

  @Override
  public CompletableFuture<Match> getCurrentMatch() {
    return matchService.find(engine.getServiceName());
  }

  @Override
  public Match getCurrentMatchSync() {
    return matchService.findSync(engine.getServiceName());
  }

  @Override
  public CompletableFuture<Match> getMatch(String id) {
    return matchService.find(id);
  }

  @Override
  public Match getMatchSync(String id) {
    return matchService.findSync(id);
  }

  @Override
  public CompletableFuture<Arena> getArena(Match match) {
    return arenaService.find(match.getArena());
  }

  @Override
  public Arena getArenaSync(Match match) {
    return arenaService.findSync(match.getArena());
  }

  @Override
  public Collection<Player> getPlayers(Match match,
                                       boolean includeSpectators) {
    return extractFromMatch(
      match,
      id -> Bukkit.getPlayer(UUID.fromString(id)),
      includeSpectators
    );
  }

  @Override
  public CompletableFuture<Collection<User>> getUsers(Match match,
                                                      boolean includeSpectators) {
    return CompletableFuture.supplyAsync(() -> getUsersSync(match, includeSpectators));
  }

  @Override
  public Collection<User> getUsersSync(Match match,
                                       boolean includeSpectators) {
    return extractFromMatch(match, userService::getOrFindSync, includeSpectators);
  }

  @Override
  public int countPlayers(Match match,
                          boolean includeSpectators) {
    return extractFromMatch(match, Function.identity(), includeSpectators).size();
  }

  @Override
  public void notify(Match match,
                     String mode,
                     String message,
                     Object... replacements) {
    Consumer<Player> notifier = createNotifier(message, mode, replacements);

    for (Player player : getPlayers(match, true)) {
      notifier.accept(player);
    }
  }

  @Override
  public void notifyPlayersAnd(Match match,
                               Consumer<Player> action,
                               Runnable after,
                               String mode,
                               String message,
                               Object... replacements) {
    Consumer<Player> notifier = this.<Player>createNotifier(message, mode, replacements).andThen(action);

    for (Player player : getPlayers(match, true)) {
      notifier.accept(player);
    }

    after.run();
  }

  @Override
  public void notifyUsersAnd(Match match,
                             Consumer<User> action,
                             Runnable after,
                             boolean sync,
                             String mode,
                             String message,
                             Object... replacements) {
    Consumer<User> notifier = this.<User>createNotifier(message, mode, replacements).andThen(action);

    getUsers(match, true).whenComplete((users, error) -> {
      if (error != null) {
        ErrorNotifier.notify(error);
      }

      Runnable performer = () -> {
        for (User user : users) {
          notifier.accept(user);
        }
      };

      if (sync) {
        Bukkit.getScheduler().runTask(plugin, performer);
      } else {
        performer.run();
      }

      after.run();
    });
  }

  private <T> Consumer<T> createNotifier(String message,
                                         String mode,
                                         Object... replacements) {
    Consumer<T> consumer;

    if (replacements == null) {
      if (mode == null) {
        consumer = player -> messageHandler.send(player, message);
      } else {
        consumer = player -> messageHandler.sendIn(player, mode, message);
      }
    } else {
      if (mode == null) {
        consumer = player -> messageHandler.sendReplacing(player, message, replacements);
      } else {
        consumer = player -> messageHandler.sendReplacingIn(player, mode, message, replacements);
      }
    }

    return consumer;
  }

  @Override
  public void cancel(Match match,
                     String cause,
                     int delay) {
    if (cause != null) {
      notify(
        match,
        MessageMode.ERROR,
        cause
      );
    }

    Bukkit.getScheduler().runTaskLater(
      plugin,
      () -> Bukkit.getPluginManager().callEvent(new MatchEndEvent(match, null, false)),
      20L * delay
    );
  }

  private <T> Collection<T> extractFromMatch(Match match,
                                             Function<String, T> parser,
                                             boolean includeSpectators) {
    Collection<String> players = new HashSet<>();

    for (Collection<String> strings : match.getTeams().values()) {
      players.addAll(strings);
    }

    if (includeSpectators) {
      players.addAll(match.getSpectators());
    }

    Collection<T> parsed = new HashSet<>();

    for (String player : players) {
      parsed.add(parser.apply(player));
    }

    return parsed;
  }

}
