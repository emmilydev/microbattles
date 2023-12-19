package online.nasgar.microbattles.plugin.loader;

import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.dist.RemoteModelService;
import net.cosmogrp.storage.redis.channel.Channel;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.loader.Loader;
import online.nasgar.microbattles.api.message.server.ServerStateMessage;
import online.nasgar.microbattles.api.model.arena.Arena;
import online.nasgar.microbattles.api.model.engine.MicroBattlesEngine;
import online.nasgar.microbattles.api.model.match.Match;
import online.nasgar.microbattles.plugin.MicroBattlesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameLoader implements Loader {
  private final MicroBattlesEngine engine;
  private final CachedRemoteModelService<Arena> arenaService;
  private final RemoteModelService<Match> matchService;
  private final MicroBattlesPlugin plugin;
  private final Channel<ServerStateMessage> serverStateChannel;

  @Inject
  public GameLoader(MicroBattlesEngine engine,
                    CachedRemoteModelService<Arena> arenaService,
                    RemoteModelService<Match> matchService,
                    MicroBattlesPlugin plugin,
                    Channel<ServerStateMessage> serverStateChannel) {
    this.engine = engine;
    this.arenaService = arenaService;
    this.matchService = matchService;
    this.plugin = plugin;
    this.serverStateChannel = serverStateChannel;
  }

  @Override
  public void load() {
    arenaService
      .find(engine.getArena())
      .whenComplete((arena, error) -> {
        try {
          if (error != null) {
            ErrorNotifier.notify(error);

            return;
          }

          if (arena == null) {
            plugin.getLogger().severe(String.format(
              "The arena %s, found in the provided engine, couldn't be found.",
              engine.getArena()
            ));

            return;
          }

          Map<DyeColor, Collection<String>> teams = new ConcurrentHashMap<>();

          for (DyeColor dyeColor : arena.getTeams()) {
            teams.put(dyeColor, new ArrayList<>());
          }

          Match match = new Match(
            engine.getServiceName(),
            arena.getId(),
            teams,
            0,
            engine.getFallingBlocksConfiguration().get(0)
          );

          matchService.save(match);

          serverStateChannel.sendMessage(new ServerStateMessage(
            engine.getServiceName(),
            ServerStateMessage.State.READY
          ));
        } catch (Exception e) {
          Bukkit.getLogger().info(e + "");
        }
      });
  }

  @Override
  public void unload() {
    matchService.deleteSync(engine.getServiceName());
  }
}
