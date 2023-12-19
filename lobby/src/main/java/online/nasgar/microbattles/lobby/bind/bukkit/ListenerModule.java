package online.nasgar.microbattles.lobby.bind.bukkit;

import me.yushust.inject.AbstractModule;
import online.nasgar.microbattles.lobby.listener.PlayerJoinListener;
import online.nasgar.microbattles.lobby.listener.gadget.MatchesGadgetListener;
import online.nasgar.microbattles.lobby.listener.gadget.ProfileGadgetListener;
import org.bukkit.event.Listener;

public class ListenerModule extends AbstractModule {
  @Override
  protected void configure() {
    multibind(Listener.class)
      .asSet()
      .to(PlayerJoinListener.class)
      .to(MatchesGadgetListener.class)
      .to(ProfileGadgetListener.class);
  }
}
