package online.nasgar.microbattles.plugin.bind.bukkit;

import me.yushust.inject.AbstractModule;
import online.nasgar.microbattles.plugin.listener.player.PlayerJoinListener;
import online.nasgar.microbattles.plugin.listener.player.PlayerQuitListener;
import online.nasgar.microbattles.plugin.listener.match.MatchReadyListener;
import online.nasgar.microbattles.plugin.listener.match.MatchStartListener;
import online.nasgar.microbattles.plugin.listener.match.MatchWallsBreakListener;
import online.nasgar.microbattles.plugin.listener.user.SpectatorJoinMatchListener;
import online.nasgar.microbattles.plugin.listener.user.UserJoinMatchListener;
import online.nasgar.microbattles.plugin.listener.user.UserLeaveMatchListener;
import org.bukkit.event.Listener;

public class ListenerModule extends AbstractModule {
  @Override
  protected void configure() {
    multibind(Listener.class)
      .asSet()
      .to(MatchReadyListener.class)
      .to(MatchStartListener.class)
      .to(MatchWallsBreakListener.class)
      .to(SpectatorJoinMatchListener.class)
      .to(UserJoinMatchListener.class)
      .to(UserLeaveMatchListener.class)//
      .to(PlayerJoinListener.class)
      .to(PlayerQuitListener.class);
  }
}
